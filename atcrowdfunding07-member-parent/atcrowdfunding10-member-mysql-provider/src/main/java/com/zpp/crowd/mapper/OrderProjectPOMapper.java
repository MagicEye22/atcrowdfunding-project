package com.zpp.crowd.mapper;


import java.util.List;

import com.zpp.crowd.entity.po.OrderProjectPO;
import com.zpp.crowd.entity.po.OrderProjectPOExample;
import com.zpp.crowd.entity.vo.OrderProjectVO;
import org.apache.ibatis.annotations.Param;

public interface OrderProjectPOMapper {
    int countByExample(OrderProjectPOExample example);

    int deleteByExample(OrderProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderProjectPO record);

    int insertSelective(OrderProjectPO record);

    List<OrderProjectPO> selectByExample(OrderProjectPOExample example);

    OrderProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderProjectPO record, @Param("example") OrderProjectPOExample example);

    int updateByExample(@Param("record") OrderProjectPO record, @Param("example") OrderProjectPOExample example);

    int updateByPrimaryKeySelective(OrderProjectPO record);

    int updateByPrimaryKey(OrderProjectPO record);

   OrderProjectVO selectOrderProjectVO(@Param("returnId")Integer returnId);
}