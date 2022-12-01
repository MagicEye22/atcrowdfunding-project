package com.zpp.crowd.test;

import com.zpp.crowd.entity.Admin;
import com.zpp.crowd.entity.Role;
import com.zpp.crowd.mapper.AdminMapper;
import com.zpp.crowd.mapper.RoleMapper;
import com.zpp.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : Zpp
 * @Date : 2022/8/29-23:48
 */

//在类上标记必要的注解，Spring和Junit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private  AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testRoleSave(){

        for (int i = 0; i <235 ; i++) {
            roleMapper.insert(new Role(null,"role"+i));
        }
    }
/*

    @Test
    public  void  testTX(){
        adminService.saveAdmin(new Admin(null,"jerry2","12312","杰瑞2","jerry2@qq.com",null));
    }

*/

    /*@Test
    public void testMapperScanner(){
        for (int i = 0; i <238 ; i++) {
            adminMapper.insert(new Admin(null,"test"+i,"12312"+i,"测试"+i,"test"+i+"@qq.com",null));
        }
    }*/

    @Test
    public void testLog(){
        //获取Logger对象,传入的Class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        //根据不同日志级别打印日志
        logger.debug("debug");
        logger.debug("debug");
        logger.debug("debug");
        logger.info("info");
        logger.info("info");
        logger.info("info");
        logger.warn("warn");
        logger.warn("warn");
        logger.warn("warn");
        logger.error("error");
        logger.error("error");
    }

    @Test
    public  void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        //如果在实际开发中，所有想查看数值的地方都使用sysout方式打印，会给项目上线运行带来问题
        //sysout本质上是一个IO操作，通常IO操作是比较消耗性能的，如果项目中的sysout很多，那么对性能的影响就比较大了
        //而如果使用日志系统，那么通过日志级别就可以批量的控制信息的打印
        System.out.println(connection);
    }
}
