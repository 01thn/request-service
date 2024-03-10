//package com.reqserv.requestservice.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import java.util.Date;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtUtil {
//  private final String secretKey = "your-secret-key";
//  private final long expirationTime = 864_000_000; // 10 days
//
//  public String generateToken(String username) {
//    Date now = new Date();
//    Date expiryDate = new Date(now.getTime() + expirationTime);
//
//    return Jwts.builder()
//        .setSubject(username)
//        .setIssuedAt(now)
//        .setExpiration(expiryDate)
//        .signWith(SignatureAlgorithm.HS512, secretKey)
//        .compact();
//  }
//
//  public boolean validateToken(String token) {
//    try {
//      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//      return true;
//    } catch (Exception e) {
//      return false;
//    }
//  }
//
//  public String getUsernameFromToken(String token) {
//    return Jwts.parser()
//        .setSigningKey(secretKey)
//        .parseClaimsJws(token)
//        .getBody()
//        .getSubject();
//  }
//
//}
