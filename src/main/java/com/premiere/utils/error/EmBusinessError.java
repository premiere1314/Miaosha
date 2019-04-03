package com.premiere.utils.error;

public enum EmBusinessError implements CommonError {
    //系统错误
    UN_ERROR(900001,"未知错误"),

    /***************************    用户异常    *******************************/
    //用户错误
    USER_NOT_EXIST(100001,"用户信息不存在"),
    USER_NOT_NAME(100002,"用户名获取失败或为空"),
    USER_NOT_PASS(100003,"用户密码获取失败或为空"),
    USER_CANT_NOT_PASS(100003,"用户或密码不能为空"),
    USER_NAME_PASS_ERROR(100003,"用户或密码错误"),
    USER_HAVE_NAME(100005,"用户名重复"),
    USER_ADD_ERROR(100006,"用户添加失败"),

    /****************************   短信异常    ******************************/


    PHONE_NOT_EXIST(100013,"手机号参数有误"),
    MSG_NOT_SEND(100014,"短信发送失败请重试"),
    MSG_NOT_TRUE(100015,"短信验证失败请重试"),
    PHONE_IS_HAVING(100015,"手机号已存在"),

    /***************************    订单异常   ******************************/

    USER_IS_NULL(200001,"用户信息不存在，或未登录"),
    ITEM_IS_NULL(200002,"商品信息不存在"),
    ITEM_NUM_ERROR(200003,"商品库存不足"),
    ;

    private int errorCode;
    private String errorMsg;

    /**
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     */
    EmBusinessError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errorMsg=errorMsg;
        return this;
    }

}