package net.bilal.appeldoffresbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET =
            "monSecretJwtSuperLongPourSpringBoot2026BilalProjet";

    private Key getSignKey() {

        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // GENERATE TOKEN
    public String generateToken(String username) {

        return Jwts.builder()

                .setSubject(username)

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60)
                )

                .signWith(getSignKey(), SignatureAlgorithm.HS256)

                .compact();
    }

    // EXTRACT USERNAME
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    // EXTRACT CLAIM
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    // EXTRACT ALL CLAIMS
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(getSignKey())

                .build()

                .parseClaimsJws(token)

                .getBody();
    }

    // CHECK TOKEN VALIDITY
    public boolean isTokenValid(
            String token,
            String username
    ) {

        final String extractedUsername =
                extractUsername(token);

        return extractedUsername.equals(username)
                && !isTokenExpired(token);
    }

    // CHECK EXPIRATION
    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    // EXTRACT EXPIRATION
    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }
}