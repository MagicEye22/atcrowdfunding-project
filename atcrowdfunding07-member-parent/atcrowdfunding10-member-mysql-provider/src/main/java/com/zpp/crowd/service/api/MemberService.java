package com.zpp.crowd.service.api;

import com.zpp.crowd.entity.po.MemberPO;

/**
 * @author : Zpp
 * @Date : 2022/10/24-22:44
 */
public interface MemberService {
    MemberPO getMemberPOByLoginAcct(String loginacct);

    void saveMember(MemberPO memberPO);

}
