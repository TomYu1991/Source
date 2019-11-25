<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备点检结果修改</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="deviceCheckRecordList.js" %>
	<script type="text/javascript">

		$(document).ready(function() {
			$('#updateDate').datetimepicker({
				format: "YYYY-MM-DD HH:mm:ss"
			});

		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/check/deviceCheckResult/save",$('#inputForm').serialize(),function(data){
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
	<script type="text/javascript">



	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="deviceCheckRecord" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">班组：</label></td>
					<td class="width-35">
						<form:select path="workingGroup"  class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('working_group')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">作业区域：</label></td>
					<td class="width-35">
						<form:select path="workingArea"  class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('working_area')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">班组长：</label></td>
					<td class="width-35">
						<form:input path="groupLeader" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">点检时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='updateDate'>
							<input type="text" name="updateDate" class="form-control" value="<fmt:formatDate value="${deviceCheckRecord.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>

					</td>
				</tr>
<%--				<tr>--%>
<%--					<td class="width-15 active"><label class="pull-right">点检频率：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<form:select path="checkDate"  class="form-control m-b" id="checkDate" multiple="multiple">--%>
<%--							<form:option value="" label=""/>--%>
<%--&lt;%&ndash;							<form:options items="${fns:getDictList('is_enable')}" itemLabel="label" itemValue="value" htmlEscape="false" id="option"/>&ndash;%&gt;--%>
<%--						</form:select>--%>
<%--					</td>--%>
<%--					<td class="width-15 active"><label class="pull-right">点检时间：</label></td>--%>
<%--					<td class="width-35">--%>
<%--						<div class='input-group form_datetime' id='updateTime'>--%>
<%--							<input type='text' disabled="true"   name="updateTime" class="form-control " />"/>--%>
<%--							<span class="input-group-addon">--%>
<%--								<span class="glyphicon glyphicon-calendar"></span>--%>
<%--							</span>--%>
<%--						</div>--%>
<%--					</td>--%>
<%--				</tr>--%>
		 	</tbody>
		</table>
	</form:form>
</body>

</html>