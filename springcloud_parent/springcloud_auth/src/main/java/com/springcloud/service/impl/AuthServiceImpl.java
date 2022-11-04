package com.springcloud.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.springcloud.common.api.BaseApiService;
import com.springcloud.common.constant.AuthApiConstant;
import com.springcloud.common.redis.RedisUtil;
import com.springcloud.jwt.JwtUtil;
import com.springcloud.manage.TokenManage;
import com.springcloud.service.AuthService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "auth-接口文档")
public class AuthServiceImpl extends BaseApiService implements AuthService {
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private TokenManage tokenManage;

    @ApiOperation(value = "auth-获取Token", notes = "auth-获取Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string")
    })
    @Override
    public Map<String, Object> getToken(String userName, String password) {
        //模拟用户登录
        // 验证用户名字和密码，正确后得到用户ID
        String userId = "ceshi11111";
        return setResultSuccess(tokenManage.getTokenInfo(userId, userName, 7200, 14800));
    }

    @ApiOperation(value = "auth-验证Token", notes = "auth-验证Token")
    @Override
    public Map<String, Object> verifyToken(String token) {
        if (StrUtil.isEmpty(token)) {
            return setResult401Error();
        }
        Claims claims = jwtUtil.getClaimByToken(token);
        boolean b = claims == null || claims.isEmpty() || jwtUtil.isTokenExpired(claims.getExpiration());
        if (b) {
            return setResult1000Error();
        }
        return setResultSuccess(claims.get(AuthApiConstant.AUTH_USER_INFO_NAME));
    }

    @Override
    @ApiOperation(value = "刷新Token", notes = "通过refreshToken获取token及用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refreshToken", value = "刷新token", required = true, dataType = "String")})
    public Map<String, Object> refreshToken(String refreshToken) {
        //通过refreshToken获取用户信息
        String refreshTokenKey = redisUtil.getKeys(AuthApiConstant.AUTH_REDIS_TOKENKEY + "*:" + refreshToken).stream().findFirst().orElse(null);
        if (StrUtil.isBlank(refreshTokenKey)) {
            return setResult1003Error();
        }
        Map map = (Map) redisUtil.get(refreshTokenKey);
        //产生认证token
        String token = jwtUtil.createToken(map);
        map.put(AuthApiConstant.TOKEN_NAME, token);
        return setResultSuccess(map);
    }
}
