package com.example.demo.config.token;

import com.example.demo.entity.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtUtil {

    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String base64Key = Encoders.BASE64.encode(key.getEncoded());

    //Return body token
    public Claims extractBodyToken(String token) {
        return Jwts.parser().setSigningKey(base64Key).parseClaimsJws(token).getBody();
    }

    //Return token
    public String generateToken(UserPrincipal userPrincipal) {
        Map<String, Object> tokenData = new HashMap<>();

        tokenData.put("roles", userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));

        return Jwts.builder()
                .setClaims(tokenData)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, base64Key).compact();
    }

    //Return true if token no expire
    private Boolean tokenNoDateExpiry(String token) {
        return extractBodyToken(token).getExpiration().after(new Date());
    }

    //Return true if user equal subject in token and date of not expire
    public Boolean validateToken(String token, UserPrincipal userPrincipal) {
        final String username = extractBodyToken(token).getSubject();
        return (username.equals(userPrincipal.getUsername()) && tokenNoDateExpiry(token));
    }
}
