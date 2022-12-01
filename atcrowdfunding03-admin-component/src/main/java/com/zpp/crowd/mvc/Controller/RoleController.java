package com.zpp.crowd.mvc.Controller;

import com.github.pagehelper.PageInfo;
import com.zpp.crowd.entity.Role;
import com.zpp.crowd.service.api.RoleService;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/9/6-23:20
 */
/*@Controller*/
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     *  角色维护 -分页
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     */
    //@ResponseBody
    //@PreAuthorize("hasRole('部长')")
    @RequestMapping("/role/get/page/info")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
            @RequestParam(value = "keyword",defaultValue = "")String keyword){

        // 调用service方法获取分页数据
        PageInfo<Role> pageInfo =  roleService.getPageInfo(pageNum,pageSize,keyword);

        // 封装到ResultEntity对象中返回（如果上面操作出现异常，交给异常映射机制处理）
        return ResultEntity.successWithoutData(pageInfo);
    }

    /**
     * 角色维护 -新增
     * @param role
     * @return
     */
    //@ResponseBody
    @RequestMapping("/role/save")
    public ResultEntity<String> saveRole(Role role){

        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    /**
     *  角色维护 - 更新
     * @param role
     * @return
     */
    //@ResponseBody
    @RequestMapping("/role/update")
    public  ResultEntity<String> updateRole(Role role){

        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    /**
     * 角色维护 -单条和批量删除
     * @param roleIdList
     * @return
     */
    //@ResponseBody
    @RequestMapping("/role/remove/id/List")
    public ResultEntity<String> removeByRoleIdArray(@RequestBody List<Integer> roleIdList){
        roleService.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
    }
}
