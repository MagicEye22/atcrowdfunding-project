package com.zpp.crowd.controller;

import com.zpp.crowd.api.MySQLRemoteService;
import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.entity.vo.PortalTypeVO;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/10/25-20:25
 */
@Controller
public class PortalController {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showPortalPage(ModelMap modelMap) {

        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeVOProjectDataRemote();

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {

            List<PortalTypeVO> portalTypeVOList = resultEntity.getData();

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,portalTypeVOList);
        }

        return "protal";
    }
}
