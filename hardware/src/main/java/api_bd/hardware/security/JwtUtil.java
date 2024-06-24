package api_bd.hardware.security;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import api_bd.hardware.domain.model.Usuario;
import java.security.Key;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

   @Value("${auth.jwt.expiration-milliseg}")
   private long jwtExpirationInMillis;

   @Value("${auth.jwt.secret}")
   private String jwtSecret;

   public String gerarToken(Authentication authentication) {
      Date dataExpiracao = new Date(new Date().getTime() + jwtExpirationInMillis);
      Usuario usuario = (Usuario) authentication.getPrincipal();
      try {
         Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes("UTF-8"));
         return Jwts.builder()
               .setSubject(usuario.getUsername())
               .setIssuedAt(new Date())
               .setExpiration(dataExpiracao)
               .signWith(secretKey)
               .compact();
      } catch (Exception e) {
         System.out.println(e.getMessage());
         return "";
      }
   }

   private Claims getClaims(String token) {
      try {
         Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes("UTF-8"));
         return Jwts.parserBuilder()
               .setSigningKey(secretKey)
               .build()
               .parseClaimsJws(token)
               .getBody();
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public String getUserName(String token) {
      Claims claims = getClaims(token);
      if (claims == null) {
         return null;
      }
      return claims.getSubject();
   }

   public boolean isValidToken(String token) {
      Claims claims = getClaims(token);
      if (claims == null) {
         return false;
      }
      String email = claims.getSubject();
      Date dataExpiracao = claims.getExpiration();
      Date agora = new Date(System.currentTimeMillis());
      return email != null && agora.before(dataExpiracao);
   }
   
}
