package com.zpp.crowd.service.api;

import com.zpp.crowd.entity.vo.AddressVO;
import com.zpp.crowd.entity.vo.OrderProjectVO;
import com.zpp.crowd.entity.vo.OrderVO;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/11/10-20:07
 */
public interface OrderService {


    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberLoginVOId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVo);

}
