package com.zpp.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Zpp
 * @Date : 2022/11/4-21:03
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalProjectVO {

        private Integer projectId;
        private String projectName;
        private String headerPicturePath;
        private Integer money;
        private String deployDate;
        private Integer percentage;
        private Integer supporter;

}
