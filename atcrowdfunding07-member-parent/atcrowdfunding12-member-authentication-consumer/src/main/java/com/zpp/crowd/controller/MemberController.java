package com.zpp.crowd.controller;

import com.zpp.crowd.api.MySQLRemoteService;
import com.zpp.crowd.api.RedisRemoteService;
import com.zpp.crowd.config.ShortMessageProperties;
import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.entity.po.MemberPO;
import com.zpp.crowd.entity.vo.MemberLoginVO;
import com.zpp.crowd.entity.vo.MemberVO;
import com.zpp.crowd.util.CrowdUtil;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : Zpp
 * @Date : 2022/10/26-23:00
 */
@Controller
public class MemberController {

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/member/logout")
    public  String logout(HttpSession session){

        session.invalidate();
        return "redirect:http://localhost/";
    }


    /**
     * 登录
     * @param loginacct
     * @param userpswd
     * @param modelMap
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/do/login")
    public String login(@RequestParam("loginacct")String loginacct,
                        @RequestParam("userpswd")String userpswd ,
                        ModelMap modelMap,
                        HttpSession session){

        // 调用远程接口 查询member对象
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        if (ResultEntity.FAILED.equals(resultEntity.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return  "member-login";
        }

        MemberPO memberPO = resultEntity.getData();
        // 账号不存在
        if (memberPO==null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return  "member-login";
        }

        // 密码校验
        // 数据库的密码
        String dataBaseUserpswd = memberPO.getUserpswd();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        boolean matchesResult = bCryptPasswordEncoder.matches(userpswd,dataBaseUserpswd);
        // 密码错误
        if (!matchesResult){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return  "member-login";
        }

        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(),memberPO.getUsername(),memberPO.getEmail());

        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);

        return "redirect:http://localhost/auth/member/to/center/page.html";
    }


    /**
     *  注册
     * @param memberVO
     * @param modelMap
     * @return
     */
    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {


        // 获取输入的手机号
        String phoneNumber = memberVO.getPhoneNumber();

        // 拼接Redis中存储的key
        String key = CrowdConstant.REDIS_AUTH_CODE_PREFIX + phoneNumber;

        // 从redis中取出携带验证码的 ResultEntity
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);


        // 检查查询操作是否有效
        String result = resultEntity.getResult();

        // 操作有效-- redis查询失败
        if (ResultEntity.FAILED.equals(result)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }

        String redisCode = resultEntity.getData();

        // 验证码为空
        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_AUTH_CODE_NOT_EXISTS);
            return "member-reg";
        }

        // 查询到验证码，比较表单提交的验证码
        String formAuthCode = memberVO.getAuthCode();
        
        // 验证码不一致
        if (!Objects.equals(formAuthCode, redisCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_AUTH_CODE_INVALID);
            return "member-reg";
        }


        // 验证码一致，从redis中删除验证码
        redisRemoteService.removeRedisKeyRemote(key);


        // 执行密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String userpswd = memberVO.getUserpswd();
        String encodePassword = bCryptPasswordEncoder.encode(userpswd);
        memberVO.setUserpswd(encodePassword);


        // MemberVO 转换为 MemberPO
        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(memberVO, memberPO);

        // 执行保存
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);

        // 保存失败 -- loginacct 重复
        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveMemberResultEntity.getMessage());
            return "member-reg";

        }

        // 重定向
        return "redirect:http://localhost/auth/member/to/login/page.html";
    }

    /**
     *
     * 获取验证码
     * @param phoneNumber
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/auth/member/send/short/message")
    public ResultEntity<String> sendMassage(@RequestParam("phoneNumber") String phoneNumber) {

        // 手机号为空
        if (phoneNumber==null|| phoneNumber.equals("")){
            return ResultEntity.failed(CrowdConstant.MESSAGE_PHONE_NUMBER_IS_NULL);
        }

        // 发送验证码到指定手机号
        ResultEntity<String> resultEntity = CrowdUtil.sendAuthCodeByShortMessage(phoneNumber,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getTemplateId(),
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getMethod());
        // 判断短信发送的结果
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            // 发送成功 验证码存入redis
            String authCode = resultEntity.getData();

            // 拼接用于在Redis中存储的key
            String key = CrowdConstant.REDIS_AUTH_CODE_PREFIX + phoneNumber;

            // 调用远程接口存入redis 设置过期时间 5分钟
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.
                    setRedisKeyValueRemoteWithTimeout(key, authCode, 5, TimeUnit.MINUTES);

            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }

        } else {
            return resultEntity;
        }


    }
}
