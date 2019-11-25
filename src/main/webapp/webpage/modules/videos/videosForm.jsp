<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>视频配置管理管理</title>
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
                jp.post("${ctx}/videos/videos/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="videos" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
		   <tr>
			   <td class="width-15 active"><label class="pull-right"><font color="red">*</font>单位：</label></td>
			   <td class="width-35">
				   <form:select path="institution" class="form-control required">
					   <form:option value="" label=""/>
					   <form:options items="${fns:getDictList('institution')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				   </form:select>
			   </td>
			   <td class="width-15 active"><label class="pull-right">工作站：</label></td>
			   <td class="width-35">
				   <sys:gridselect url="${ctx}/station/workStation/data" id="station" name="station.id" value="${videos.station.id}" labelName="station.workStation" labelValue="${videos.station.workStation}"
								   title="选择工作站" cssClass="form-control " fieldLabels="名称|位置|类型" fieldKeys="workStation|location|type" searchLabels="名称|类型 " searchKeys="workStation|type" ></sys:gridselect>
			   </td>
		   </tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>摄像机名：</label></td>
					<td class="width-35">
						<form:input path="cameraName" htmlEscape="false" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>摄像机IP：</label></td>
					<td class="width-35">
						<form:input path="cameraIp" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>摄像机端口号：</label></td>
					<td class="width-35">
						<form:input path="cameraPort" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>摄像头的编码：</label></td>
					<td class="width-35">
						<form:input path="cameraNo" htmlEscape="false"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用户名：</label></td>
					<td class="width-35">
						<form:input path="username" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>密码：</label></td>
					<td class="width-35">
						<form:input path="password" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">种类：</label></td>
					<td class="width-35">
						<form:select path="cameraType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('camera_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>显示顺序：</label></td>
					<td class="width-35">
						<form:input path="cameraOrder" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否正常：</label></td>
					<td class="width-35">
						<form:select path="isNormal" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('is_normal')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">分辨率：</label></td>
					<td class="width-35">
						<form:input path="resolution" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">帧数：</label></td>
					<td class="width-35">
						<form:input path="frames" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">视频组端口号：</label></td>
					<td class="width-35">
						<form:input path="videoGroup" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">视频通道：</label></td>
					<td class="width-35">
						<form:input path="videoCamera" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">视频模式：</label></td>
					<td class="width-35">
						<form:input path="videoMode" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>