package com.zpp.crowd.mvc.Controller;

import com.zpp.crowd.entity.Auth;
import com.zpp.crowd.entity.Role;
import com.zpp.crowd.service.api.AdminService;
import com.zpp.crowd.service.api.AuthService;
import com.zpp.crowd.service.api.RoleService;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author : Zpp
 * @Date : 2022/9/12-19:29
 */
@Controller
public class AssignController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    /**
     * 权限分配-权限分配-执行分配
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/assgin/do/role/assgin/auth")
    public ResultEntity<String> saveRoleAuthRelationship(@RequestBody Map<String,List<Integer>> map ){

        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }


    /**
     * 权限分配-权限分配-根据roleId查询对应authId的集合
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/assgin/get/auth/id")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(@RequestParam("roleId")Integer roleId){

        List<Integer> authIdList = authService.getAssignedAuthIdByRoleId(roleId);

        return ResultEntity.successWithoutData(authIdList);
    }


    /**
     * 权限分配-权限分配-查询auth所有数据生成zTree树结构
     * @return
     */
    @ResponseBody
    @RequestMapping("/assgin/get/all/auth")
    public ResultEntity<List<Auth>> getAuthAll(){
        List<Auth> authList=authService.getAll();

        return ResultEntity.successWithoutData(authList);
    }


    /**
     * 权限分配-角色分配-执行分配
     * @param adminId
     * @param pageNum
     * @param keyword
     * @param roleIdList
     * @return
     */
    @RequestMapping("/assign/do/role/assign")
    public String saveAdminRoleRelationship(@RequestParam("adminId")Integer adminId,
                                   @RequestParam("pageNum")Integer pageNum,
                                   @RequestParam("keyword")String keyword,
                                   //允许用户在页面上取消所有已分配角色再提交表单，此时表单为空
                                   //设置required = false表示这个参数是可有可无的
                                   @RequestParam(value = "roleIdList" ,required = false)List<Integer> roleIdList){

        adminService.saveAdminRoleRelationship(adminId,roleIdList);

        return "redirect:/admin/get/page?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    /**
     * 权限分配-角色分配-前往分配页面
      * @param adminId
     * @param modelMap
     * @return
     */
    @RequestMapping("/assign/to/role")
    public String toAssignRole(@RequestParam("adminId")Integer adminId, ModelMap modelMap){
            // 查询已分配的角色
            List<Role> AssignedRoleList=roleService.getAssignedRole(adminId);
            // 查询为分配的角色
            List<Role> unAssignRoleList=roleService.getUnAssignedRole(adminId);
            // 存入模型
            modelMap.addAttribute("AssignedRoleList",AssignedRoleList);
            modelMap.addAttribute("unAssignRoleList",unAssignRoleList);
        return "admin/assign-role";
    }
}
