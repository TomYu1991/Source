<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>点检表管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

        $(document).ready(function() {

            if($('#checkContect') .length > 0){
            $('#checkNumber').attr("readOnly","true");

            }

            $('#checkDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            //设置核验时间最大为当前时间
            var value = $('#checkDate').find('input[name="checkDate"]').val();
            value = value?value:new Date();
            $('#checkDate').data("DateTimePicker").minDate(new Date());
            $('#checkDate').data("DateTimePicker").date(value);

            });



        function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
            }else{
                jp.loading();
                jp.post("${ctx}/checkall/checkes/save",$('#inputForm').serialize(),function(data){
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
        function getEquipment() {
            jp.post("${ctx}/checkall/checkes/getEquipment",$('#inputForm').serialize(),function(data){
                if(data.success) {
                    var checkes = data.data;

                    var checkMethod = checkes.checkMethod;
                    $('#checkMethod').val(checkMethod);
                    var maintenExplanation = checkes.maintenExplanation;
                    $('#maintenExplanation').val(maintenExplanation);



                }
            })
        }


	</script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="checkes" class="form-horizontal">
	<form:hidden path="id"/>
	<table class="table table-bordered">
		<tbody>
		<c:if test="${checkes.checkState eq null || checkes.checkState eq '已计划' || checkes.checkState eq '已检查'}">
		<tr>
			<td class="width-15 active"><label class="pull-right"><font color="red">*</font>点检标准号：</label></td>
			<td class="width-35">
				<form:input path="checkNumber" htmlEscape="false"    class="form-control required isIntGteZero"/>
			</td>
            <td class="width-15 active"><label class="pull-right">设备名称：</label></td>
            <td class="width-35">
                <form:input path="equipment" Value="${checkes.equipment.equipment}" htmlEscape="false"    class="form-control " disabled="true"/>
            </td>
		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">设备检查方法：</label></td>
			<td class="width-35">
				<form:input path="checkMethod" htmlEscape="false"    class="form-control " disabled="true"/>
			</td>
			<td class="width-15 active"><label class="pull-right">点检设备描述：</label></td>
			<td class="width-35">
				<form:textarea path="maintenExplanation" htmlEscape="false" rows="4"    class="form-control" disabled="true"/>
			</td>
		</tr>
		</c:if>
		<tr>
            <td class="width-15 active"><label class="pull-right">点检人：</label></td>
            <td class="width-35">
                <form:input path="checkInspector" Value="${checkes.checkInspector.name}" htmlEscape="false"    class="form-control " disabled="true"/>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>点检日期：</label></td>
            <td class="width-35">
                <div class='input-group form_datetime' id='checkDate'>
                    <input type='text'  name="checkDate" disabled="true" class="form-control "  value="<fmt:formatDate value="${checkes.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                </div>
            </td>
		</tr>
        <c:if test="${checkes.checkState eq '已计划' || checkes.checkState eq '已检查'}">
		<tr>
			<td class="width-15 active"><label class="pull-right"><font color="red">*</font>点检内容：</label></td>
			<td class="width-35">
				<form:input path="checkContect" htmlEscape="false"    class="form-control required"/>
			</td>
			<td class="width-15 active"><label class="pull-right"><font color="red">*</font>点检结果：</label></td>
			<td class="width-35">
				<form:select path="checkResult" class="form-control required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('dic_check_result')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</td>
		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">点检状态：</label></td>
			<td class="width-35">
				<form:input path="checkState" htmlEscape="false"    class="form-control " disabled="true"/>
			</td>
			<td class="width-15 active"><label class="pull-right">备注：</label></td>
			<td class="width-35">
				<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
			</td>
		</tr>
		</c:if>
		</tbody>
	</table>
</form:form>
</body>
</html>