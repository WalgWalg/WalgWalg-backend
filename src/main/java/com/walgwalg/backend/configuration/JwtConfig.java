package com.walgwalg.backend.configuration;

import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/secrets/jwt-secrets.properties")
@Configuration
public class JwtConfig {
    @Value("${jwt.secret}") //secret을 환경 변수에서 가져옴
    private String secret;

    @Bean
    public JwtAuthTokenProvider jwtProvider(){ //provider를 bean으로 등록
        return new JwtAuthTokenProvider(secret);
    }

}
