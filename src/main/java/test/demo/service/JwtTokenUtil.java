package test.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final String SECRET = "superSecretKey"; // TODO: move to config
    private static final long EXPIRATION_MS = 86400000; // 24h

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String generateToken(Long userId) {
        return JWT.create()
                .withClaim("user_id", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .sign(algorithm);
    }

    public Long extractUserId(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        token = token.substring(7);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("user_id").asLong();
    }

}