package org.example.authservice.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.ErrorResponse;
import org.example.authservice.entity.CustomUserDetailsService;
import org.example.authservice.entity.users;
import org.example.authservice.repository.UsersRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@NonNullApi
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;
    private final UsersRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private static final List<String> WHITE_LIST = List.of(
            "/auth/login", "/auth/register", "/auth/refresh-token"
    );


    // filter request
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if (WHITE_LIST.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        // phase 1: Get token form request
        final String authHeader = request.getHeader("Authorization");
        ErrorResponse er =new ErrorResponse();
        er.setStatus(401);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token is missing or malformed. Please login."); // token missing or malformed
            return;
        }
        // phase 2: Check a token format is valid or invalid
        String token = authHeader.substring(7);
        try {
            if (!jwtUtil.validateToken(token, "access_token" )) {
                handleError(response, HttpServletResponse.SC_UNAUTHORIZED,"Token is invalid. Please login." );
                return;
            }
            // phase 3: get user from token
            String userId = jwtUtil.getUserIdFromToken(token);
            Optional<users> u = userRepository.findById(UUID.fromString(userId));

            // phase 4: spring boot check authorization
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(u.get().getEmail());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken
                                (userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (MalformedJwtException e) {
            handleError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Malformed token. Please login again.");
            return;
        } catch (ExpiredJwtException e) {
            handleError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Token has expired.");
            return;
        } catch (JwtException e) {
            handleError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid token.");
            return;
        }
        // final: if not throw exception, redirect user into request
        filterChain.doFilter(request, response);
    }
    private void handleError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);

        // Create a map with the status and message
        Map<String, Object> errorResponse = Map.of("status", status, "message", message);

        // Convert the map to a JSON string using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        // Write the JSON response
        response.setContentType("application/json");
        response.getWriter().println(jsonResponse);
    }
}

