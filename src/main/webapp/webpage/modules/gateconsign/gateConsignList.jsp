<%@ page import="com.jeeplus.common.utils.PropertiesLoader" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>门岗管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@include file="gateConsignList.js" %>
    <script src="${pageContext.request.contextPath}/webpage/include/device.js"></script>
    <script src="${pageContext.request.contextPath}/webpage/include/deviceControl.js"></script>


</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">委托单/预约单信息列表</h3>
        </div>
        <div class="panel-body">

            <!-- 搜索 -->
            <div id="search-collapse" class="collapse">
                <div class="accordion-inner">
                    <form:form id="searchForm" modelAttribute="consign" class="form form-horizontal well clearfix">
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="车牌号：">车牌号：</label>
                            <form:input path="vehicleNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="身份证号：">身份证号：</label>
                            <form:input path="IDCard" htmlEscape="false" maxlength="20"  class=" form-control"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="类型：">类型：</label>
                            <form:select path="type" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('consign_type')}" itemLabel="label"
                                              itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="委托/预约单号：">委托/预约单号：</label>
                            <form:input path="consignId" htmlEscape="false" maxlength="20" class=" form-control"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="委托单状态：">委托单状态：</label>
                            <form:select path="status"  class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('consign_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <div class="form-group">
                                <label class="label-item single-overflow pull-left" title="创建时间：">&nbsp;创建时间：</label>
                                <div class="col-xs-12">
                                    <div class="col-xs-12 col-sm-5">
                                        <div class='input-group date' id='startTime' style="left: -10px;" >
                                            <input type='text'  name="startTime" class="form-control"  />
                                            <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-1">
                                        ~
                                    </div>
                                    <div class="col-xs-12 col-sm-5">
                                        <div class='input-group date' id='endTime' style="left: -10px;" >
                                            <input type='text'  name="endTime" class="form-control" />
                                            <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <form:hidden path="searchFlag" htmlEscape="false" class=" form-control" id="searchFlag" />
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <div style="margin-top:26px">
                                <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                        class="fa fa-search"></i> 查询</a>
                                <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                        class="fa fa-refresh"></i> 重置</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>

            <!-- 工具栏 -->
            <div id="toolbar">

               <%-- <shiro:hasPermission name="gateconsign:consign:open">
                    <select class="btn btn-default" id="gateNum" onchange="changestation()">
                        <option value="" label="请选择岗亭"></option>
                        <c:forEach items="${list}" var="l">
                            <option value="${l.id}" label="${l.workStation}"></option>
                        </c:forEach>
                    </select>
                    <select class="btn btn-default" id="dz" >
                    </select>
                    <button id="openGate" class="btn btn-success" disabled onclick="openGate()">
                        开启
                    </button>
                </shiro:hasPermission>
                <shiro:hasPermission name="gateconsign:consign:open">
                    <button id="closeGate" class="btn btn-danger" disabled onclick="closeGate()">
                        关闭
                    </button>
                </shiro:hasPermission>--%>
                <shiro:hasPermission name="gateconsign:consign:checkConsign">
                    <button id="readCard" class="btn btn-info"  onclick="openCard()">
                    刷卡
                    </button>
                    <%--<button id="ffqysbcp" class="btn btn-success" onclick="ffqysbcp()">
                    <i class="glyphicon glyphicon-edit"></i>非法区域识别车牌--%>
                </button>
                    <button id="bcryxx" class="btn btn-success" onclick="bcryxx()">
                        <i class="glyphicon glyphicon-edit"></i>更新人员信息
                    </button>
                </shiro:hasPermission>
                <shiro:hasPermission name="gateconsign:consign:export">
                    <button id="export" class="btn btn-warning">
                        <i class="fa fa-file-excel-o"></i> 导出
                    </button>
                </shiro:hasPermission>
              <%-- <shiro:hasPermission name="gateconsign:consign:LEDsend">
                   <input type="text" id="LEDcontent" class="btn btn-default" placeholder="在此输入LED展示内容">
                   <button id="LEDsend" class="btn btn-success" onclick="sendLED()">
                       LED发送
                   </button>
               </shiro:hasPermission>--%>
                <%--<shiro:hasPermission name="gateconsign:consign:import">
                    <button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
                </shiro:hasPermission>--%>
            </div>

            <!-- 表格 -->
            <table id="consignTable" data-toolbar="#toolbar"></table>

            <!-- context menu -->
            <ul id="context-menu" class="dropdown-menu">
                <shiro:hasPermission name="gateconsign:consign:view">
                    <li data-item="view"><a>查看</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="gateconsign:consign:edit">
                    <li data-item="edit"><a>编辑</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="gateconsign:consign:del">
                    <li data-item="delete"><a>删除</a></li>
                </shiro:hasPermission>
                <li data-item="action1"><a>取消</a></li>
            </ul>
        </div>
    </div>
</div>
<script >
    /*//滚动条
    $(function(){
        var divel =  $("<div style='display:inline-block;position:fixed;bottom:0;left:0;width:100%;height:20px;background:#fff;overflow:auto;' ><div style='display:inline-block;height:30px;'></div></div>").appendTo("body");

        $(".fixed-table-body").css("overflow-x","hidden");
        $(divel).find(">div").width( $(".fixed-table-body>table" ).width());
        $(divel).scroll(function(){
            $(".fixed-table-body").scrollLeft($(this).scrollLeft());
        });
        $(".fixed-table-body").resize(function(){
            $(divel).find(">div").width( $(".fixed-table-body>table" ).width());
        });
    })*/
    //滚动条
    $(function(){
        var divel =  $("<div style='display:inline-block;position:fixed;bottom:0;left:0;width:100%;height:20px;background:#fff;overflow:auto;' ><div style='display:inline-block;height:30px;'></div></div>").appendTo("body");

        $(".fixed-table-body").css("overflow-x","hidden");
        $(divel).find(">div").width( $(".fixed-table-body>table" ).width() + $(".fixed-table-body").width());
        $(divel).scroll(function(){
            $(".fixed-table-body").scrollLeft($(this).scrollLeft());
        });
        $(".fixed-table-body").resize(function(){
            $(divel).find(">div").width( $(".fixed-table-body>table" ).width() + $(".fixed-table-body").width());
        });
    })


</script>
</body>
</html>