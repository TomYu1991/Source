<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备点检子项配置</title>
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
		function save() {
			var isValidate = jp.validateForm('#inputForm');//校验表单
			if(!isValidate){
				return false;
			}else{
				jp.loading();
				jp.post("${ctx}/check/deviceCheckRitem/save",$('#inputForm').serialize(),function(data){
					if(data.success){
						jp.getParent().refresh();
						var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
						parent.layer.close(dialogIndex);
						jp.success(data.msg)

					}else{
						jp.error(data.msg);
					}
				})
			}

		}
	</script>

</head>
<body>
<h3 align="center">宝钢德盛不锈钢有限公司<br>
	设备岗位点检记录表</h3>
<table class="table table-bordered" align="center" style="width:90%;margin:auto">


	<tr>
		<td>序号</td>
		<td>设备名称</td>
		<td>项目</td>
		<td>点检内容</td>
		<td>点检标准描述</td>
		<td>点检方法</td>
		<td>设备状态</td>
		<td>点检结果</td>
		<td>备注</td>
	</tr>
	<form action="${ctx}/check/deviceCheckRitem/data" method="post" id="inputForm">
		<div style="text-align: center">
			<label>班组：</label>
<%--			<form:option value="" label=""/>--%>
			<select name="workingGroup">
				<option value="_">---请选择---</option>
				<option value="甲班组">甲班组</option>
				<option value="乙班组">乙班组</option>
				<option value="丙班组">丙班组</option>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>班组长：</label>
			<select name="groupLeader">
				<option value="_">---请选择---</option>
				<option value="王小燕">王小燕</option>
				<option value="顾玲玲">顾玲玲</option>
				<option value="石智勇">石智勇</option>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>点检人员：</label><u>${fns:getUser().name}</u>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>点检区域：</label><u>${deviceCheckRecord.workingArea}</u>
		</div>
		<input type="hidden" name="recordId" value="${deviceCheckRecord.id}">
        <input type="hidden" name="groupWorker" value="${fns:getUser().name}"/>
		<c:forEach items="${deviceCheckRitemList}" var="item" varStatus="status">
			<input type="hidden" name="id" value="${item.id}"/>
			<tr>
				<td>${status.count}</td>
				<td>${item.deviceName}</td>
				<td>${item.checkItem}</td>
				<td>${item.checkContent}</td>
				<td>${item.checkReference}</td>
				<td>${item.checkMethod}</td>
				<td>${item.deviceState}</td>
				<td>
<%--					<input type="text" name="${item.id}result"/>--%>
					<select name="${item.id}result">
						<option value="1">有异常</option>
						<option value="0" selected="selected">无异常</option>
					</select>
				</td>
				<td><input type="text" name="${item.id}remarks" width="255"/></td>
			</tr>

		</c:forEach>

	</form>

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