<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>出门条信息管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/passcheck/passCheck/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="passCheck" class="form-horizontal">
		<form:hidden path="id"/>
			<table class="table table-bordered">
				<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">出门条号：</label></td>
					<td class="width-35">
						<form:input path="trnpAppNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">用户姓名：</label></td>
					<td class="width-35">
						<form:input path="userName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">车牌号：</label></td>
					<td class="width-35">
						<form:input path="vehicleNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">司机：</label></td>
					<td class="width-35">
						<form:input path="transContactPerson" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">签发部门代码：</label></td>
					<td class="width-35">
						<form:input path="deptCode" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">签发部门名称：</label></td>
					<td class="width-35">
						<form:input path="depName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">签发人员工号：</label></td>
					<td class="width-35">
						<form:input path="dealPersonNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">签发人员姓名：</label></td>
					<td class="width-35">
						<form:input path="dealPersonName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">签发日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='dealDate'>
							<input type='text'  name="dealDate" class="form-control "  value="<fmt:formatDate value="${passCheck.dealDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">放行码：</label></td>
					<td class="width-35">
						<form:input path="passCode" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">承运公司名称：</label></td>
					<td class="width-35">
						<form:input path="carryCompanyName" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">公司中文名称：</label></td>
					<td class="width-35">
						<form:input path="companyCname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">类别代码：</label></td>
					<td class="width-35">
						<form:select path="typeCode"  class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pass_check_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开始时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='startTime'>
							<input type='text'  name="startTime" class="form-control "  value="<fmt:formatDate value="${passCheck.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">结束时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='endTime'>
							<input type='text'  name="endTime" class="form-control "  value="<fmt:formatDate value="${passCheck.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">申请时刻：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='applyTime'>
							<input type='text'  name="applyTime" class="form-control "  value="<fmt:formatDate value="${passCheck.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>

					<td class="width-15 active"><label class="pull-right">申请人工号：</label></td>
					<td class="width-35">
						<form:input path="applyPersonNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">审批时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='approveTime'>
							<input type='text'  name="approveTime" class="form-control "  value="<fmt:formatDate value="${passCheck.approveTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>

					<td class="width-15 active"><label class="pull-right">审批人工号：</label></td>
					<td class="width-35">
						<form:input path="approvePersonNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">生效标记：</label></td>
					<td class="width-35">
						<form:select path="validFlag" class="form-control">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pass_check_effect')}"  itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35">
						<form:textarea path="remark" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
					<td class="width-35" ></td>
				</tr>
				</tbody>
			</table>
	</form:form>
</body>
</html>