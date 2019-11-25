<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>点检表管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

        $(document).ready(function() {

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
		<c:choose>
			<c:when test="${checkes.checkState eq null || checkes.checkState eq '已计划'}">
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>点检标准号：</label></td>
					<td class="width-35">
						<form:input path="checkNumber" htmlEscape="false"    class="form-control required isIntGteZero"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>设备名称：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/checkequipment/equipment/data" id="equipment" name="equipment.id" value="${checkes.equipment.id}" labelName="equipment.equipment" labelValue="${checkes.equipment.equipment}"
										title="选择设备名称" cssClass="form-control required" fieldLabels="设备名称|设备检查方法|点检设备描述" fieldKeys="equipment|checkMethod|maintenExplanation" searchLabels="设备名称|设备检查方法" searchKeys="equipment|checkMethod" onchange="getEquipment()"></sys:gridselect>
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
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>点检人：</label></td>
					<td class="width-35">
						<sys:userselect id="checkInspector" name="checkInspector.id" value="${UserUtils.get(checkes.checkInspector.id)}" labelName="checkInspector.name" labelValue="${checkes.checkInspector.name}"
										cssClass="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>点检日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='checkDate'>
							<input type='text'  name="checkDate" class="form-control "  value="<fmt:formatDate value="${checkes.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
						</div>
					</td>
				</tr>
			</c:when>
		</c:choose>
		</tbody>
	</table>
</form:form>
</body>
</html>