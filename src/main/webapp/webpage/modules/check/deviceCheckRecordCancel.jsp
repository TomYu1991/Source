<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>取消点检记录</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="deviceCheckItemList.js" %>
	<script type="text/javascript">
		$(function(){

		})
		function save() {
			var isValidate = jp.validateForm('#inputForm');//校验表单
			if(!isValidate){
				return false;
			}else{
				jp.loading();
				jp.post("${ctx}/check/deviceCheckRecord/updateCancel",$('#inputForm').serialize(),function(data){
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
<form:form id="inputForm" modelAttribute="deviceCheckRecord" class="form-horizontal">
	<form:hidden path="id"/>
	<form:hidden path="groupWorker" value="${fns:getUser().name}"/>
	<table class="table table-bordered">
		<tbody>
		<tr>
			<td class="width-15 active"><label class="pull-right">取消原因：</label></td>
			<td class="width-35">
				<form:textarea path="remarks" htmlEscape="false"    class="form-control "/>
			</td>
		</tr>
		</tbody>
	</table>
</form:form>

</body>
</html>