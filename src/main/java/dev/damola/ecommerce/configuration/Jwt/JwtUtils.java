package dev.damola.ecommerce.configuration.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.damola.ecommerce.configuration.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtils {
    @Value("${jwt_secret}")
    private String jwtSecret;
    public String issue(String id,String email){
        return JWT.create()
                .withSubject(id)
                .withClaim("email",email)
                .withExpiresAt(Instant.now().plus(Duration.of(8, ChronoUnit.HOURS)))
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public DecodedJWT decode(String token){
        return JWT.require(Algorithm.HMAC256(jwtSecret))
                .build()
                .verify(token);
    }

    public UserPrincipal getUserPrincipalFromDecodedToken(DecodedJWT jwt){
        return UserPrincipal.builder()
                .userId(jwt.getSubject())
                .email(jwt.getClaim("email").asString())
                .build();
    }
}
