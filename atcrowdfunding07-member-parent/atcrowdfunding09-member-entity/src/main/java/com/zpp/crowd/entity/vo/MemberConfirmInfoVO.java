package com.zpp.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Zpp
 * @Date : 2022/11/1-21:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberConfirmInfoVO implements Serializable {

    // 易付宝账号
    private String paynum;
    // 法人身份证号
    private String cardnum;
}
