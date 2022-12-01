package com.zpp.crowd.mvc.config;

import com.zpp.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author : Zpp
 * @Date : 2022/10/14-22:26
 */

/**
 * 为了获取admin表中的 其他字段信息 封装了springSecurity User的子类
 *
 */
public class SecurityAdmin extends User {


    private  Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin, Collection<GrantedAuthority> authorities) {
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        this.originalAdmin=originalAdmin;

        // 将Admin对象中的密码擦除
        this.originalAdmin.setUserPswd(null);
    }

    public Admin getOriginalAdmin() {
        return originalAdmin;
    }


}
