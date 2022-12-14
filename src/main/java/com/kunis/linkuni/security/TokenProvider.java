package com.kunis.linkuni.security;

import com.kunis.linkuni.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
//    @Autowired
//    private RedisTemplate redisTemplate;

    private static final String SECRET_KEY = "Q4NSl604sgyHJj1cUeR4uUqwEkR3ycUeR4uUqwEkR3ycUeR4uUAt7WJrxMEbSF4XXyaD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4H";

    public String create(User user) {
        // 기한 지금으로부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

		/*
		{ // header
		  "alg":"HS512"
		}.
		{ // payload
		  "sub":"40288093784915d201784916a40c0001",
		  "iss": "demo app",
		  "iat":1595733657,
		  "exp":1596597657
		}.
		// SECRET_KEY를 이용해 서명한 부분
		Nn4d1MOVLZg79sfFACTIpCPKqWmpZMZQsbNrXdJJNWkRv50_l7bPLQPwhMobT4vBOG6Q3JYjhDrKFlBSaUxZOg
		 */
        // JWT Token 생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(user.getId().toString()) // sub
                .setIssuer("url app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }

    public String validateAndGetUserId(String token) {
        // parseClaimsJws메서드가 Base 64로 디코딩 및 파싱.
        // 즉, 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용 해 서명 후, token의 서명 과 비교.
        // 위조되지 않았다면 페이로드(Claims) 리턴
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

//    public boolean validateToken(String jwtToken) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
//            boolean isNotExpire = claims.getBody().getExpiration().after(new Date());
//            if (null != redisTemplate.opsForValue().get(Constant.REDIS_PREFIX + jwtToken)) {
//                log.info("이미 로그아웃 처리된 사용자");
//                return false;
//            }
//            return isNotExpire;
//        } catch (Exception e) {
//            log.info("token is not valid");
//            return false;
//        }
//    }

}