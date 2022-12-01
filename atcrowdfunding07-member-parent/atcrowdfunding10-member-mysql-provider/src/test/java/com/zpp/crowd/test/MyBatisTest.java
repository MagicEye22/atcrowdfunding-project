package com.zpp.crowd.test;

import com.zpp.crowd.entity.po.MemberPO;
import com.zpp.crowd.entity.vo.DetailProjectVO;
import com.zpp.crowd.entity.vo.PortalTypeVO;
import com.zpp.crowd.mapper.MemberPOMapper;
import com.zpp.crowd.mapper.ProjectPOMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/10/24-21:32
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class MyBatisTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Test
    public void test3(){
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(6);

        System.out.println(detailProjectVO);
    }


    @Test
    public void test2(){
        List<PortalTypeVO> portalTypeVOS = projectPOMapper.selectPortalTypeVOList();

        portalTypeVOS.forEach(System.out::println);
    }

    @Test
    public void testMapper() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source = "123123";
        String encode = passwordEncoder.encode(source);
        MemberPO memberPO = new MemberPO(null, "root", encode, " 管理员", "root@qq.com", 1, 1, "管理员", "123123", 2);
        memberPOMapper.insert(memberPO);
        //log.info("mapper:{}" ,memberPOMapper);
    }
    @Test
    public void test() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("data:{}",connection);
    }

}
