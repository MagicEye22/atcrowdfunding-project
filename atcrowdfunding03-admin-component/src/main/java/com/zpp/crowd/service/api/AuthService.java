package com.zpp.crowd.service.api;

import com.zpp.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * @author : Zpp
 * @Date : 2022/9/12-22:16
 */
public interface AuthService {
    List<Auth> getAll();

    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    void saveRoleAuthRelationship(Map<String, List<Integer>> map);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
