package com.zpp.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zpp.crowd.entity.Role;
import com.zpp.crowd.entity.RoleExample;
import com.zpp.crowd.mapper.RoleMapper;
import com.zpp.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/9/6-23:19
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {

        // 开启分页功能
        PageHelper.startPage(pageNum,pageSize);

        // 执行查询
        List<Role> roleList=roleMapper.selectRoleByKeyword(keyword);

        // 封装为PageInfo对象
        return new PageInfo<>(roleList);
    }

    /**
     * 角色维护 -新增
     * @param role
     */
    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    /**
     * 角色维护 - 更新
     * @param role
     */
    @Override
    public void updateRole(Role role) {

        roleMapper.updateByPrimaryKey(role);
    }

    /**
     * 角色维护 -单条和批量删除
     * @param roleIdList
     */
    @Override
    public void removeRole(List<Integer> roleIdList) {
        RoleExample roleExample = new RoleExample();

        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andIdIn(roleIdList);

        roleMapper.deleteByExample(roleExample);
    }

    /**
     * 权限分配-查询已分配
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getAssignedRole(Integer adminId) {

        return  roleMapper.selectAssignedRole(adminId);
    }

    /**
     * 权限分配-查询未分配
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {

        return  roleMapper.selectUnAssignedRole(adminId);
    }



}
