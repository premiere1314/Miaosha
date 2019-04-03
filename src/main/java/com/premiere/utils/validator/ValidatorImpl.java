package com.premiere.utils.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

//声明它是spring 容器的一个bean
@Component
public class ValidatorImpl implements InitializingBean {


    private Validator validator;

    /**
     * 当请求头参数发送到控制层是，会通过自定义的效验规则，进行检查
     * 实例化效验器
     */
    @Override
    public void afterPropertiesSet() {
        //将 hibernate-validator 通过工程初始化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }



    //实现效验的方法并返回效验的结果
    public ValidatorResult validatorResult(Object bean) {
        ValidatorResult result = new ValidatorResult();
        //如果bean 参数违背了 validation 定义的规则，则返回结果 validate
        Set<ConstraintViolation<Object>> validate = validator.validate(bean);
        if (validate.size() > 0) {
            result.setHasError(true);//如果大于0 则代表有错误
            //使用拉姆达表达式获取错误信息
            validate.forEach(constraintViolation -> {
                String message = constraintViolation.getMessage();//错误信息
                String propertyPath = constraintViolation.getPropertyPath().toString();//错误的名称
                result.getErrorMsgMap().put(propertyPath,message);
            });
            return result;
        }
        return result;
    }

}
