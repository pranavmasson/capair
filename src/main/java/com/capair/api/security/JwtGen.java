package com.capair.api.security;

import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.capair.api.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGen {

    private final Logger logger = LoggerFactory.getLogger(JwtGen.class);
    
    @Value("${capair.app.jwtSecret}")
    private String jwtSecret;

    @Value("${capair.app.jwtExpiration}")
    private int jwtExpiration;
  
    @Value("${capair.app.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
          return cookie.getValue();
        } else {
          return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/user").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }
    
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/user").build();
        return cookie;
    }
    
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }
      
    private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    
    public boolean validateJwtToken(String authToken) {

        try {
          Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
          return true;
        } catch (MalformedJwtException e) {
          logger.error("Invalid JWT token: {}", e.getMessage());
          System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException e) {
          logger.error("JWT token is expired: {}", e.getMessage());
          System.out.println("JWT token is expired");
        } catch (UnsupportedJwtException e) {
          logger.error("JWT token is unsupported: {}", e.getMessage());
          System.out.println("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
          logger.error("JWT claims string is empty: {}", e.getMessage());
          System.out.println("JWT claims string is empty");
        }
    
        return false;
    }

    public String generateTokenFromUsername(String username) {   
    return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractToken(String token){
        if (token == null){
          return null;
        } 
        else if (!token.startsWith("Bearer")){
          return null;
        }
        return token.substring(7);
    }
    
}
