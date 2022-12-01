package com.zpp.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Zpp
 * @Date : 2022/11/9-23:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProjectVO implements Serializable {

    private Integer id;
    private  Integer projectId;
    private String projectName;
    private String launchName;
    private String returnContent;
    private Integer returnCount;
    private Integer supportPrice;
    private Integer freight;
    private Integer orderId;
    private Integer signalPurchase;
    private Integer purchase;

}
