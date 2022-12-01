package com.zpp.crowd.mapper;


import com.zpp.crowd.entity.Admin;
import com.zpp.crowd.entity.AdminExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AdminMapper {
    int countByExample(AdminExample example);

    int deleteByExample(AdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectAdminByKeyword(@Param("keyword") String keyword);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    void deleteOldRelationship(Integer adminId);

    void insertNewRelationship(@Param("adminId")Integer adminId, @Param("roleIdList") List<Integer> roleIdList);
}