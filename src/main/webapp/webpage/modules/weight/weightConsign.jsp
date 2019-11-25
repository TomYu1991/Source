<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/weightTaglib.jsp"%>
<html>
<head>
    <title>车辆过磅</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>
    <%@include file="/webpage/include/treeview.jsp" %>
    <script src="${pageContext.request.contextPath}/webpage/include/deviceControl.js"></script>
    <script src="${pageContext.request.contextPath}/webpage/include/device.js"></script>
    <%@include file="weightConsign.js" %>
</head>
<body>

<div class="wrapper wrapper-content">


    <div class="panel panel-primary">

        <div style="text-align: center">
            <!-- 搜索 -->
            <div id="search" class="collapse">
                <div class="accordion-inner">
                    <form:form id="searchForm" modelAttribute="consign" class="form form-horizontal well clearfix">
                        <form:input path="consignNo" htmlEscape="false"   class=" form-control" id="SconsignNo"/>
                        <form:input path="vehicleNo" htmlEscape="false"   class=" form-control" id="SvehicleNo"/>
                    </form:form>
                </div>
            </div>
            <table class="ta" border="0"  style="width: 100%;border-collapse: collapse;height:200px">
                <tr>
                    <td class="td" align="center" style="border: 1px; width: 200px">
                      <span  style="font-size: 40px;"><input id="vehicleNo" disabled="true" type="text" style="width: 200px"></span>
                    </td>
                    <td class="td" align="center" id="showweight" style="border: 1px">
                        <input type="text" id = "weight" style="font-size: 60px;color:red;width: 250px;height: 80px;border-radius: 10px;margin-left: 100px">
                        </span><span style="font-size: 80px;margin-left: 10px">kg</span>
                    </td>
                    <td class="td" align="center">
                        <button id="dy"  class="btn btn-primary" onclick="sureweight()" style="float: right;margin-top: 30px;margin-right: 50px;width: 240px;height: 100px">
                            <span style="font-size: 40px">请求过磅</span>
                        </button>
                        <button id="sx"  class="btn btn-primary" onclick="clearall()" style="float: right;margin-top: 30px;margin-right: 50px;width: 240px;height: 100px">
                            <span style="font-size: 40px">刷新</span>
                        </button>
                    </td>
                </tr>
            </table>
            <div id='b'></div>
        </div>
        <div class="panel-heading" >
            <h3 class="panel-title">委托订单</h3>
        </div>
        <div class="panel-body" >
            <div id="loading" style="display:none;z-index:999;position: absolute;left:0px;width:100%">
                <img src="${ctxStatic}/common/images/loading.gif" style="width:100%"/>
            </div>
            <!-- 表格 -->
            <table id="consignTable" data-toolbar="#toolbar" ></table>

        </div>
    </div>


</div>
<div>
</div>
</body>
</html>