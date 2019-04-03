package com.premiere.utils.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 效验原型
 */
public class ValidatorResult {

    //效验结果是否有错
    private boolean hasError = false;
    //存放错误信息
    private Map<String, String> errorMsgMap = new HashMap<>();

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    //实现通过通用的格式化字符串信息获取错误的msg方法
    public String getErrorMsg() {
        return StringUtils.join(errorMsgMap.values());
    }
}
