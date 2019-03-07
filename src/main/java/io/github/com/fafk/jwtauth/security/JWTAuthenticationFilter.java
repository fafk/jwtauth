package io.github.com.fafk.jwtauth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static io.github.com.fafk.jwtauth.security.AuthConstants.EXPIRATION_TIME;
import static io.github.com.fafk.jwtauth.security.AuthConstants.SECRET;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Value("${application.security.tokenIssuer:yourOrganization.com}")
    private String tokenIssuer;

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            io.github.com.fafk.jwtauth.user.User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), io.github.com.fafk.jwtauth.user.User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        final var claims = Jwts.claims().setSubject(((User) auth.getPrincipal()).getUsername());
        final var now = new Date();
        final var validity = new Date(now.getTime() + EXPIRATION_TIME * 1000L);
        final var token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(tokenIssuer)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        log.debug("Generated token for publisher: {} - {}", auth.getPrincipal(), token);

        res.addHeader("Authorization", "Bearer " + token);
    }
}
