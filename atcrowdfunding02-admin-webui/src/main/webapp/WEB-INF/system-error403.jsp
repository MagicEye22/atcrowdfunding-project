<%--
  Created by IntelliJ IDEA.
  User: Z
  Date: 2022/10/16
  Time: 19:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}">

    <script src="jquery/jquery-2.1.1.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script src="layer/layer.js"></script>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/login.css">
    <style>
    </style>

    <script type="text/javascript" >
        $(function (){
            $("button").click(function(){
                //相当于浏览器的后退按钮
                window.history.back();
            })
        })

    </script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container" style="text-align:center ">
    <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i>尚筹网系统消息</h2>
    <h3 >错误信息：${sessionScope.exception2.message}</h3>
    <button style="width: 150px;margin: 50px auto  0px auto" class="btn btn-lg btn-success btn-block">
        返回
    </button>
</div>

</body>
</html>

