package org.example.authservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.authservice.JWT.JwtUtils;
import org.example.authservice.constant.ErrorMessage;
import org.example.authservice.dto.*;
import org.example.authservice.entity.users;
import org.example.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/auth") // declare request mapping url
@RequiredArgsConstructor
public class AuthController {
    // DI
    public final AuthService authService;
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
        // Phase 1: Authentication user
        String email = usersLogin.getEmail();
        String password = usersLogin.getPassword_hash();
        Optional<users> user = authService.login(email, password);

        if (user.isEmpty()) {
            // Trả về response lỗi khi không tìm thấy người dùng
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(
                            ErrorMessage.UNAUTHORIZATION.getCode(),
                            ErrorMessage.UNAUTHORIZATION.getMessage()
                    ));
        }
        // Phase 2: Generate tokens and save them into database
        UUID userId = user.get().getId();
        String accessToken = jwtUtils.generateAccessToken(userId, expirationTime);
        String refreshToken = jwtUtils.generateRefreshToken(userId, refreshTime);
        // Phase 3: Response with status, message, and tokens
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "LOGIN SUCCESSFULLY",
                new UsersResponse(accessToken, refreshToken)));
    }

    // user registry new account
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersRegister usersRegister) {
        boolean success = authService.register(usersRegister);
        if (!success) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(
                            ErrorMessage.EMAIL_EXISTS.getCode(),
                            ErrorMessage.EMAIL_EXISTS.getMessage()
                    ));

        }
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "REGISTER SUCCESSFULLY",
                ""
        ));
    }

    // refresh token if access token is expired!
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || !jwtUtils.validateToken(refreshToken, "refresh_token")) {
            System.out.println("Refresh token timeout");
            ErrorResponse ER =   new ErrorResponse(
                    ErrorMessage.INVALID_TOKEN.getCode(),
                    ErrorMessage.INVALID_TOKEN.getMessage()
            );
            return ResponseEntity.badRequest().body(
                  ER
            );
        }
        String userId = jwtUtils.getUserIdFromToken(refreshToken);
        Optional<users> user = authService.findUserById(UUID.fromString(userId));
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            ErrorMessage.USER_NOT_FOUND.getCode(),
                            ErrorMessage.USER_NOT_FOUND.getMessage()
                    )
            );
        }
        String newAccessToken = jwtUtils.generateAccessToken(UUID.fromString(userId), expirationTime);
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "LOGIN SUCCESSFULLY",
                Map.of("accessToken", newAccessToken)
        ));
    }

    // user logout, remove token after user logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UsersResponse body) {
        String accessToken = body.getToken();
        String refreshToken = body.getRefreshToken();
        if (refreshToken == null || accessToken == null ||
                !jwtUtils.validateToken(refreshToken, "refresh_token")
                || !jwtUtils.validateToken(accessToken, "access_token")) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            ErrorMessage.INVALID_TOKEN.getCode(),
                            ErrorMessage.INVALID_TOKEN.getMessage()
                    )
            );
        }
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "successfully" ,
                jwtUtils.TimeOutToken(accessToken, refreshToken)
        ));
    }
}
