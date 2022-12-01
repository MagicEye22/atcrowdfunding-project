package com.zpp.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.entity.Admin;
import com.zpp.crowd.entity.AdminExample;
import com.zpp.crowd.exception.LoginAcctAlreadyInForUpdateException;
import com.zpp.crowd.exception.LoginAcctAlreadyInUseException;
import com.zpp.crowd.exception.LoginFailedException;
import com.zpp.crowd.mapper.AdminMapper;
import com.zpp.crowd.service.api.AdminService;
import com.zpp.crowd.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author : Zpp
 * @Date : 2022/8/30-16:04
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /**
     *  SpringSecurity 的 UserDetailsService 需要的用户信息 密码和账号
     * @param username 登录的账号 loginAcct
     * @return
     */
    public Admin getAdminByLoginAcct(String username) {
        AdminExample adminExample = new AdminExample();

        AdminExample.Criteria criteria = adminExample.createCriteria();

        criteria.andLoginAcctEqualTo(username);

        List<Admin> admins = adminMapper.selectByExample(adminExample);

        return admins.get(0);
    }


    /**
     * 管理员-添加方法
     *
     * @param admin
     */
    @Override
    public void saveAdmin(Admin admin) {

        // 密码加密
        //String md5Password = CrowdUtil.MD5(admin.getUserPswd());
        String encodePassword = bCryptPasswordEncoder.encode(admin.getUserPswd());
        admin.setUserPswd(encodePassword);

        // 生成创建时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        admin.setCreateTime(format);

        // 执行保存
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }

    }

    /**
     * 测试 查询所有
     *
     * @return
     */
    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    /**
     * 登录验证
     *
     * @param loginAcct 用户账号
     * @param userPswd  用户密码
     * @return
     */
    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        // 1.先要根据登录账号查询Admin对象

        // ①创建 AdminExample 对象
        AdminExample adminExample = new AdminExample();

        // ②创建 Criteria 对象
        AdminExample.Criteria criteria = adminExample.createCriteria();

        // ③在 Criteria 对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);

        // ④调用 AdminMapper 的方法执行查询
        List<Admin> admins = adminMapper.selectByExample(adminExample);

        // 2.如果admin对象是否为空
        if (admins.size() == 0 || admins == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if (admins.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        Admin admin = admins.get(0);


        // 3.如果admin对象为null则抛出异常
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 4.如果admin对象不为null则将数据库密码从admin对象中取出
        String userPswdDB = admin.getUserPswd();

        // 5.将表单提交的明文密码进行加密
        String userPswdForm = CrowdUtil.MD5(userPswd);

        // 6.对密码进行比较
        if (!Objects.equals(userPswdDB, userPswdForm)) {

            // 7.如果比较结果还是不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 8.如果一致返回admin对象
        return admin;
    }

    /**
     * 分页功能
     *
     * @param keyword  查询的参数
     * @param pageNum  当前页数
     * @param pageSize 每页的显示个数
     * @return PageInfo
     */
    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        // 1.调用PageHelper的静态方法开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 2.执行查询
        List<Admin> admins = adminMapper.selectAdminByKeyword(keyword);

        // 3.封装PageInfo对象中
        return new PageInfo<>(admins);
    }

    /**
     * 管理员维护-删除
     *
     * @param adminId
     */
    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    /**
     * 管理员维护-更新.1-获取对应id的admin对象
     *
     * @param adminId
     * @return
     */
    @Override
    public Admin getAdminById(Integer adminId) {
        Admin admin = adminMapper.selectByPrimaryKey(adminId);

        return admin;
    }

    /**
     * 管理员维护-更新.2-执行修改
     *
     * @param admin
     */
    @Override
    public void update(Admin admin) {


        try {
            //根据主键 有选择的更新： 字段为null的不更新
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    /**
     * 管理员维护-批量删除
     *
     * @param adminIdList
     */
    @Override
    public void batchRemove(List<Integer> adminIdList) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andIdIn(adminIdList);
        adminMapper.deleteByExample(adminExample);
    }

    /**
     * 权限分配-角色分配-执行分配
     *
     * @param adminId
     * @param roleIdList
     */
    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 为了简化操作：先根据 adminId 删除旧的数据，再根据 roleIdList 保存全部新的

        // 1.根据 adminId 删除旧的关联关系数据
        adminMapper.deleteOldRelationship(adminId);

        // 2.根据 roleIdList 和 adminId 保存新的关联关
        if (roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }
}
