<%@ page import="com.jeeplus.common.utils.PropertiesLoader" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>排队信息管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="controlQueueList.js" %>
	<script src="${pageContext.request.contextPath}/webpage/include/deviceControl.js"></script>
	<script src="${pageContext.request.contextPath}/webpage/include/device.js"></script>
	<%
		PropertiesLoader pro = new PropertiesLoader("/properties/config.properties");

		String http= "http://";
		String hostip=pro.getProperty("hostip")+"";
		String hostport=pro.getProperty("hostport")+"";
		String FtpPath=pro.getProperty("FtpPath")+"";
		String path=http+hostip+":"+hostport+FtpPath;
	%>
</head>

<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-body">

	<!-- 工具栏 -->
<%--	<div id="toolbar">--%>
<%--		<shiro:hasPermission name="controlqueue:controlQueue:handle">--%>
<%--			<button id="remove" class="btn btn-default" disabled onclick="deleteQueue()">--%>
<%--				<i class="fa fa-search-minus"></i> 删除--%>
<%--			</button>--%>
<%--		</shiro:hasPermission>--%>
<%--		</div>--%>
	<!-- 表格 -->
	<table id="controlQueueTable"   data-toolbar="#toolbar"></table>

	</div>
	</div>
	</div>
	<div>
		<tr>
			<td>
				<input id="path" value="<%=path%>" type="hidden">
			</td>
		</tr>
	</div>
</body>
</html>