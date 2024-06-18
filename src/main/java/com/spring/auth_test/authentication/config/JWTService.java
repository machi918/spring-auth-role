package com.spring.auth_test.authentication.config;

import com.spring.auth_test.authentication.model.RolesEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;


@Service
public class JWTService {

    private final int jwtExpirationInMinutes = 60;

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = this.extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        // MEJORABLE
        if (roles.contains(new SimpleGrantedAuthority(RolesEnum.ADMIN.name()))) {
            claims.put("role", RolesEnum.ADMIN.name());
        }
        if (roles.contains(new SimpleGrantedAuthority(RolesEnum.USER.name()))) {
            claims.put("role", RolesEnum.USER.name());
        }
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // -----------------------------------------------------
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMinutes * 60 * 1000))
            .signWith(secretKey())
            .compact();
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = this.extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token){

        Jwts.parserBuilder().setSigningKey(secretKey()).toString();

        return Jwts
                .parser()
                .setSigningKey(secretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    @Bean
    public SecretKey secretKey(){
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

}
