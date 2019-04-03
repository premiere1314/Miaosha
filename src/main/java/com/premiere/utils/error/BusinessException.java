package com.premiere.utils.error;

//包装器
public class BusinessException extends Exception implements CommonError {

    private CommonError error;

    //直接接受BusinessException 的参数用于构造业务异常
    public BusinessException(CommonError error){
        super();
        this.error=error;
    }

    //自定义BusinessException 的参数用于构造业务异常
    public BusinessException(CommonError error,String errorMsg){
        super();
        this.error=error;
        this.error.setErrorMsg(errorMsg);
    }

    @Override
    public int getErrorCode() {
        return this.error.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.error.getErrorMsg();
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.error.setErrorMsg(errorMsg);
        return this;
    }
}
