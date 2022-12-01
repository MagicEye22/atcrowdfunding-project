package com.zpp.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Zpp
 * @Date : 2022/10/27-22:56
 */

/**
 * 页面表单提交数据对应的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {

    private String loginacct;

    private String userpswd;

    private String username;

    private String email;

    private String phoneNumber;

    private String authCode;
}
