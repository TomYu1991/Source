<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备点检子项配置</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="deviceCheckItemList.js" %>
	<script type="text/javascript">

		$(document).ready(function() {

		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/check/deviceCheckItem/save",$('#inputForm').serialize(),function(data){
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
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="deviceCheckItem" class="form-horizontal">
		<form:hidden path="configId"/>
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">设备名称：</label></td>
					<td class="width-35">
						<form:input path="deviceName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">点检项目：</label></td>
					<td class="width-35">
						<form:input path="checkItem" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">点检内容：</label></td>
					<td class="width-35">
						<form:textarea path="checkContent" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">点检标准：</label></td>
					<td class="width-35">
						<form:textarea path="checkReference" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">点检方法：</label></td>
					<td class="width-35">
						<form:textarea path="checkMethod" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">设备状态：</label></td>
<%--					<td class="width-35">--%>
<%--						<form:textarea path="remarks" htmlEscape="false"    class="form-control "/>--%>
<%--					</td>--%>
					<td class="width-35">
						<form:select path="deviceState"  class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('device_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
<%--				<tr>--%>

<%--					<td class="width-15 active"><label class="pull-right">配置表Id：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:input path="ItemId" value="${deviceCheckItem.ItemId}" htmlEscape="false"    class="form-control "/>--%>
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
		 	</tbody>
		</table>
	</form:form>
</body>
</html>