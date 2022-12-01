package com.zpp.crowd.controller;

import com.zpp.crowd.api.MySQLRemoteService;
import com.zpp.crowd.config.OSSProperties;
import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.entity.vo.*;
import com.zpp.crowd.util.CrowdUtil;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/11/2-21:14
 */

@Controller
public class ProjectController {

    /**
     * OSS服务 配置
     */
    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    /**
     *  项目详情
     * @param projectId
     * @param modelMap
     * @return
     */
    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId")Integer projectId ,ModelMap modelMap){

        ResultEntity<DetailProjectVO> detailProjectVORemote = mySQLRemoteService.getDetailProjectVORemote(projectId);

        if(ResultEntity.SUCCESS.equals(detailProjectVORemote.getResult())){
            DetailProjectVO detailProjectVO = detailProjectVORemote.getData();

            modelMap.addAttribute("detailProjectVO",detailProjectVO);

        }


        return "project-show-detail";
    }



    /**
     *   将projectVO提交到 数据
     * @param memberConfirmInfoVO 确认页面信息
     * @param session  。。
     * @param modelMap 。。
     * @return
     */
    @RequestMapping("/create/confirm")
    public String saveConfirm(MemberConfirmInfoVO memberConfirmInfoVO,HttpSession session,ModelMap modelMap){

        // 取出 projectVo 对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        // 检查
        if(projectVO==null){
            throw  new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }

        // 收集到的 确定信息 存入 projectVo
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        // session 域都区当前登录的用户
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        // 取出 memberId
        Integer memberId = memberLoginVO.getId();
        // 调用 远程接口将projectVO 存入数据库
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(projectVO, memberId);

        // 检查是否存入成功
        if (ResultEntity.FAILED.equals(saveResultEntity.getResult())){ //失败
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResultEntity.getMessage());
            return "project-confirm";
        }

        // session 域 移除 projectVO
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        // 成功 跳转成功页面
        return "redirect:http://localhost/project/create/success/page.html";
    }


    /**
     *  将回报页面的数据 封装到projectVo中 ，为了后面存入数据库
     * @param returnVO 页面收集的信息
     * @param session 为了获取 redis中的 projectVO对象
     * @return
     */
    @ResponseBody
    @RequestMapping("/create/save/return")
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session) {

        try {
            // 从session域中读取 projectVO对象
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

            // 对取出的 projectVO对象进行 检查
            if (projectVO == null) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }

            //  projectVO 获取 存储 回报 信息的集合
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();

            // 检查 returnVOList
            if (returnVOList == null || returnVOList.size() == 0) {
                returnVOList = new ArrayList<>();
                projectVO.setReturnVOList(returnVOList);
            }

            // 存入 projectVo 放到session中 最后存入数据库
            returnVOList.add(returnVO);

            // 将最新的 projectVO 存入 session
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            // e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }


    }


    /**
     * 回报页面 图片上传
     *
     * @param returnPicture
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/create/upload/return/picture")
    public ResultEntity<String> upLoadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {

        // 进行上传
        ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename());

        // 返回上传的结果
        return resultEntity;

    }


    /**
     * @param projectVO         // 接收除了上传图片之外的其他普通数据
     * @param headerPicture     // 接收上传的头图
     * @param detailPictureList // 接收上传的详情图片
     * @param session           // 用来将收集了一部分数据的 ProjectVO 对象存入 Session
     * @param modelMap          // 用来在当前操作失败后返回上一个表单页面时携带提示消息
     * @return
     */
    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(ProjectVO projectVO,
                                       MultipartFile headerPicture,
                                       List<MultipartFile> detailPictureList, // 注意 ：接收干参数也是通过表单里的name属性值进行匹配接收的
                                       HttpSession session,
                                       ModelMap modelMap) throws IOException {

        // 完成头图的上传
        boolean headerPictureEmpty = headerPicture.isEmpty();

        if (headerPictureEmpty) {
            // 如果没有上传头图则返回到表单页面并显示错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }

        // 有内容的头图进行上传
        ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename());

        String result = resultEntity.getResult();

        // 是否上传成功
        if (ResultEntity.SUCCESS.equals(result)) {

            // 图片的访问路径
            String headerPicturePath = resultEntity.getData();

            //  存入 projectVO
            projectVO.setHeaderPicturePath(headerPicturePath);

        } else {
            // 上传失败
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);

            return "project-launch";

        }


        //  详情图片
        List<String> detailPicturePaths = new ArrayList<>(); // 存放详情图片的路径的集合

        // 集合为空
        if (detailPictureList == null || detailPictureList.size() == 0) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return "project-launch";
        }

        for (MultipartFile detailPicture : detailPictureList) {

            // 详情图片单个为空
            if (detailPicture.isEmpty()) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";
            }


            ResultEntity<String> detailPictureResultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicture.getOriginalFilename());

            String detailPictureResult = detailPictureResultEntity.getResult();

            if (ResultEntity.SUCCESS.equals(detailPictureResult)) {

                String detailPicturePath = detailPictureResultEntity.getData();

                detailPicturePaths.add(detailPicturePath);
            } else {

                // 上传失败
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);

                return "project-launch";
            }

        }

        // 详情图片路径 存入projectVO
        projectVO.setDetailPicturePathList(detailPicturePaths);

        // 将 projectVO 存入session
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

        // 下一个表单页面
        return "redirect:http://localhost/project/return/info/page.html";
    }


}
