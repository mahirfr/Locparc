package com.mahir.locparc.security.config;

import com.mahir.locparc.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

//    @Value("${jwt.secret}")
    private String SECRET_KEY = "tototititototititototitisomestuffjusttofilluptherequiredamountofletters";

    private Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // In here we pass the secret key to be used when encoding our message
                .build()
                .parseClaimsJws(token)
                .getBody(); // We get all the claims of the token
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }


    public String generateJwt(MyUserDetails myUserDetails) {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", myUserDetails.getUser().getFirstName());
        data.put("lastName" , myUserDetails.getUser().getLastName ());
        data.put("role"     , myUserDetails.getUser().getRole().getName());

        return Jwts
                .builder()
                .setClaims(data)
                .setSubject(myUserDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean isTokenValid(String token, MyUserDetails myUserDetails) {
        String username = extractClaims(token).getSubject();
        return username.equals(myUserDetails.getUsername());
    }


}
