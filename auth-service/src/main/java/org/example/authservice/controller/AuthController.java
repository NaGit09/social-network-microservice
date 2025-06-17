package org.example.authservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.authservice.JWT.JwtUtils;
import org.example.authservice.dto.UsersLogin;
import org.example.authservice.dto.UsersRegister;
import org.example.authservice.dto.UsersResponse;
import org.example.authservice.entity.users;
import org.example.authservice.service.AuthService;
import org.example.authservice.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/auth") // declare request mapping url
@RequiredArgsConstructor
public class AuthController {
    // DI
    public final AuthService authService;
    public final TokenService tokenService;
    public final JwtUtils jwtUtils;
    // time of token expired
    public long expirationTime = 1000 * 60 * 60 * 15;
    public long refreshTime = 1000 * 60 * 60 * 24 * 7;
    // constant messageResponse
    @GetMapping("/") // Test application
    public String helloSpring() {
        return "Hello Spring";
    }

    // user login with email, password
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersLogin usersLogin, HttpServletResponse response) {
        // phase 1: authentication user
        String email = usersLogin.getEmail();
        String password = usersLogin.getPassword_hash();
        users user = authService.login(email, password);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
        // phase 2 : generate token and save token into db
        UUID userId = user.getId();
        String accessToken = jwtUtils.generateToken(userId, expirationTime);
        String refreshToken = jwtUtils.generateToken(userId, refreshTime);
        tokenService.updateRefreshToken(userId , refreshToken);
        // phase 3: response user with status code and messageResponse, token
        return ResponseEntity.ok(
                new UsersResponse("User login successfully",
                        accessToken, refreshToken));
    }

    // user registry new account
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersRegister usersRegister) {

        boolean success = authService.register(usersRegister);
        if (!success) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already registered");
        }

        return ResponseEntity.ok("Register successful");
    }

    // refresh token if access token is expired!
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || !jwtUtils.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String userId = jwtUtils.getUserIdFromToken(refreshToken);

        users user = authService.findUserById(UUID.fromString(userId));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        String newAccessToken = jwtUtils.generateToken(UUID.fromString(userId), expirationTime);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    // user logout, remove token after user logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        System.out.println("refreshToken: " + refreshToken);
        if (refreshToken == null || jwtUtils.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        tokenService.deactivateRefreshToken(refreshToken);
        return ResponseEntity.ok("Logged out successfully");
    }
}
