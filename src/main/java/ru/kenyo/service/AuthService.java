package ru.kenyo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import ru.kenyo.dto.LoginDTO;
import ru.kenyo.exception.BadRequestException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {
    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;



    public String login(LoginDTO login) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.login(), login.password())
            );
            var authorities = authentication.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
            Instant createdAt = Instant.now();
            Instant expiredAt = createdAt.plus(30, ChronoUnit.MINUTES);

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("example.com")
                    .issuedAt(createdAt)
                    .expiresAt(expiredAt)
                    .subject(login.login())
                    .claim("roles", authorities)
                    .build();

            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid data");
        }
    }

    public void logout() {

    }
}
