package com.zpp.crowd.service.impl;

import com.zpp.crowd.entity.po.MemberPO;
import com.zpp.crowd.entity.po.MemberPOExample;
import com.zpp.crowd.mapper.MemberPOMapper;
import com.zpp.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/10/24-22:44
 */
@Transactional(readOnly = true) //事务
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {

        MemberPOExample memberPOExample = new MemberPOExample();

        MemberPOExample.Criteria criteria = memberPOExample.createCriteria();

        criteria.andLoginacctEqualTo(loginacct);

        List<MemberPO> memberPOS = memberPOMapper.selectByExample(memberPOExample);

        if (memberPOS.size() == 0 || memberPOS == null) {
            return null;
        }

        return memberPOS.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
