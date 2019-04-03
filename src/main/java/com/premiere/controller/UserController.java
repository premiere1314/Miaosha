package com.premiere.controller;

import com.premiere.controller.vo.UserInfoVo;
import com.premiere.service.UserInfoService;
import com.premiere.service.model.UserInfoModel;
import com.premiere.utils.MSUtils;
import com.premiere.utils.error.BusinessException;
import com.premiere.utils.CommonReturnType;
import com.premiere.utils.error.EmBusinessError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {

    private final UserInfoService service;
    private final HttpServletRequest request;

    @Value("${APPCODE-SEND}")
    public String appCode;
    @Value("${CODE-API-HOST}")
    public String apiHost;
    @Value("${CODE-API-PATH}")
    public String apiPath;
    @Value("${TPL-ID}")
    public String tpl_id;

    @Autowired
    public UserController(UserInfoService service, HttpServletRequest request) {
        this.service = service;
        this.request = request;
    }

    /**
     * 测试
     *
     * @param id 用户ID
     * @return 页面返回数据
     * @throws BusinessException ？
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public CommonReturnType getUserInfo(String id) throws BusinessException {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        UserInfoModel userInfo = service.getUserInfo(id);
        UserInfoVo userInfoVo = this.convertUserInfoFormUserInfoModel(userInfo);
        if (null == userInfoVo) {
            //自定义异常方法
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        return CommonReturnType.create(userInfoVo);
    }

    /**
     * 用户获取opt短信
     *
     * @param telPhone 手机号
     * @return 页面返回数据
     * @throws BusinessException ？
     */
    @RequestMapping(value = "/getOptCode", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType getOptCode(@RequestParam String telPhone) throws BusinessException {
        if (StringUtils.isEmpty(telPhone)) {
            throw new BusinessException(EmBusinessError.PHONE_NOT_EXIST);
        }
        if (service.checkTelPhone(telPhone)) {
            throw new BusinessException(EmBusinessError.PHONE_IS_HAVING);
        }

        String code = MSUtils.getCode();
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(2 * 60);
        session.setAttribute(telPhone, code);

        System.out.println("appCode::" + code);
      /* if (!SendUtils.sendCode(telPhone,code,apiHost,apiPath,appCode,tpl_id)){
            throw new BusinessException(EmBusinessError.MSG_NOT_SEND);
        }*/

        //将opt的验证码用对应的手机号
        return CommonReturnType.create("success");
    }

    /**
     * 检查用户名是否重复
     *
     * @param name 用户名
     * @return 页面返回数据
     * @throws BusinessException ？
     */
    @RequestMapping(value = "/checkUserName", method = RequestMethod.GET)
    public CommonReturnType checkUserName(String name) throws BusinessException {
        if (StringUtils.isEmpty(name)) {
            throw new BusinessException(EmBusinessError.USER_NOT_NAME);
        }
        if (service.checkUserName(name)) {
            return CommonReturnType.create("checked");
        }
        return CommonReturnType.create("having");
    }


    /**
     * 注册用户
     *
     * @param name         用户名
     * @param password     密码
     * @param telPhone     手机号
     * @param age          年龄
     * @param registerMode 注册方式
     * @param thirdPayId   支付方式
     * @param optCode      验证码
     * @return 页面返回数据
     * @throws BusinessException        抛出异常
     * @throws NoSuchAlgorithmException 抛出异常
     */
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType insertUser(@RequestParam String name,
                                       @RequestParam String password,
                                       @RequestParam String telPhone,
                                       @RequestParam Integer age,
                                       @RequestParam String registerMode,
                                       @RequestParam String thirdPayId,
                                       @RequestParam String optCode) throws BusinessException, NoSuchAlgorithmException {
        if (null == optCode) {
            throw new BusinessException(EmBusinessError.MSG_NOT_TRUE);
        } else if (null == request.getSession().getAttribute(telPhone)) {
            throw new BusinessException(EmBusinessError.PHONE_NOT_EXIST);
        } else if (!StringUtils.equals(optCode, (String) request.getSession().getAttribute(telPhone))) {
            throw new BusinessException(EmBusinessError.MSG_NOT_TRUE);
        } else if (!service.checkUserName(name)) {
            throw new BusinessException(EmBusinessError.USER_HAVE_NAME);
        } else {
            UserInfoModel userInfoModel = new UserInfoModel();
            userInfoModel.setName(name);
            userInfoModel.setPassword(password);
            userInfoModel.setAge(age);
            userInfoModel.setTelPhone(telPhone);
            userInfoModel.setRegisterMode(registerMode);
            userInfoModel.setThirdPayId(thirdPayId);
            service.insertUser(userInfoModel);
        }
        return CommonReturnType.create("success");
    }

    /**
     * 用户登录
     *
     * @param name     用户名
     * @param password 密码
     * @return 页面返回数据
     * @throws BusinessException        ？
     * @throws NoSuchAlgorithmException ？
     */
    @RequestMapping(value = "/loginUser", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType loginUser(@RequestParam String name,
                                      @RequestParam String password) throws BusinessException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.USER_CANT_NOT_PASS);
        }
        UserInfoModel userInfoModel = service.getLoginUserInfoModel(name, MSUtils.EncodeByMD5(password));
        if (null == userInfoModel) {
            throw new BusinessException(EmBusinessError.USER_NAME_PASS_ERROR);
        } else {
            request.getSession().setAttribute("USER_INFO", userInfoModel);
            request.getSession().setAttribute("IS_LOGIN", true);
        }
        return CommonReturnType.create(userInfoModel);
    }


    /**
     * 格式化想页面传输的值
     *
     * @param userInfo 用户类
     * @return 用户包装类
     */
    private UserInfoVo convertUserInfoFormUserInfoModel(UserInfoModel userInfo) {
        if (null == userInfo) {
            return null;
        }
        UserInfoVo infoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, infoVo);
        return infoVo;
    }


}
