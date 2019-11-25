<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>

	<title>今日点检报表</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<style>
		td {
			width: 125px;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {

		});
	</script>

</head>
<body>
<h3 align="center">宝钢德盛不锈钢有限公司<br>
	设备岗位点检报表</h3>
<hr>


<c:if test="${yhb.size() gt 0}">
	<table class="table table-bordered" align="center" style="width:90%;margin:auto">
		<div style="text-align:center">
			<label>点检人员：</label><u>${yhb.get(0).groupWorker}</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>点检区域：</label><u><strong>【1#磅作业区】</u>
		</div>
		<tr>
			<td>设备名称</td>
			<td>点检项目</td>
			<td>点检内容</td>
			<td>点检时间</td>
			<td>点检结果</td>
			<td>备注</td>
		</tr>
		<c:forEach items="${yhb}" var="record" varStatus="s">
			<c:if test="${record.isAborted eq '是'}">

			</c:if>
			<c:if test="${record.deviceCheckRitemList.size() gt 0}">
				<c:forEach items="${record.deviceCheckRitemList}" var="list" varStatus="status">
					<tr>
						<td>${list.deviceName}</td>
						<td>${list.checkItem}</td>
						<td>${list.checkContent}</td>
						<td><fmt:formatDate value="${record.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<c:if test="${list.checkResult eq '0'}">
							<td>无异常</td>
						</c:if>
						<c:if test="${list.checkResult eq '1'}">
							<td>有异常</td>
						</c:if>
<%--						<td>${list.checkResult}</td>--%>
						<td>${list.remarks}</td>
					</tr>
				</c:forEach>
			</c:if>
		</c:forEach>
	</table>
<hr>
</c:if>
<c:if test="${shb.size() gt 0}">
	<table class="table table-bordered" align="center" style="width:90%;margin:auto">
		<div style="text-align:center">
			<label>点检人员：</label><u>${shb.get(0).groupWorker}</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>点检区域：</label><u><strong>【3#磅作业区】</u>
		</div>
		<tr>
			<td>设备名称</td>
			<td>点检项目</td>
			<td>点检内容</td>
			<td>点检时间</td>
			<td>点检结果</td>
			<td>备注</td>
		</tr>
		<c:forEach items="${shb}" var="record" varStatus="s">
			<c:if test="${record.deviceCheckRitemList.size() gt 0}">
				<c:forEach items="${record.deviceCheckRitemList}" var="list" varStatus="status">
					<tr>
						<td>${list.deviceName}</td>
						<td>${list.checkItem}</td>
						<td>${list.checkContent}</td>
						<td><fmt:formatDate value="${record.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<c:if test="${list.checkResult eq '0'}">
							<td>无异常</td>
						</c:if>
						<c:if test="${list.checkResult eq '1'}">
							<td>有异常</td>
						</c:if>
						<td>${list.remarks}</td>
					</tr>
				</c:forEach>
			</c:if>
		</c:forEach>
	</table>
<hr>
</c:if>
<c:if test="${gdb.size() gt 0}">
	<table class="table table-bordered" align="center" style="width:90%;margin:auto">
		<div style="text-align:center">
			<label>点检人员：</label><u>${gdb.get(0).groupWorker}</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>点检区域：</label><u><strong>【轨道磅作业区】</u>
		</div>
		<tr>
			<td>设备名称</td>
			<td>点检项目</td>
			<td>点检内容</td>
			<td>点检时间</td>
			<td>点检结果</td>
			<td>备注</td>
		</tr>
		<c:forEach items="${gdb}" var="record" varStatus="s">
			<c:if test="${record.deviceCheckRitemList.size() gt 0}">
				<c:forEach items="${record.deviceCheckRitemList}" var="list" varStatus="status">
					<tr>
						<td>${list.deviceName}</td>
						<td>${list.checkItem}</td>
						<td>${list.checkContent}</td>
						<td><fmt:formatDate value="${record.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<c:if test="${list.checkResult eq '0'}">
							<td>无异常</td>
						</c:if>
						<c:if test="${list.checkResult eq '1'}">
							<td>有异常</td>
						</c:if>
						<td>${list.remarks}</td>
					</tr>
				</c:forEach>
			</c:if>
		</c:forEach>
	</table>
<hr>
</c:if>
<c:if test="${jks.size() gt 0}">
	<table class="table table-bordered" align="center" style="width:90%;margin:auto">
		<div style="text-align:center">
			<label>点检人员：</label><u>${jks.get(0).groupWorker}</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>点检区域：</label><u><strong>【集控室作业区】</u>
		</div>
		<tr>
			<td>设备名称</td>
			<td>点检项目</td>
			<td>点检内容</td>
			<td>点检时间</td>
			<td>点检结果</td>
			<td>备注</td>
		</tr>
		<c:forEach items="${jks}" var="record" varStatus="s">
			<c:if test="${record.deviceCheckRitemList.size() gt 0}">
				<c:forEach items="${record.deviceCheckRitemList}" var="list" varStatus="status">
					<tr>
						<td>${list.deviceName}</td>
						<td>${list.checkItem}</td>
						<td>${list.checkContent}</td>
						<td><fmt:formatDate value="${record.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<c:if test="${list.checkResult eq '0'}">
							<td>无异常</td>
						</c:if>
						<c:if test="${list.checkResult eq '1'}">
							<td>有异常</td>
						</c:if>
						<td>${list.remarks}</td>
					</tr>
				</c:forEach>
			</c:if>
		</c:forEach>
	</table>
<hr>
</c:if>
<c:if test="${aborted.size() gt 0 }">
	<table class="table table-bordered" align="center" style="width:90%;margin:auto">
		<div style="text-align:center">
			<label><strong>【取消记录】</strong></label>
		</div>
		<tr>
			<td>记录id</td>
			<td>点检人员</td>
			<td>记录生成时间</td>
			<td>取消时间</td>
			<td>取消原因</td>
		</tr>
		<c:forEach items="${aborted}" var="record" varStatus="s">
			<tr>
				<td>${record.id}</td>
				<td>${record.groupWorker}</td>
				<td><fmt:formatDate value="${record.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${record.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${record.remarks}</td>
			</tr>
		</c:forEach>
	</table>
</c:if>
<%--<c:forEach items="${deviceCheckRecordList}" var="deviceCheckRecord" varStatus="status">--%>
<%--	<c:choose>--%>
<%--		<c:when test="${deviceCheckRecord.isAborted eq '是'}">--%>
<%--			<table class="table table-bordered" align="center" style="width:90%;margin:auto">--%>
<%--				<div style="text-align: center"><strong>记录：【${status.count}】</strong></div>--%>
<%--				<div style="text-align:center">--%>
<%--					<label>班组：</label><u>${deviceCheckRecord.workingGroup}</u>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<label>班组长：</label><u>${deviceCheckRecord.groupLeader}</u>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<label>点检人员：</label><u>${deviceCheckRecord.groupWorker}</u>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<label>点检区域：</label><u>${deviceCheckRecord.workingArea}</u>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<fmt:formatDate value="${deviceCheckRecord.updateDate}" var="date" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
<%--					<label>点检时间：</label><u>${date}</u>--%>
<%--				</div>--%>
<%--				<tr>--%>
<%--					<td colspan="8" style="text-align: center">点检记录被取消，取消原因为：${deviceCheckRecord.remarks}</td>--%>
<%--				</tr>--%>
<%--			</table>--%>
<%--		</c:when>--%>
<%--		<c:otherwise>--%>
<%--			<table class="table table-bordered" align="center" style="width:90%;margin:auto">--%>
<%--				<div style="text-align: center"><strong>记录：【${status.count}】</strong></div>--%>
<%--				<div style="text-align:center">--%>
<%--					<label>班组：</label><u>${deviceCheckRecord.workingGroup}</u>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<label>班组长：</label><u>${deviceCheckRecord.groupLeader}</u>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<label>点检人员：</label><u>${deviceCheckRecord.groupWorker}</u>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<label>点检区域：</label><u>${deviceCheckRecord.workingArea}</u>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<fmt:formatDate value="${deviceCheckRecord.updateDate}" var="date" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
<%--					<label>点检时间：</label><u>${date}</u>--%>
<%--				</div>--%>
<%--				<tr>--%>
<%--					<td>序号</td>--%>
<%--					<td>设备名称</td>--%>
<%--					<td>项目</td>--%>
<%--					<td>点检内容</td>--%>
<%--					<td>点检标准描述</td>--%>
<%--					<td>点检方法</td>--%>
<%--					<td>点检结果</td>--%>
<%--						&lt;%&ndash;			<td>设备状态</td>&ndash;%&gt;--%>
<%--					<td>备注</td>--%>
<%--				</tr>--%>
<%--				<c:forEach items="${deviceCheckRecord.deviceCheckRitemList}" var="item" varStatus="status">--%>
<%--					<tr>--%>
<%--						<td>${status.count}</td>--%>
<%--						<td>${item.deviceName}</td>--%>
<%--						<td>${item.checkItem}</td>--%>
<%--						<td>${item.checkContent}</td>--%>
<%--						<td>${item.checkReference}</td>--%>
<%--						<td>${item.checkMethod}</td>--%>
<%--						<c:if test="${item.checkResult=='0'}">--%>
<%--							<td>正常</td>--%>
<%--						</c:if>--%>
<%--						<c:if test="${item.checkResult=='1'}">--%>
<%--							<td>异常</td>--%>
<%--						</c:if>--%>
<%--							&lt;%&ndash;				<td>${item.deviceState}</td>&ndash;%&gt;--%>
<%--						<td>${item.remarks}</td>--%>
<%--					</tr>--%>
<%--				</c:forEach>--%>
<%--			</table>--%>
<%--		</c:otherwise>--%>
<%--	</c:choose>--%>

<%--	<hr/>--%>
<%--</c:forEach>--%>

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