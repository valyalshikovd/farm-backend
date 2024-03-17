package com.example.farmbackend.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * Сервис для роботы с JWT.
 */
@Service
public class JwtService {
    private static String SECRET_KEY = "5909438069dd7987c18c0ec268a03f99e0a54a01c1f3016ca01f26c0d32238bc" ;
    public String extractUserEmail(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }
    public String generateToken(UserDetails userDetails){
        return  generateToken(new HashMap<>(), userDetails);
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userEmail = extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenIxpired(token);
    }
    private boolean isTokenIxpired(String token) {
        return  extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Генерирует JWT токен для пользователя.
     *
     * @param extraClaims Дополнительные данные для включения в токен (опционально).
     * @param userDetails Данные пользователя.
     * @return Строка с JWT токеном.
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return  Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Извлекает значение указанного поля из JWT токена.
     *
     * @param token JWT токен.
     * @param claimsResolver Функция, определяющая, как извлечь значение поля из Claims объекта.
     * @param <T> Тип возвращаемого значения.
     * @return Поле из JWT токена.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final  Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все поля из JWT токена.
     *
     * @param token JWT токен.
     * @return Объект Claims, содержащий все поля из токена.
     */
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Получает секретный ключ для подписи JWT токена.
     *
     * @return Ключ для подписи токена.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
