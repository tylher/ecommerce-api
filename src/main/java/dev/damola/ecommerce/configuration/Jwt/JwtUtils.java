package dev.damola.ecommerce.configuration.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.damola.ecommerce.configuration.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${jwt_secret}")
    private String jwtSecret;
    public String issue(String id, String email, List<String> roles){
        return JWT.create()
                .withSubject(id)
                .withClaim("email",email)
                .withClaim("roles",roles)
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
                .authorities((jwt.getClaim("roles").asList(String.class).stream().map(SimpleGrantedAuthority::new))
                        .collect(Collectors.toList()))
                .build();
    }
}
