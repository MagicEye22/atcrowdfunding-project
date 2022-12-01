package com.zpp.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/11/4-23:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailProjectVO {

    private Integer projectId;

    private String projectName;

    private String projectDesc;

    private Integer money;

    private Integer lastDay;

    private Integer status;

    private String statusText;

    private Integer day;

    private String deployDate;

    private Integer supportMoney;

    private Integer supporterCount;

    private Integer followerCount;

    private Integer percentage;

    private String headerPicturePath;

    private List<String> detailPicturePathList;

    private List<DetailReturnVO> detailReturnVOList;
}
