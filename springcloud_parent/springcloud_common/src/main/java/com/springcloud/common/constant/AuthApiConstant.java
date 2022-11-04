package com.springcloud.common.constant;

public interface AuthApiConstant {
    /**
     * 鉴权获取token地址
     */
    String AUTH_TOKEN_GETTOKE_URL = "/auth/getToken";

    /**
     * 刷新Token地址
     */
    String AUTH_ReFRESH_TOKEN_URL = "/auth/refreshToken";

    /**
     * 验证Token地址
     */
    String AUTH_VERIFY_TOKEN_URL = "/auth/verifyToken";

    /**
     * Swagger访问地址
     */
    String SWAGGER_DOC_URL = "/doc.html";

    /**
     * Swagger 文档地址
     */
    String SWAGGER_API_DOC = "/v2/api-docs";

    /**
     * Swagger 静态资源
     */
    String SWAGGER_STATE_RESOURCE = "/webjars/bycdao-ui/";


    /**
     * token
     */
    String TOKEN_NAME = "token";


    /**
     * 用户信息
     */
    String AUTH_USER_INFO_NAME = "info";

    /**
     * 用户名
     */
    String AUTH_USER_USERNAME_NAME = "userName";

    /**
     * 用户id
     */
    String AUTH_USER_ID = "userId";

    /**
     * 用户token过期时间名称
     */
    String AUTH_USER_REFRESH_NAME = "tokenExpire";

    /**
     * token在Redis中的文件目录
     */
    String AUTH_REDIS_TOKENKEY = "token";

    /**
     * 用户刷新token名称
     */
    String AUTH_USER_REFRESHTOKEN_NAME = "refreshToken";

    /**
     * 用户刷新token周期名称
     */
    String AUTH_USER_REFRESHTOKENEXPIRE_NAME = "refreshTokenExpire";


    /**
     * JWT秘钥
     */
    String AUTH_SECRET = "my_secret";

    /**
     * Swagger标题
     */
    String SWAGGER_TITLE = "测试";

    /**
     * Swagger版本
     */
    String SWAGGER_VERSION = "1.0";

    /**
     * Swagger前缀
     */
    String X_FORWARDED_PREFIX = "X-Forwarded-Prefix";

}
