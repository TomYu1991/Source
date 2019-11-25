<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车辆信息管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

            $('#dealTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#approveTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#startTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#endTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
        })
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/vehicleinfo/vehicleInfo/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="vehicleInfo" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">公司中文名称：</label></td>
					<td class="width-35">
						<form:input path="companyCname" htmlEscape="false"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">类别代码：</label></td>
					<td class="width-35">
						<form:input path="typeCode" htmlEscape="false"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">车牌号：</label></td>
					<td class="width-35">
						<form:input path="vehicleNo" htmlEscape="false"  class="form-control "/>
					</td>

					<td class="width-15 active"><label class="pull-right">组批代码：</label></td>
					<td class="width-35">
						<form:input path="groupCode" htmlEscape="false"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">放行码：</label></td>
					<td class="width-35">
						<form:input path="passCode" htmlEscape="false"  class="form-control "/>
					</td>

					<td class="width-15 active"><label class="pull-right">RFID卡号：</label></td>
					<td class="width-35">
						<form:input path="rfidNo" htmlEscape="false"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">集团公司名称：</label></td>
					<td class="width-35">
						<form:input path="groupCompanyName" htmlEscape="false"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">承运公司名称：</label></td>
					<td class="width-35">
						<form:input path="carryCompanyName" htmlEscape="false"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">运输联系人：</label></td>
					<td class="width-35">
						<form:input path="transContactPerson" htmlEscape="false"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">运输联系人电话：</label></td>
					<td class="width-35">
						<form:input path="transContactPersonTel" htmlEscape="false"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">局车类型：</label></td>
					<td class="width-35">
						<form:input path="wagonType" htmlEscape="false"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">数量：</label></td>
					<td class="width-35">
						<form:input path="qty" htmlEscape="false"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">处置时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='dealTime'>
							<input type='text'  name="dealTime" class="form-control " value="<fmt:formatDate value="${vehicleInfo.dealTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">审批时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='approveTime'>
							<input type='text'  name="approveTime" class="form-control "  value="<fmt:formatDate value="${vehicleInfo.approveTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开始时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='startTime'>
							<input type='text'  name="startTime" class="form-control "  value="<fmt:formatDate value="${vehicleInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">结束时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='endTime'>
							<input type='text'  name="endTime" class="form-control "  value="<fmt:formatDate value="${vehicleInfo.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="100" class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>