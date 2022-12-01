package com.zpp.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.zpp.crowd.entity.Role;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/9/6-23:19
 */
public interface RoleService {

     PageInfo<Role> getPageInfo(Integer pageNum ,Integer pageSize ,String keyword);

    void saveRole(Role role);

    void updateRole(Role role);

    void removeRole(List<Integer> roleIdList);


    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);


}
