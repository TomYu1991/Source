<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>员工通行记录管理</title>
	<meta name="decorator" content="ani"/>
<%--	<script type="text/javascript">--%>

<%--		$(document).ready(function() {--%>

<%--		});--%>
<%--		function save() {--%>
<%--            var isValidate = jp.validateForm('#inputForm');//校验表单--%>
<%--            if(!isValidate){--%>
<%--                return false;--%>
<%--			}else{--%>
<%--                jp.loading();--%>
<%--                jp.post("${ctx}/userpasscord/userPassRecord/save",$('#inputForm').serialize(),function(data){--%>
<%--                    if(data.success){--%>
<%--                        jp.getParent().refresh();--%>
<%--                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引--%>
<%--                        parent.layer.close(dialogIndex);--%>
<%--                        jp.success(data.msg)--%>

<%--                    }else{--%>
<%--                        jp.error(data.msg);--%>
<%--                    }--%>
<%--                })--%>
<%--			}--%>

<%--        }--%>
<%--	</script>--%>
</head>
<body class="bg-white">
<%--		<form:form id="inputForm" modelAttribute="userPassRecord" class="form-horizontal">--%>
<%--		<form:hidden path="id"/>	--%>
<%--		<table class="table table-bordered">--%>
<%--		   <tbody>--%>
<%--				<tr>--%>
<%--					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>--%>
<%--					</td>--%>
<%--					<td class="width-15 active"><label class="pull-right">工号：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:input path="personnelId" htmlEscape="false"    class="form-control "/>--%>
<%--					</td>--%>
<%--				</tr>--%>
<%--				<tr>--%>
<%--					<td class="width-15 active"><label class="pull-right">姓名：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:input path="name" htmlEscape="false"    class="form-control "/>--%>
<%--					</td>--%>
<%--					<td class="width-15 active"><label class="pull-right">性别：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:input path="sex" htmlEscape="false"    class="form-control "/>--%>
<%--					</td>--%>
<%--				</tr>--%>
<%--				<tr>--%>
<%--					<td class="width-15 active"><label class="pull-right">识别卡号：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:input path="cardSerial" htmlEscape="false"    class="form-control "/>--%>
<%--					</td>--%>
<%--					<td class="width-15 active"><label class="pull-right">部门名称：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:input path="deptName" htmlEscape="false"    class="form-control "/>--%>
<%--					</td>--%>
<%--				</tr>--%>
<%--				<tr>--%>
<%--					<td class="width-15 active"><label class="pull-right">通行时间：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:input path="passTime" htmlEscape="false"    class="form-control "/>--%>
<%--					</td>--%>
<%--					<td class="width-15 active"><label class="pull-right">通行门岗：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:input path="passStation" htmlEscape="false"    class="form-control "/>--%>
<%--					</td>--%>
<%--				</tr>--%>
<%--				<tr>--%>
<%--					<td class="width-15 active"><label class="pull-right">通行类型：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:select path="passType" class="form-control ">--%>
<%--							<form:option value="" label=""/>--%>
<%--							<form:options items="${fns:getDictList('pass_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
<%--						</form:select>--%>
<%--					</td>--%>
<%--					<td class="width-15 active"></td>--%>
<%--		   			<td class="width-35" ></td>--%>
<%--		  		</tr>--%>
<%--		 	</tbody>--%>
<%--		</table>--%>
<%--	</form:form>--%>
<h1>hello world!</h1>
</body>
</html>