package com.springcloud.common.api;

import com.springcloud.common.constant.AuthApiConstant;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Data
public class BaseApiService extends BaseResult {
    /**
     * 获取上下文请求
     *
     * @return 请求对象
     */
    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取上下文中的用户名
     *
     * @return
     */
    public String getUserName() {
        return getRequest().getHeader(AuthApiConstant.AUTH_USER_USERNAME_NAME);
    }

    /**
     * 获取上下文中的用户id
     *
     * @return
     */
    public String getUserId() {
        return getRequest().getHeader(AuthApiConstant.AUTH_USER_ID);
    }

}
