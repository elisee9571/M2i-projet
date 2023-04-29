package com.example.demo.config.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    //Return body token
    public Claims extractBodyToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    //Return token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> tokenData = new HashMap<>();

        tokenData.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));

        return Jwts.builder()
                .setClaims(tokenData)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();

    }

    //Return true if token no expire
    private Boolean tokenNoDateExpiry(String token) {
        return extractBodyToken(token).getExpiration().after(new Date());
    }

    //Return true if user equal subject in token and date of not expire
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractBodyToken(token).getSubject();
        return (username.equals(userDetails.getUsername()) && tokenNoDateExpiry(token));
    }
}
