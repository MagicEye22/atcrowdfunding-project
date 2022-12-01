package com.zpp.crowd.service.api;

import com.zpp.crowd.entity.vo.DetailProjectVO;
import com.zpp.crowd.entity.vo.PortalTypeVO;
import com.zpp.crowd.entity.vo.ProjectVO;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/11/1-21:21
 */
public interface ProjectService {

    void saveProject(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getPortalTypeVO();

    DetailProjectVO getDetailProjectVO(Integer projectId);

}
