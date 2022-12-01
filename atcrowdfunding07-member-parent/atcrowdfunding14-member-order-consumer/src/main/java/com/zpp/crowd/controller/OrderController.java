package com.zpp.crowd.controller;

import com.zpp.crowd.api.MySQLRemoteService;
import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.entity.vo.AddressVO;
import com.zpp.crowd.entity.vo.MemberLoginVO;
import com.zpp.crowd.entity.vo.OrderProjectVO;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/11/9-23:49
 */
@Controller
public class OrderController {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;


    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {

        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);

        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);

        Integer returnCount = orderProjectVO.getReturnCount();

        // 重定向到 确定页面  偷懒的方式 不写 ajax请求
        return "redirect:http://localhost/order/confirm/order/" + returnCount;
    }


    @RequestMapping("confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount, HttpSession session) {

        // 取出 orderProjectVO
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);
        // 设置回报数量
        orderProjectVO.setReturnCount(returnCount);

        // 重新存入 session
        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, orderProjectVO);


        // 获取当前已登录用户的id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberLoginVOId = memberLoginVO.getId();

        // 查询收获地址
        ResultEntity<List<AddressVO>> addressVOResultEntity = mySQLRemoteService.getAddressVORemote(memberLoginVOId);

        if (ResultEntity.SUCCESS.equals(addressVOResultEntity.getResult())) {

            List<AddressVO> AddressVOList = addressVOResultEntity.getData();
            session.setAttribute(CrowdConstant.ATTR_NAME_ADDRESS_VO_LIST, AddressVOList);

        }

        return "confirm-order";
    }


    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("returnId") Integer returnId,
                                        HttpSession session) {

        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(projectId, returnId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {

            OrderProjectVO orderProjectVO = resultEntity.getData();

            orderProjectVO.setProjectId(projectId);

            session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, orderProjectVO);
        }

        return "confirm-return";
    }
}
