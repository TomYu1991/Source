<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备点检配置</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="deviceCheckRecordList.js" %>
	<script type="text/javascript">

		$(document).ready(function() {

		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/check/deviceCheckConfig/save",$('#inputForm').serialize(),function(data){
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
		function fun(id){
			var v = document.getElementById(id).value;
			var day = document.getElementById("checkDate");
			day.innerHTML="";
			if(v=='week'){
				for(var i=1;i<8;i++){
					// var obj = new Option(i);
					// citySelObj.innerHTML+=("<option>"+cities[i]+"</option>")
					day.innerHTML+=("<option>"+i+"</option>")
				}
			}else if(v=='month'){
				for(var i=1;i<31;i++){
					// var obj = new Option(i);
					day.innerHTML+=("<option>"+i+"</option>")
				}
			}else{
				// alert("lalala")
			}
		}



		// function fun(id){
		// 	//alert(index);
		// 	var cities=arr[index];
		//
		// 	//获取市的下拉选
		// 	var citySelObj=document.getElementsByName("city")[0];
		//
		// 	//先初始化
		// 	citySelObj.innerHTML="<option >-请选择-</option>";
		//
		// 	//遍历数组 组装成option 追加到select
		// 	for(var i=0;i<cities.length;i++){
		// 		citySelObj.innerHTML+=("<option>"+cities[i]+"</option>");
		// 	}
		// }
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="deviceCheckConfig" class="form-horizontal">
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
					<td class="width-15 active"><label class="pull-right">点检周期：</label></td>
					<td class="width-35">
						<form:select path="checkCycle"  class="form-control m-b" id="checkCycle" onchange="fun(this.id)">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('check_cycle')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">点检频率：</label></td>
					<td class="width-35">
						<form:select path="checkDate"  class="form-control m-b" id="checkDate" multiple="multiple">
							<form:option value="" label=""/>
<%--							<form:options items="${fns:getDictList('is_enable')}" itemLabel="label" itemValue="value" htmlEscape="false" id="option"/>--%>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">是否启用：</label></td>
					<td class="width-35">
						<form:select path="isEnable"  class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('is_enable')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>

</html>