package com.springcloud.common.constant;

public interface BaseApiConstant {

    String HTTP_CODE_NAME = "code";
    String HTTP_MSG_NAME = "msg";
    String HTTP_DATA_NAME = "data";

    /**
     * 访问成功
     */
    Integer HTTP_200_CODE = 200;
    String HTTP_200_MSG = "成功";

    /**
     * 访问失败
     */
    Integer HTTP_500_CODE = 500;
    String HTTP_500_MSG = "内部错误";

    /**
     * 认证失败
     */
    Integer HTTP_401_CODE = 401;
    String HTTP_401_MSG = "认证失败";
    /**
     * 无访问权限
     */
    Integer HTTP_403_CODE = 403;
    String HTTP_403_MSG = "无访问权限";
    /**
     * token过期
     */
    Integer HTTP_1000_CODE = 1000;
    String HTTP_1000_MSG = "token过期";
    /**
     * refreshToken过期
     */
    Integer HTTP_1003_CODE = 1001;
    String HTTP_1003_MSG = "refreshToken过期";
    /**
     * 登录需要修改密码
     */
    Integer HTTP_1004_CODE = 1004;
    String HTTP_1004_MSG = "登录需要修改密码";

    /**
     * 字段为空错误编码
     */
    Integer HTTP_2000_CODE = 2000;
    String HTTP_2000_MSG = "参数不能为空";

    /**
     * 新增时参数重复编码
     */
    Integer HTTP_2001_CODE = 2001;
    String HTTP_2001_MSG = "主键编号重复";


    /**
     * 网关获取Toke参数为空
     */
    Integer HTTP_406_CODE = 406;
    String HTTP_406_MSG = "网关获取Toke参数为空";

    /**
     * Token错误
     */
    Integer HTTP_407_CODE = 407;
    String HTTP_407_MSG = "Token验证失败";
}
