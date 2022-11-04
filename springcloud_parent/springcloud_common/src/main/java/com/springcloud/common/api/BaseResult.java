package com.springcloud.common.api;

import com.springcloud.common.constant.BaseApiConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BaseResult {

    public Map<String, Object> setResult(Integer code, String msg, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put(BaseApiConstant.HTTP_CODE_NAME, code);
        map.put(BaseApiConstant.HTTP_MSG_NAME, msg);
        if (data != null)
            map.put(BaseApiConstant.HTTP_DATA_NAME, data);
        return map;
    }

    public Map<String, Object> setResultSuccess(String msg) {
        return setResult(BaseApiConstant.HTTP_200_CODE, msg, null);
    }

    public Map<String, Object> setResultSuccess(Object data) {
        return setResult(BaseApiConstant.HTTP_200_CODE, BaseApiConstant.HTTP_200_MSG, data);
    }

    public Map<String, Object> setResultSuccess() {
        return setResult(BaseApiConstant.HTTP_200_CODE, BaseApiConstant.HTTP_200_MSG, null);
    }

    public Map<String, Object> setResultError(String msg) {
        return setResult(BaseApiConstant.HTTP_500_CODE, msg, null);
    }

    public Map<String, Object> setResultError() {
        return setResultError(BaseApiConstant.HTTP_500_MSG);
    }

    public Map<String, Object> setResult401Error() {
        return setResult(BaseApiConstant.HTTP_401_CODE, BaseApiConstant.HTTP_401_MSG, null);
    }

    public Map<String, Object> setResult403Error() {
        return setResult(BaseApiConstant.HTTP_403_CODE, BaseApiConstant.HTTP_403_MSG, null);
    }

    public Map<String, Object> setResult1000Error() {
        return setResult(BaseApiConstant.HTTP_1000_CODE, BaseApiConstant.HTTP_1000_MSG, null);
    }


    public Map<String, Object> setResult1003Error() {
        return setResult(BaseApiConstant.HTTP_1003_CODE, BaseApiConstant.HTTP_1003_MSG, null);
    }

    /*指定参数不能为空*/
    public Map<String, Object> setResult2000Error(String msg) {
        return setResult(BaseApiConstant.HTTP_2000_CODE, msg, null);
    }
}
