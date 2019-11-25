<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车辆进出记录管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

	        $('#intoTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#outTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/vehicleaccessrecord/vehicleAccessRecord/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="vehicleAccessRecord" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">车牌号：</label></td>
					<td class="width-35">
						<form:input path="vehicleNo" htmlEscape="false"    class="form-control " disabled="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">司机名称：</label></td>
					<td class="width-35">
						<form:input path="transContactPerson" htmlEscape="false"    class="form-control " disabled="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">身份证号：</label></td>
					<td class="width-35">
						<form:input path="idcard" htmlEscape="false"    class="form-control " disabled="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">司机联系方式：</label></td>
					<td class="width-35">
						<form:input path="transContactPersonTel" htmlEscape="false"    class="form-control " disabled="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">RFID卡号：</label></td>
					<td class="width-35">
						<form:input path="rfidNo" htmlEscape="false"    class="form-control " disabled="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">委托单类型：</label></td>
					<td class="width-35">
						<form:select path="consignType"  class="form-control m-b" disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('consign_type')}" itemLabel="label" itemValue="value" htmlEscape="false" disabled="true"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">进厂时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='intoTime'>
							<input type='text'  name="intoTime" class="form-control "  value="<fmt:formatDate value="${vehicleAccessRecord.intoTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" disabled="true"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">出厂时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='outTime'>
							<input type='text'  name="outTime" class="form-control "  value="<fmt:formatDate value="${vehicleAccessRecord.outTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" disabled="true"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">车辆状态：</label></td>
					<td class="width-35">
						<form:select path="state" class="form-control " disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('vehicle_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control " disabled="true"/>
					</td>
				</tr>
		 	</tbody>
		</table>

	</form:form>
</body>
</html>