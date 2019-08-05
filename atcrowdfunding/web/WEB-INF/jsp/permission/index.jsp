<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <link rel="stylesheet" href="${APP_PATH }/ztree/zTreeStyle.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        table tbody tr:nth-child(odd){background:#F4F4F4;}
        table tbody td:nth-child(even){color:#C00;}
    </style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 许可管理</a></div>
        </div>

        <div id="navbar" class="navbar-collapse collapse">
            <jsp:include page="/WEB-INF/jsp/common/top.jsp"></jsp:include>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <div class="tree">
                <jsp:include page="/WEB-INF/jsp/common/menu.jsp"></jsp:include>
            </div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 许可树</h3>
                </div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree">
                        
                    </ul>
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
<script src="${APP_PATH }/ztree/jquery.ztree.all-3.5.min.js"></script>
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
       loadData();
    });

    var zNodes = [];
    var setting = {
            view : {
                addDiyDom : function (treeId,treeNode) {
                    var icoObj = $("#" + treeNode.tId + "_ico");
                    if (treeNode.icon){
                        icoObj.removeClass("button ico_docu ico_open").addClass(treeNode.icon).css("background","");
                    }
                },
                addHoverDom: function(treeId, treeNode){   //在节点后面，设置自定义按钮组
                    var aObj = $("#" + treeNode.tId + "_a"); // tId = permissionTree_1, ==> $("#permissionTree_1_a")
                    aObj.attr("href", "javascript:;");  //取消当前链接事件
                    if (treeNode.editNameFlag || $("#btnGroup"+treeNode.tId).length>0) return;
                    var s = '<span id="btnGroup'+treeNode.tId+'">';
                    if ( treeNode.level == 0 ) {    //根节点
                        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" onclick="window.location.href=\'${APP_PATH}/permission/toAdd.htm?id='+treeNode.id+'\'" >&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
                    } else if ( treeNode.level == 1 ) { //分支节点
                        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;"  href="#" title="修改权限信息" onclick="window.location.href=\'${APP_PATH}/permission/toUpdate.htm?id='+treeNode.id+'\'" >&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
                        if (treeNode.children.length == 0) {
                            s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#"  onclick="deletePermission('+treeNode.id+','+treeNode.name+')">&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
                        }
                        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" onclick="window.location.href=\'${APP_PATH}/permission/toAdd.htm?id='+treeNode.id+'\'" >&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
                    } else if ( treeNode.level == 2 ) { //叶子节点
                        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;"  href="#" title="修改权限信息" onclick="window.location.href=\'${APP_PATH}/permission/toUpdate.htm?id='+treeNode.id+'\'">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
                        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" onclick="deletePermission('+treeNode.id+','+treeNode.name+')">&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
                    }

                    s += '</span>';
                    aObj.after(s);
                },
                removeHoverDom: function(treeId, treeNode){
                    $("#btnGroup"+treeNode.tId).remove();
                }
            }
    };
    
    function  loadData() {


        $.ajax({
            type : "POST",
            url : "${APP_PATH}/permission/loadData.do",
            success : function (result) {
                if (result.success){
                    zNodes = result.data;
                    $.fn.zTree.init($("#treeDemo"),setting,zNodes);
                }else{
                    layer.msg("加载数据失败！",{time:1000,icon:5,shift:6});
                }
            }
        });
        
    }




    
    
    function deletePermission(id,name) {

        layer.confirm("确认删除["+name+"]许可吗？",{icon:3,title:'提示'},function (cIndex) {
            layer.close(cIndex);
            $.ajax({
                type : "POST",
                url : "${APP_PATH}/permission/deletePermission.do",
                data : {
                    "id" : id
                },
                success : function (result) {
                    if (result.success){
                        zNodes = result.data;
                        $.fn.zTree.init($("#treeDemo"),setting,zNodes);
                        loadData();
                    }else{
                        layer.msg("删除许可数据失败！",{time:1000,icon:5,shift:6});
                    }
                }
            });
        },function (cIndex) {
            layer.close(cIndex);
        });




    }



</script>
</body>
</html>
    
