package com.zpp.crowd.service.impl;

import com.zpp.crowd.entity.po.*;
import com.zpp.crowd.entity.vo.AddressVO;
import com.zpp.crowd.entity.vo.OrderProjectVO;
import com.zpp.crowd.entity.vo.OrderVO;
import com.zpp.crowd.mapper.AddressPOMapper;
import com.zpp.crowd.mapper.OrderPOMapper;
import com.zpp.crowd.mapper.OrderProjectPOMapper;
import com.zpp.crowd.mapper.ProjectPOMapper;
import com.zpp.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/11/10-20:07
 */

@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    private OrderPOMapper orderPOMapper;

    @Autowired
    private AddressPOMapper addressPOMapper;


    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberLoginVOId) {

        AddressPOExample addressPOExample = new AddressPOExample();
        addressPOExample.createCriteria().andMemberIdEqualTo(memberLoginVOId);

        List<AddressPO> addressPOS = addressPOMapper.selectByExample(addressPOExample);

        // 页面用的 VO
        List<AddressVO> addressVOList = new ArrayList<>();

        // PO转VO
        for (AddressPO addressPo : addressPOS) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPo, addressVO);
            addressVOList.add(addressVO);
        }

        return addressVOList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {

        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO, addressPO);

        addressPOMapper.insertSelective(addressPO);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVo) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVo, orderPO);

        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVo.getOrderProjectVO(), orderProjectPO);

        // 同时修改项目的已筹资金
        // 获取 projectId
        Integer projectId = orderVo.getOrderProjectVO().getProjectId();

        // 根据项目id 查询对应已筹资金
        ProjectPO projectPO = projectPOMapper.selectByPrimaryKey(projectId);
        Long supportMoney = projectPO.getSupportmoney();

        // 计算新的 已筹资金
        Double totalSupportMoney = supportMoney + orderVo.getOrderAmount();

        // 修改 项目的已筹资金
        projectPOMapper.updateBySupportMoney(projectId, totalSupportMoney);

        // 执行保存
        orderPOMapper.insert(orderPO);

        //orderProjectPO 保存 orderId 做关联  要设置对应 mapper.xml 开启主键回填 useGeneratedKeys="true" keyProperty="id"
        Integer id = orderPO.getId();

        orderProjectPO.setOrderId(id);

        orderProjectPOMapper.insert(orderProjectPO);

    }
}
