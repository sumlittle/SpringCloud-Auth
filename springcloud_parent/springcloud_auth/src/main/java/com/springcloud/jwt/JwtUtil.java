package com.springcloud.jwt;

import cn.hutool.core.date.DateUtil;

import com.springcloud.common.constant.AuthApiConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具
 */
@Component
@Slf4j
public class JwtUtil {
    /**
     * 密钥
     */
    private static final String SECRET = AuthApiConstant.AUTH_SECRET;


    /**
     * 生成用户token,设置token超时时间
     */
    public String createToken(Map map) {
        Claims claims = Jwts.claims();
        claims.put(AuthApiConstant.AUTH_USER_INFO_NAME, map);
        return Jwts.builder().setClaims(claims).setExpiration(DateUtil.offsetSecond(new Date(), Integer.parseInt(map.get(AuthApiConstant.AUTH_USER_REFRESH_NAME).toString())))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
     * 通过Token获取用户信息
     *
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return jws.getBody();
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 校验token是否过期
     *
     * @param expiration
     * @return
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

}
