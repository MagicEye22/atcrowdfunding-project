package com.zpp.crowd.controller;

import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.entity.po.MemberPO;
import com.zpp.crowd.service.api.MemberService;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Zpp
 * @Date : 2022/10/24-22:45
 */

@RestController
public class MemberProviderController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO) {

        try {

            memberService.saveMember(memberPO);
            return ResultEntity.successWithoutData();

        } catch (Exception e) {
           // e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }

            return ResultEntity.failed(e.getMessage());
        }

    }

    @RequestMapping("/get/memberpo/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(
            @RequestParam("loginacct") String loginacct) {

        try {
            // 调用本地service完成本地查询
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginacct);

            // 查询成功
            return ResultEntity.successWithoutData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();

            // 查询失败
            return ResultEntity.failed(e.getMessage());
        }

    }

}
