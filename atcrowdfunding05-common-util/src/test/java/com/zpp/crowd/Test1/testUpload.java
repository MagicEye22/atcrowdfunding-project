package com.zpp.crowd.Test1;

import com.zpp.crowd.util.CrowdUtil;
import com.zpp.crowd.util.ResultEntity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author : Zpp
 * @Date : 2022/11/1-11:28
 */
public class testUpload {

    /*public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("E:\\idea-Workspace\\atcrowdfunding01-admin-parent\\atcrowdfunding05-common-util\\src\\R-C.jpg");

        ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOss("http://oss-cn-guangzhou.aliyuncs.com",
                "LTAI5tK1TTWo1f1LbbojPafF",
                "JRCMJkJBkwmNqKtYWp0vNGgeSZg4WL",
                fileInputStream,
                "crowd-project-zpp",
                "http://crowd-project-zpp.oss-cn-guangzhou.aliyuncs.com",
                "src/R-C.jpg");

        System.out.println(resultEntity);
    }*/
}
