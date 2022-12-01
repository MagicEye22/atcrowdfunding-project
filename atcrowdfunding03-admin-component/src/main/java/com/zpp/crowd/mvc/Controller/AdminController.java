package com.zpp.crowd.mvc.Controller;

import com.github.pagehelper.PageInfo;
import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.entity.Admin;
import com.zpp.crowd.service.api.AdminService;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author : Zpp
 * @Date : 2022/9/3-15:37
 */
@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录-登录验证
     *
     * @param loginAcct
     * @param userPswd
     * @param session
     * @return
     */
    @Deprecated //该方法无效
    @RequestMapping("/admin/do/login")
    public String doLogin(@RequestParam("loginAcct") String loginAcct, @RequestParam("userPass") String userPswd, HttpSession session) {

        //调用service方法进行登录检查
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
        //将成功返回的admin对象存入session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        return "redirect:/admin/to/main";//重定向目标视图
    }

    /**
     * 管理员登录-退出系统
     *
     * @param session
     * @return
     */
    @Deprecated //该方法无效
    @RequestMapping("/admin/do/logout")
    public String doLogout(HttpSession session) {
        //强制session失效
        session.invalidate();
        return "redirect:/admin";//重定向回登录页面 view-controller
    }

    /**
     * 管理员维护-分页
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    //@PreAuthorize("hasAnyRole('经理') or hasAuthority('user:get')")
    @RequestMapping("/admin/get/page")
    public String getPageInfo(
            //使用defaultValue 属性，指定默认值，在请求中没有携带对应参数时使用默认值
            //keyword 默认值使用空字符串，和sql语句配合实现两种情况适配
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            Model model) {

        // 调用service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

        // 将pageInfo对象存入模型
        model.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);

        return "admin/admin-page";
    }

    /**
     * 管理员维护-单条删除
     *
     * @param adminId 管理员用户id
     * @param pageNum 当前页码
     * @param keyword 查询参数
     * @return 删除的当前页视图
     */
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable("adminId") Integer adminId, @PathVariable("pageNum") Integer pageNum, @PathVariable("keyword") String keyword) {
        // 执行删除
        adminService.remove(adminId);


        // 页面的跳转
        // 方案一：直接转发到 admin-page.html 会无法显示分页数据
        // return "admin/admin-page";
        // 方案二：转发到/admin/get/page 地址，一旦刷新页面会重复执行删除浪费性能
        // return "forward:/admin/get/page";
        // 方案三：重定向到/admin/get/page 地址
        // 同时为了保持原本所在的页面和查询关键词再附加 pageNum 和 keyword 两个请求参数
        return "redirect:/admin/get/page?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    /**
     * 管理员维护-新增
     *
     * @param admin
     * @return
     */
   // @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save")
    public String save(Admin admin) {

        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page?pageNum=" + Integer.MAX_VALUE;
    }

    /**
     * 管理员维护-更新.1-获取对应id的admin对象
     *
     * @param adminId
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/edit")
    public String toEditPage(@RequestParam("adminId") Integer adminId, ModelMap modelMap) {

        // 根据id查询admin对象
        Admin admin = adminService.getAdminById(adminId);
        // 将查询到的admin对象存入模型
        modelMap.addAttribute("admin", admin);
        return "admin/admin-edit";
    }

    /**
     * 管理员维护-更新.2-执行修改
     *
     * @param admin
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping("/admin/update")
    public String update(Admin admin, @RequestParam("pageNum") Integer pageNum, @RequestParam("keyword") String keyword) {

        adminService.update(admin);

        return "redirect:/admin/get/page?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    /**
     * 批量删除
     *
     * @param adminIdList
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping("/admin/remove/List")
    public String adminRemoveList(@RequestParam("adminIdList") Integer[] adminIdList, @RequestParam("pageNum") Integer pageNum, @RequestParam("keyword") String keyword) {
        List<Integer> adminIdToList = Arrays.asList(adminIdList);
        adminService.batchRemove(adminIdToList);
        return "redirect:/admin/get/page?pageNum=" + pageNum + "&keyword=" + keyword;
    }
}
