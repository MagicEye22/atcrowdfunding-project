package com.zpp.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.zpp.crowd.entity.Admin;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/8/30-16:04
 */
public interface AdminService {

    Admin getAdminByLoginAcct(String username);

    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);

    void remove(Integer adminId);

    Admin getAdminById(Integer adminId);

    void update(Admin admin);

    void batchRemove(List<Integer> adminIdList);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);
}
