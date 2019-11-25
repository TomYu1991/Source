<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备点检结果详情</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<style>
		td{width:125px}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {

		});
	</script>

</head>
<body>
<h3 align="center">宝钢德盛不锈钢有限公司<br>
	设备岗位点检记录表</h3>
<table class="table table-bordered" align="center" style="width:90%;margin:auto">
	<div style="text-align: center">
		<label>班组：</label><u>${deviceCheckRecord.workingGroup}</u>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<label>班组长：</label><u>${deviceCheckRecord.groupLeader}</u>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<label>点检人员：</label><u>${deviceCheckRecord.groupWorker}</u>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<label>点检区域：</label><u>${deviceCheckRecord.workingArea}</u>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<fmt:formatDate value="${deviceCheckRecord.updateDate}" var="date" pattern="yyyy-MM-dd HH:mm:ss"/>
		<label>点检时间：</label><u>${date}</u>
	</div>
	<tr>
		<td>序号</td>
		<td>设备名称</td>
		<td>项目</td>
		<td>点检内容</td>
		<td>点检标准描述</td>
		<td>点检方法</td>
		<td>点检结果</td>
		<td>设备状态</td>
		<td>备注</td>
	</tr>

		<c:forEach items="${deviceCheckRitemList}" var="item" varStatus="status">
			<input type="hidden" name="id" value="${item.id}"/>
			<tr>
				<td>${status.count}</td>
				<td>${item.deviceName}</td>
				<td>${item.checkItem}</td>
				<td>${item.checkContent}</td>
				<td>${item.checkReference}</td>
				<td>${item.checkMethod}</td>
				<c:if test="${item.checkResult=='0'}">
					<td>正常</td>
				</c:if>
				<c:if test="${item.checkResult=='1'}">
					<td>异常</td>
				</c:if>
<%--				<td>${item.checkResult}</td>--%>
				<td>${item.deviceState}</td>
				<td>${item.remarks}</td>
			</tr>
		</c:forEach>

</table>
<hr>
<div align="left" style="width:90%;margin:auto">
			<strong>说明：</strong><br/>
			<p>1、在岗人员检每日下班前，依规定执行保养，若保养时为正常状况时，选择“无异常”；否则选择“有异常”，在备注栏内注明异常现象并通知相关人员处理。</p>
			<p>2、班组长每周不定时检查（至少两次）保养作业的执行情况。</p>
			<p>3、设备检修未使用时，在点检结果记为‘休’字。</p>
		</div>
</div>
</div>
</body>
</html>