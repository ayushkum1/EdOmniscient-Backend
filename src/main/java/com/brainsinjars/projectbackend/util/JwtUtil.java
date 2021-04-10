package com.brainsinjars.projectbackend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

@Service
public class JwtUtil {
    //move to .env
    private static final String SECRET = "Ae4@1.slInd!#lsk!!!332";
    private static final String REFRESH_TOKEN_SECRET = "Lso408n#@l1h242!!$$fFSCIds";
    private static final String RESET_TOKEN_SECRET = "nNOubret3038!*@*OBOB29211HS!!!";
    private final long EXPIRATION;
    private final long REFRESH_EXPIRATION;

    public JwtUtil(@Value("${jwt.expiration-ms}") long expiration, @Value("${jwt.refresh-expiration-ms}") long refresh_expiration) {
        this.EXPIRATION = expiration;
        this.REFRESH_EXPIRATION = refresh_expiration;
    }

    public String generateResetToken(String email) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, RESET_TOKEN_SECRET).compact();
    }

    public String verifyAndGetSubjectFromResetToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(RESET_TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            boolean expired = body.getExpiration().before(new Date());
            if (!expired) return body.getSubject();
            else return null;
        } catch (JwtException e) {
            return null;
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setClaims(new HashMap<>()).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.REFRESH_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, REFRESH_TOKEN_SECRET).compact();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = this.extractUsername(token, false);
        extractClaim(token, (claims) -> claims.get("roles"), false);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, false);
    }

    public Boolean validateRefreshToken(String token) {
        return !isTokenExpired(token, true);
    }

    public String extractUsername(String token, Boolean isRefresh) throws MalformedJwtException, SignatureException, ExpiredJwtException {
        return extractClaim(token, Claims::getSubject, isRefresh);
    }

    private Date extractExpiration(String token, Boolean isRefresh) throws MalformedJwtException, SignatureException, ExpiredJwtException {
        return extractClaim(token, Claims::getExpiration, isRefresh);
    }

    private Boolean isTokenExpired(String token, Boolean isRefresh) {
        return extractExpiration(token, isRefresh).before(new Date());
    }

    private Claims extractAllClaims(String token, Boolean isRefresh) throws MalformedJwtException, SignatureException, ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(isRefresh ? REFRESH_TOKEN_SECRET : SECRET)
                .parseClaimsJws(token).getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, Boolean isRefresh) throws MalformedJwtException, SignatureException, ExpiredJwtException {
        Claims claims = extractAllClaims(token, isRefresh);
        return claimsResolver.apply(claims);
    }
}
