package com.zpp.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Zpp
 * @Date : 2022/10/28-1:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginVO  implements Serializable {

    private Integer id;

    private String username;

    private String email;
}
