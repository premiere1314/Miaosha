package com.premiere.controller;

import com.premiere.utils.error.BusinessException;
import com.premiere.utils.CommonReturnType;
import com.premiere.utils.error.EmBusinessError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(value = "主类继承",tags = "主项用来控制返回的数据")
public class BaseController {

    /*public static  String CONTENTTYPE="application/x-www-form-urlencoded";*/

    /**
     *
     * @param e 异常
     * @return 出现异常后的返回
     */
    @ApiOperation(value = "格式化返回给浏览器的数据",response=CommonReturnType.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)//定义ExceptionHandler 未从controller层解决的异常
    public Object HanlderException(Exception e){
        Map<String, Object> map = new HashMap<>();
        if (e instanceof BusinessException){
            BusinessException businessException =(BusinessException)e;
            map.put("errorCode",businessException.getErrorCode());
            map.put("errorMsg",businessException.getErrorMsg());

        }else {
            map.put("errorCode", EmBusinessError.UN_ERROR.getErrorCode());
            map.put("errorMsg",EmBusinessError.UN_ERROR.getErrorMsg());
        }
        return CommonReturnType.create(map,"error");
    }
}
