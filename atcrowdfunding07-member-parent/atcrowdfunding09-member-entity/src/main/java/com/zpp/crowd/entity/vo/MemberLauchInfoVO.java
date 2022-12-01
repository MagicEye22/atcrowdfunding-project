package com.zpp.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Zpp
 * @Date : 2022/11/1-21:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLauchInfoVO  implements Serializable {
    // 简单介绍
    private String descriptionSimple;
    // 详细介绍
    private String descriptionDetail;
    // 联系电话
    private String phoneNum;
    // 客服电话
    private String serviceNum;
}
