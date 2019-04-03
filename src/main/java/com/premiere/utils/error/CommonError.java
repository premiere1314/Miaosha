package com.premiere.utils.error;

public interface CommonError {

    int getErrorCode();

    String getErrorMsg();

    CommonError setErrorMsg(String errorMsg);
}
