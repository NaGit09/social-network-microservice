package org.example.authservice.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.dto.UsersResponse;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtUtils {

    private static final String SECRET_KEY = "SK40CSI2PC-D#?4klj50fdl3kd3ldư;l3ke";

    // generate signature from a private key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    // generate token from userId and time token expired
    public String generateAccessToken(UUID userId, long ACCESS_TOKEN_EXPIRATION) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("token_type", "access_token")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateRefreshToken(UUID userId, long REFRESH_TOKEN_EXPIRATION) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("token_type", "refresh_token")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean validateToken(String token, String expectedTokenType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("token_type", String.class);

            if (tokenType == null) {
                System.out.println("Token không có loại xác định (token_type)");
                return false;
            }

            if (!tokenType.equals(expectedTokenType)) {
                System.out.println("Loại token không khớp: " + tokenType);
                return false;
            }

            return true;

        } catch (ExpiredJwtException e) {
            System.out.println("Token đã hết hạn: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Token không hỗ trợ: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Token bị lỗi: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi tham số token: " + e.getMessage());
        }
return false;
    }
    // get userId from token return claims
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    // Timeout token
    public UsersResponse TimeOutToken (String token  , String refreshToken) {
        UUID userId = UUID.fromString(getUserIdFromToken(token));
        return new UsersResponse(
                generateAccessToken(userId , 0) ,
                generateRefreshToken(userId , 0)
        ) ;
    }
}
