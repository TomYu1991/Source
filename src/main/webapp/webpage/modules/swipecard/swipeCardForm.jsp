<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>安保人员管理</title>
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
                jp.post("${ctx}/swipecard/swipeCard/save",$('#inputForm').serialize(),function(data){
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

        $(document).ready(function() {

            $('#startTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#endTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
        });
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="swipeCard" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">工号：</label></td>
					<td class="width-35">
						<form:input path="jobNumber" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">姓名：</label></td>
					<td class="width-35">
						<form:input path="workName" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属班组：</label></td>
					<td class="width-35">
						<form:select path="officeId"  class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('swipe_class')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">联系方式：</label></td>
					<td class="width-35">
						<form:input path="telephone" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
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