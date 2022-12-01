function fillAuthTree() {
    // 发送Ajax请求查询Auth数据
    var ajaxReturn =$.ajax({
        "url": "/atcrowdfunding02_admin_webui/assgin/get/all/auth",
        "type": "post",
        "dataType": "json",
        "async":false
    })
    if (ajaxReturn.status!=200){
        layer.msg("请求处理出错！statusCode："+ajaxReturn.status)
        return;
    }
    // 响应结果中获取Auth数据
    var authList = ajaxReturn.responseJSON.data

    // zTree简单JSON数据结构
    var setting = {
        "data": {
            "simpleData": {
                // 开启简单json
                "enable": true,
                "pIdKey":"categoryId"//使用categoryId关联父节点
            },
            "key":{
                "name":"title"//使用title属性显示节点名称
            }
        },
        "check":{
            "enable":true// 显示复选框
        }

    }
    //生成zTree
    $.fn.zTree.init($("#authTreeDemo"),setting,authList)

    // 展开树形结构
    var zTreeObj=$.fn.zTree.getZTreeObj("authTreeDemo");
    zTreeObj.expandAll(true)

    // 查询已分配的auth的id组成的list
     ajaxReturn =$.ajax({
         "url":"/atcrowdfunding02_admin_webui/assgin/get/auth/id",
         "type":"post",
         "data":{
             "roleId":window.roleId
         },
         "async":false,
         "dataType":"json"
     })

    if (ajaxReturn.status!=200){
        layer.msg("请求处理出错！statusCode："+ajaxReturn.status)
        return;
    }

    // 响应结果中获取authIdList
    var authIdList=ajaxReturn.responseJSON.data;

    // 根据authIdArray把树形结构中对应的节点勾选

    // 遍历authIdList
    for (var i = 0; i <authIdList.length ; i++) {
        var authId=authIdList[i];
        // 根据id查询树形结构中对应的节点
        var treeNode=zTreeObj.getNodeByParam("id",authId)

        // 将treeNode设置为被勾选
        // 第二个参数设置为true表示节点勾选
        // 第三个参数设置为false表示不联动，为了避免勾选多余数据
        zTreeObj.checkNode(treeNode,true,false)
    }


}


// 删除-确认模态框
function showConfirmModal(roleArray) {

    // 清除旧数据
    $("#roleNameSpan").empty();

    // 创建全局变量数组存放roleId
    window.roleIdArray = []

    //打开模态框
    $("#confirmModal").modal("show");

    // 遍历roleArray数组
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];

        var roleName = role.roleName;

        $("#roleNameSpan").append(roleName + "<br/>");

        window.roleIdArray.push(role.roleId)
    }
}


function generatePage() {// 执行分页，生成页面效果,任何时候调用这个函数都会重新加载页面

    // 1.获取分页数据
    var pageInfo = getPageInfoRemote();

    // 2.填充表格
    fillTableBody(pageInfo)


}

function getPageInfoRemote() {// 远程访问服务器端，获取pageInfo数据
    var ajaxResult = $.ajax({
        "url": "/atcrowdfunding02_admin_webui/role/get/page/info",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "dataType": "json",
        "async": false
    })

    /* console.log(ajaxResult)*/
    // 判断当前响应状态码是否为200
    var statusCode = ajaxResult.status;
    if (statusCode != 200) {
        layer.msg("失败！响应状态码:" + statusCode + ",说明信息:" + ajaxResult.statusText);
        return null;
    }

    // 如果响应状态码为200 说明请求处理成功，获取pageInfo
    var resultEntity = ajaxResult.responseJSON;

    // 从resultEntity中获取result属性
    var result = resultEntity.result;

    if (result == "FAILED") {//后台程序出现异常的处理
        layer.msg(resultEntity.message)
        return null;
    }

    // 返回的是success后，获取pageInfo
    var pageInfo = resultEntity.data;

    // 返回pageInfo
    return pageInfo;

}

function fillTableBody(pageInfo) {// 填充表格

    // 清除tbody中的旧的内容
    $("#rolePageBody").empty();
    $("#Pagination").empty();

    // 判断pageInfo是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list.length == 0 || pageInfo.list == null) {
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！没有查询到您要的数据！</td></tr>")
        return;
    }

    // 使用 pageInfo 的 list 属性填充 tbody
    for (var i = 0; i < pageInfo.list.length; i++) {

        var role = pageInfo.list[i];

        var roleId = role.id;

        var roleName = role.name;

        var numberTd = "<td>" + (i + 1) + "</td>";

        var checkboxTd = "<td><input id='" + roleId + "' class='itemBox' type='checkbox'></td>";

        var roleNameTd = "<td>" + roleName + "</td>";

        var checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class='glyphicon glyphicon-check'></i></button>";

        // 通过 button 标签的 id 属性（别的属性其实也可以）把 roleId 值传递到 button 按钮的单击
        var pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class='glyphicon glyphicon-pencil'></i></button>";

        // 与更新一致
        var removeBtn = "<button id='" + roleId + "' type='button' class='btn btn-danger btn-xs removeBtn'><i class='glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
        var tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";
        $("#rolePageBody").append(tr);
    }
    // 生成分页导航条
    generateNavigator(pageInfo);

}

function generateNavigator(pageInfo) {// 生成分页页码导航条
    // 获取总记录条数
    var totalRecord = pageInfo.total;

    // 声明一个JSON对象存储Pagination要存储的属性
    var properties = {
        num_edge_entries: 3, // 边缘页数
        num_display_entries: 5, // 主体页数
        callback: paginationCallBack, // 指定用户点击 "翻页"的按钮时跳转页面的回调函数
        items_per_page: pageInfo.pageSize, // 每页要显示的数据的数量
        current_page: pageInfo.pageNum - 1, // pagination内部使用pageIndex来管理页码，pageIndex从0开始，pageNum从1开始，所以要 -1
        prev_text: "上一页", // 上一页按钮上显示的文本
        next_text: "下一页"  // 下一页按钮上显示的文本
    };

    // 调用pagination()生成页码导航条
    $("#Pagination").pagination(totalRecord, properties)
}

function paginationCallBack(pageIndex, jQuery) {// 翻页时的回调函数

    // 修改window对象的pageNum属性
    window.pageNum = pageIndex + 1;

    // 调用分页函数
    generatePage();


    // 取消页码超链接默认行为
    return false;
}

