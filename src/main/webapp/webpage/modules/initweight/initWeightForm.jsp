<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>过磅初始信息管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

	        $('#weightTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/initweight/initWeight/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="initWeight" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">车牌号：</label></td>
					<td class="width-35">
						<form:input path="vehicleNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">铁水罐号：</label></td>
					<td class="width-35">
						<form:input path="ladleNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">品名：</label></td>
					<td class="width-35">
						<form:input path="prodCname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">称重时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='weightTime'>
							<input type='text'  name="weightTime" class="form-control "  value="<fmt:formatDate value="${initWeight.weightTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">重量：</label></td>
					<td class="width-35">
						<form:input path="weightWt" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">锁皮标记：</label></td>
					<td class="width-35">
						<form:select path="defaultFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('default_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">过磅工作站：</label></td>
					<td class="width-35">
						<form:select path="stationIp" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('workststion_ip')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>