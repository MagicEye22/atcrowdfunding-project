package com.zpp.crowd.mvc.config;

import com.zpp.crowd.entity.Admin;
import com.zpp.crowd.entity.Role;
import com.zpp.crowd.service.api.AdminService;
import com.zpp.crowd.service.api.AuthService;
import com.zpp.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/10/14-22:34
 */
@Component //放在父容器中 ， 子容器用不了父容器中的东西
public class CrowdUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 根据账号名称查询Admin对象
       Admin admin = adminService.getAdminByLoginAcct(username);

       // 获取adminid
        Integer adminId = admin.getId();

        // 根据adminId 查询角色信息
        List<Role> assignedRole = roleService.getAssignedRole(adminId);

        // 根据amdinId 查询权限信息
        List<String> authNames = authService.getAssignedAuthNameByAdminId(adminId);

        // 创建存储 角色和权限信息的集合
        List<GrantedAuthority>  authorities= new ArrayList<>();

        // 存入角色信息
        for (Role role: assignedRole) {
            String roleName="ROLE_"+role.getName();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authorities.add(simpleGrantedAuthority);
        }

        // 存入权限信息
        for (String authName: authNames) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);
            authorities.add(simpleGrantedAuthority);
        }

        // 封装 SecurityAdmin 对象
        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);
        return securityAdmin;
    }
}
