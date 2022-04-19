package com.walgwalg.backend.provider.security;


import com.walgwalg.backend.core.security.AuthToken;
import com.walgwalg.backend.exception.errors.CustomJwtRuntimeException;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtAuthToken implements AuthToken<Claims> {

    private final Key key;
    @Getter
    private final String token;

    private static final String AUTHORITIES_KEY = "role";

    JwtAuthToken(String token, Key key){
        this.key = key;
        this.token = token;
    }
    JwtAuthToken(String id, String role, Date expiredDate, Key key){
        this.key = key;
        this.token = Jwts.builder()
                .setSubject(id) // 토큰 제목 여기서는 유저 아이디
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact();
    }

    @Override
    public boolean validate(){
        return getClaims() != null;
    }

    @Override
    public Claims getClaims(){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (SecurityException e){
            log.info("Invalid JWT signature.");
            throw new CustomJwtRuntimeException();
        }catch (MalformedJwtException e){
            log.info("Invalid JWT token.");
            throw new CustomJwtRuntimeException();
        }catch (ExpiredJwtException e){
            log.info("Expired JWT token.");
            throw new CustomJwtRuntimeException();
        }catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token.");
            throw new CustomJwtRuntimeException();
        }catch (IllegalArgumentException e){
            log.info("JWT token compact of handler are invalid.");
            throw new CustomJwtRuntimeException();
        }
    }
}
