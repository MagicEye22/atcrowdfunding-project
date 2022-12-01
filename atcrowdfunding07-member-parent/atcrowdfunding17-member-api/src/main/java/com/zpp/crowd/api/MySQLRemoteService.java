package com.zpp.crowd.api;

import com.zpp.crowd.entity.po.MemberPO;
import com.zpp.crowd.entity.vo.*;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/10/24-22:36
 */
@FeignClient("zpp-crowd-mysql")
public interface MySQLRemoteService {

    @RequestMapping("/get/memberpo/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct);

    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);

    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,
                                             @RequestParam("memberId") Integer memberId);

    @RequestMapping("/get/portal/type/project/data/remote")
    ResultEntity<List<PortalTypeVO>> getPortalTypeVOProjectDataRemote();

    @RequestMapping("/get/project/detail/remote/{projectId}")
    ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);

    @RequestMapping("get/Order/Project/VO/Remote/{projectId}/{returnId}")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(
            // 这里的参数也可以使用  @RequestParam ，使用时将路径中的参数名删除
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId);

    @RequestMapping("/get/Address/VO/Remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberLoginVOId") Integer memberLoginVOId);

    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);

    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVo);
}
