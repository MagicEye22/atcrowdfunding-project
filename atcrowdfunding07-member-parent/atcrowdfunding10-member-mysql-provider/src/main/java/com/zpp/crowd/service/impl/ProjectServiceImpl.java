package com.zpp.crowd.service.impl;

import com.zpp.crowd.entity.po.MemberConfirmInfoPO;
import com.zpp.crowd.entity.po.MemberLaunchInfoPO;
import com.zpp.crowd.entity.po.ProjectPO;
import com.zpp.crowd.entity.po.ReturnPO;
import com.zpp.crowd.entity.vo.*;
import com.zpp.crowd.mapper.*;
import com.zpp.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/11/1-21:21
 */
@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {

        // 保存projectPO
        ProjectPO projectPO = new ProjectPO();

        // 从projectVO复制属性
        BeanUtils.copyProperties(projectVO, projectPO);

        // memberId
        projectPO.setMemberid(memberId);

        // 项目创建时间
        String creteData = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(creteData);

        // 状态
        projectPO.setStatus(0);

        // 保存projectVo
        projectPOMapper.insertSelective(projectPO);

        // 获取 projectId
        Integer projectId = projectPO.getId();

        // 保存项目，分类的关联关系信息
        // 从projectVO对象 获取 typeIdList
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeIdList, projectId);

        // 保存项目，标签的关联关系信息
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagRelationship(tagIdList, projectId);

        // 保存项目 详情图片的路径信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(projectId, detailPicturePathList);


        // 保存项目发起人的信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);

        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        // 保存项目回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();

        List<ReturnPO> returnPOList = new ArrayList<>();
        // 遍历 VO 转 PO 保存
        for (ReturnVO returnVO : returnVOList) {

            ReturnPO returnPO = new ReturnPO();

            BeanUtils.copyProperties(returnVO, returnPO);

            returnPOList.add(returnPO);

        }

        returnPOMapper.insertReturnPOBatch(returnPOList, projectId);

        // 保存项目的确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);

        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }


    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {

        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        // 设置状态码对应项目状态信息
        Integer status = detailProjectVO.getStatus();
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }

        // 计算lastDay
        String deployDate = detailProjectVO.getDeployDate();

        // 获取当前日期
        Date currentDay = new Date();

        // 把众筹日期转换为 Data日期类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date deployDay = format.parse(deployDate);

            // 获取当前日期的时间戳
            long currentTimeStamp = currentDay.getTime();

            // 获取项目日期的时间戳
            long deployTimeStamp = deployDay.getTime();

            // 计算当前过去的时间 换算成天
            long pastDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;

            // 获取项目总天数
            Integer totalDay = detailProjectVO.getDay();

            // 计算LastDay
            Integer LastDat = (int) (totalDay - pastDays);

            detailProjectVO.setLastDay(LastDat);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return detailProjectVO;
    }
}
