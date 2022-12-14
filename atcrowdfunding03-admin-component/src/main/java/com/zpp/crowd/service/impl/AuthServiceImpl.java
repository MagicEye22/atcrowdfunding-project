package com.zpp.crowd.service.impl;

import com.zpp.crowd.entity.Auth;
import com.zpp.crowd.entity.AuthExample;
import com.zpp.crowd.mapper.AuthMapper;
import com.zpp.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author : Zpp
 * @Date : 2022/9/12-22:16
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        // 1，先获取roleId
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);
        // 删除旧的关联关系
        authMapper.deleteOldRelationship(roleId);

        // 获取authIdList
        List<Integer> authIdList = map.get("authIdArray");

        // 判断authIdList是否有效
        if (authIdList!=null && authIdList.size()>0){
            authMapper.insertNewRelationship(roleId,authIdList);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }
}
