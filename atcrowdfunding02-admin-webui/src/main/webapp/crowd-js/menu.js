function generateTree(){
    // 准备生成树形结构的json数据，数据的来源是发送ajax请求返回的json数组
    $.ajax({
        "url":"/atcrowdfunding02_admin_webui/menu/get/whole/tree",
        "type":"post",
        "dataType":"json",
        "success":function (resp){
            var result=resp.result
            if (result=="SUCCESS"){
                // 从响应体中获取生成树形结构的json数据
                var zNodes = resp.data
                // 创建一个json对象，用于存储对zTree的设置
                var setting ={
                    "view":{
                        "addDiyDom": myAddDiyDom,
                        "addHoverDom":myAddHoverDom,
                        "removeHoverDom":myRemoveHoverDom
                    },
                    "data":{
                        "key":{
                            "url":"testUrl"
                        }
                    }
                }
                // 初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes)
            }

            if (result=="FAILED"){
                layer.msg("请求失败！"+resp.message)
            }
        },
        "error":function (resp){
            layer.msg("失败！statusCode:" + resp.status + ",errorText:" + resp.statusText);
        }

    })
}



function myAddDiyDom(treeId, treeNode) {

    // zTree 生成 id 的规则
    // 例子：treeDemo_7_ico
    // 解析：ul 标签的 id_当前节点的序号_功能
    // 提示：“ul 标签的 id_当前节点的序号”部分可以通过访问 treeNode 的 tId 属性得到
    // 根据 id 的生成规则拼接出来 span 标签的
    var spanId = treeNode.tId + "_ico";

    // 根据控制图标的 span 标签的 id 找到这个 span 标签
    // 删除旧的 class
    // 添加新的 class
    $("#" + spanId).removeClass().addClass(treeNode.icon)

}

// 在鼠标移出节点范围时删除按钮组
function myRemoveHoverDom(treeId, treeNode) {
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 移除
    $("#" + btnGroupId).remove();
}

// 在鼠标移入节点范围时添加按钮组
function myAddHoverDom(treeId, treeNode) {
    // 按钮组的标签结构：<span><a><i></i></a><a><i></i></a></span>
    // 按钮组出现的位置：节点中 treeDemo_n_a 超链接的后面
    // 为了在需要移除按钮组的时候能够精确定位到按钮组所在 span，需要给 span 设置有规律的 id
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 判断之前是否已经添加了按钮组
    if ($("#" + btnGroupId).length > 0) {
        return;
    }


    // 准备各个按钮的 HTML 标签
    var addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs'  style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn ="<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title=' 删 除 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg'></i></a>";
    var editBtn ="<a id='" + treeNode.id + "' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title=' 修 改 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";

    // 获取当前节点的级别数据
    var level = treeNode.level;

    // 存储拼装好的按钮的代码
    var btnHTML = "";

    // 判断当前节点的等级
    if (level == 0) {
        // 级别为0时是根节点，只能添加子节点
        btnHTML = btnHTML + addBtn
    }
    if (level == 1) {
        // 级别为1时是分支节点，只能添加和修改子节点
        btnHTML = addBtn + " " + editBtn

        // 获取当前节点的子节点数量
        var length = treeNode.children.length;

        // 如果没有子节点，可以删除
        if (length == 0) {
            btnHTML = btnHTML + " " + removeBtn
        }
    }

    if (level == 2) {
        // 级别为2时是叶子节点，只能删除和修改子节点
        btnHTML = editBtn + " " + removeBtn
    }

    var anchorId = treeNode.tId + "_a"

    // 执行在超链接后面附加的span元素的操作
    $("#" + anchorId).after("<span id='" + btnGroupId + "'>" + btnHTML + "</span>")

}

