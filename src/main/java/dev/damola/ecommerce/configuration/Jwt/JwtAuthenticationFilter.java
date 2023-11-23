package dev.damola.ecommerce.configuration.Jwt;

import dev.damola.ecommerce.configuration.UserPrincipalAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {
        getTokenFromRequest(request).map(token->jwtUtils.decode(token))
                .map(decodedJWT ->jwtUtils.getUserPrincipalFromDecodedToken(decodedJWT))
                .map(UserPrincipalAuthenticationToken::new).
                ifPresent(principal -> SecurityContextHolder.getContext().setAuthentication(principal));

        filterChain.doFilter(request,response);

    }

    private Optional<String> getTokenFromRequest(HttpServletRequest req){
        var token = req.getHeader("Authorization");
        if(StringUtils.hasText(token)&& token.startsWith("Bearer ")){
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}
