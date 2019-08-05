<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="stylesheet" href="${APP_PATH }/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH }/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH }/css/main.css">
    <link rel="stylesheet" href="${APP_PATH }/css/doc.min.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
    </style>
</head>

<body>

<div id="top">
    <jsp:include page="/WEB-INF/jsp/common/top.jsp"></jsp:include>
</div>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <div class="tree">
                <jsp:include page="/WEB-INF/jsp/common/menu.jsp"></jsp:include>
            </div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">显示会员资质信息</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                    <div class="panel-body">
                        <form id="addForm">
                            <div class="form-group">
                                <label>会员真实姓名</label>
                                ${member.realname}
                            </div>
                            <div class="form-group">
                                <label>会员身份证号</label>
                                ${member.cardnum}
                            </div>
                            <div class="form-group">
                                <label>会员电话</label>
                                ${member.tel}
                            </div>

                            <hr>
                            <c:forEach items="${certimgs}" var="map">
                                <div class="form-group">
                                    <label>${map.name}</label> <BR>
                                    <img src="${APP_PATH}/pics/cert/${map.iconpath}">
                                </div>
                            </c:forEach>


                            <button id="passBtn" type="button" class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 通过</button>
                            <button id="refusetBtn" type="button" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 拒绝</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">帮助</h4>
            </div>
            <div class="modal-body">
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题1</h4>
                    <p>测试内容1，测试内容1，测试内容1，测试内容1，测试内容1，测试内容1</p>
                </div>
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题2</h4>
                    <p>测试内容2，测试内容2，测试内容2，测试内容2，测试内容2，测试内容2</p>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH }/script/docs.min.js"></script>
<script src="${APP_PATH }/jquery/layer/layer.js"></script>
<script src="${APP_PATH }/script/common.js"></script>
<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function(){
            if ( $(this).find("ul") ) {
                $(this).toggleClass("tree-closed");
                if ( $(this).hasClass("tree-closed") ) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });
        showMenu();
    });


    $("#passBtn").click(function(){

        $.ajax({
            type : "POST",
            url  : "${APP_PATH}/authcert/pass.do",
            data : {
                "taskid" : "${param.taskid}",
                "memberid" : "${param.memberid}"
            },
            success : function(result) {
                window.location.href = "${APP_PATH}/authcert/index.htm";
            }
        });
    });



    $("#refuseBtn").click(function(){
        $.ajax({
            type : "POST",
            url  : "${APP_PATH}/authcert/refuse.do",
            data : {
                "taskid" : "${param.taskid}",
                "memberid" : "${param.memberid}"
            },

            success : function(result) {
                window.location.href = "${APP_PATH}/authcert/index.htm";
            }
        });
    });


</script>
</body>
</html>
