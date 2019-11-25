<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>查看点检设备表管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
            $('#station').attr("readOnly","true");
            $('#equipment').attr("readOnly","true");
            $('#checkMethod').attr("readOnly","true");
            $('#maintenExplanation').attr("readOnly","true");
		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/checkequipment/equipment/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="equipment" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">工作站：</label></td>
					<td class="width-35">
						<form:input path="station" Value="${equipment.station.workStation}" htmlEscape="false" class="form-control" />
					</td>
					<td class="width-15 active"><label class="pull-right">设备名称：</label></td>
					<td class="width-35">
						<form:input path="equipment" htmlEscape="false"    class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">设备检查方法：</label></td>
					<td class="width-35">
						<form:input path="checkMethod" htmlEscape="false"    class="form-control"/>
					</td>
					<td class="width-15 active"><label class="pull-right">点检设备描述：</label></td>
					<td class="width-35">
						<form:textarea path="maintenExplanation" htmlEscape="false" rows="4"    class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">设备类型：</label></td>
					<td class="width-35">
						<form:select path="equipmentType" class="form-control" disabled="true">
						<form:option value="" label="" disabled="ture"/>
						<form:options items="${fns:getDictList('dic_equipment_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>