package com.zpp.crowd.controller;

import com.zpp.crowd.entity.vo.AddressVO;
import com.zpp.crowd.entity.vo.OrderProjectVO;
import com.zpp.crowd.entity.vo.OrderVO;
import com.zpp.crowd.service.api.OrderService;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/11/10-0:06
 */

@RestController
public class OrderProviderController {

    @Autowired
    private OrderService orderService;


    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVo){

        try {
            orderService.saveOrder(orderVo);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }


    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO){

        try {
            orderService.saveAddress(addressVO);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }

    }

    @RequestMapping("/get/Address/VO/Remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberLoginVOId") Integer memberLoginVOId){

        try {
            List<AddressVO> addressVOS =orderService.getAddressVOList(memberLoginVOId);

            return ResultEntity.successWithoutData(addressVOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }


    }

    @RequestMapping("get/Order/Project/VO/Remote/{projectId}/{returnId}")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@PathVariable("projectId") Integer projectId,
                                                         @PathVariable("returnId") Integer returnId) {

        try {

            OrderProjectVO orderProjectVO = orderService.getOrderProjectVO(projectId, returnId);

            return ResultEntity.successWithoutData(orderProjectVO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }

}
