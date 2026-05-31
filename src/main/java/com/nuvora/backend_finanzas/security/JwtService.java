package com.nuvora.backend_finanzas.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.unknow}")
    private String secretKey;

    @Value("${jwt.exp}")
    private Long expTime;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(Long usuarioId){
        return Jwts.builder()
                .setSubject(String.valueOf(usuarioId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .signWith(getSigningKey())
                .compact();
    }

    public Long validateTokenAndGetUserId(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());

    }

}
