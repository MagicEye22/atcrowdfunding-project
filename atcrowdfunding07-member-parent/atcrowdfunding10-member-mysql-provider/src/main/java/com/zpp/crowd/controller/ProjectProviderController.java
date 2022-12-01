package com.zpp.crowd.controller;

import com.zpp.crowd.entity.vo.DetailProjectVO;
import com.zpp.crowd.entity.vo.PortalTypeVO;
import com.zpp.crowd.entity.vo.ProjectVO;
import com.zpp.crowd.service.api.ProjectService;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/11/1-21:20
 */
@RestController
public class ProjectProviderController {

    @Autowired
    private ProjectService projectService;


    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId")Integer projectId){

        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);

            return ResultEntity.successWithoutData(detailProjectVO);

        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }


    }

    @RequestMapping("/get/portal/type/project/data/remote")
   public ResultEntity<List<PortalTypeVO>> getPortalTypeVOProjectDataRemote(){

        try {
            List<PortalTypeVO> portalTypeVO = projectService.getPortalTypeVO();

            return ResultEntity.successWithoutData(portalTypeVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }



    @RequestMapping("/save/project/vo/remote")
    public  ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,
                                             @RequestParam("memberId") Integer memberId) {

        try {
            // 执行保存
            projectService.saveProject(projectVO, memberId);

            return ResultEntity.successWithoutData();

        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }
}
