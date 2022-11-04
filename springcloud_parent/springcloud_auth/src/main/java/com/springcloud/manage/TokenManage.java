package com.springcloud.manage;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.springcloud.common.constant.AuthApiConstant;
import com.springcloud.common.redis.RedisUtil;
import com.springcloud.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenManage {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 生成token
     *
     * @param userId             用户id
     * @param userName           名字
     * @param expire             token 过期实际时间
     * @param refreshTokenExpire refreshToken过期时间
     * @return
     */
    public Map<String, Object> getTokenInfo(String userId, String userName,
                                            int expire, int refreshTokenExpire) {
        Map<String, Object> map = new HashMap();
        map.put(AuthApiConstant.AUTH_USER_USERNAME_NAME, userName); //用户名
        map.put(AuthApiConstant.AUTH_USER_ID, userId);    //用户编号
        map.put(AuthApiConstant.AUTH_USER_REFRESH_NAME, expire);// Token过期时间
        //产生认证token
        String token = jwtUtil.createToken(map);
        //生成refreshToken
        String refreshToken = redisUtil.getKeys(AuthApiConstant.AUTH_REDIS_TOKENKEY + userId + ":*").stream().findFirst().orElse(null);
        if (StrUtil.isBlank(refreshToken)) {
            refreshToken = IdUtil.simpleUUID();
        } else {
            refreshTokenExpire = Convert.toInt(redisUtil.getExpire(refreshToken));
            refreshToken = refreshToken.substring(refreshToken.lastIndexOf(":") + 1);
        }
        //将用户信息map放到redis
        redisUtil.set(AuthApiConstant.AUTH_REDIS_TOKENKEY + userId + ":" + refreshToken, map, refreshTokenExpire);
        map.put(AuthApiConstant.TOKEN_NAME, token);
        map.put(AuthApiConstant.AUTH_USER_REFRESHTOKEN_NAME, refreshToken);
        map.put(AuthApiConstant.AUTH_USER_REFRESHTOKENEXPIRE_NAME, refreshTokenExpire);
        return map;
    }
}
