<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作站管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			jp.ajaxForm("#inputForm",function(data){
				if(data.success){
				    jp.success(data.msg);
					jp.go("${ctx}/station/workStation");
				}else{
				    jp.error(data.msg);
				    $("#inputForm").find("button:submit").button("reset");
				}
			});
			
		});
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
			$(list+idx).find(".form_datetime").each(function(){
				 $(this).datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
			    });
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/station/workStation"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="workStation" action="${ctx}/station/workStation/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
				<div class="form-group">
					<label class="col-sm-2 control-label">名称：</label>
					<div class="col-sm-10">
						<form:input path="workStation" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">位置：</label>
					<div class="col-sm-10">
						<form:input path="location" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">进门放行码：</label>
					<div class="col-sm-10">
						<form:input path="inPassCode" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">出门放行码：</label>
				<div class="col-sm-10">
					<form:input path="outPassCode" htmlEscape="false"    class="form-control "/>
				</div>
			</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">类型：</label>
					<div class="col-sm-10">
						<form:select path="type" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('station_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">工作站IP地址：</label>
					<div class="col-sm-10">
						<form:input path="stationIp" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">设备管理：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">人员管理：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#stationDeviceList', stationDeviceRowIdx, stationDeviceTpl);stationDeviceRowIdx = stationDeviceRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>设备名称</th>
						<th>设备编号</th>
						<th>设备类型</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="stationDeviceList">
				</tbody>
			</table>
			<script type="text/template" id="stationDeviceTpl">
				<tr id="stationDeviceList{{idx}}">
					<td class="hide">
						<input id="stationDeviceList{{idx}}_id" name="stationDeviceList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="stationDeviceList{{idx}}_delFlag" name="stationDeviceList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="stationDeviceList{{idx}}_deviceName" name="stationDeviceList[{{idx}}].deviceName" type="text" value="{{row.deviceName}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="stationDeviceList{{idx}}_deviceNum" name="stationDeviceList[{{idx}}].deviceNum" type="text" value="{{row.deviceNum}}"    class="form-control "/>
					</td>
					
					
					<td>
						<select id="stationDeviceList{{idx}}_deviceType" name="stationDeviceList[{{idx}}].deviceType" data-value="{{row.deviceType}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('device_type')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>

					<td>
						<textarea id="stationDeviceList{{idx}}_remarks" name="stationDeviceList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#stationDeviceList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var stationDeviceRowIdx = 0, stationDeviceTpl = $("#stationDeviceTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(workStation.stationDeviceList)};
					for (var i=0; i<data.length; i++){
						addRow('#stationDeviceList', stationDeviceRowIdx, stationDeviceTpl, data[i]);
						stationDeviceRowIdx = stationDeviceRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane fade">
			<a class="btn btn-white btn-sm" onclick="addRow('#stationWorkerList', stationWorkerRowIdx, stationWorkerTpl);stationWorkerRowIdx = stationWorkerRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>人员id</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="stationWorkerList">
				</tbody>
			</table>
			<script type="text/template" id="stationWorkerTpl">//<!--
				<tr id="stationWorkerList{{idx}}">
					<td class="hide">
						<input id="stationWorkerList{{idx}}_id" name="stationWorkerList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="stationWorkerList{{idx}}_delFlag" name="stationWorkerList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td  class="max-width-250">
						<sys:userselect id="stationWorkerList{{idx}}_user" name="stationWorkerList[{{idx}}].user.id" value="{{row.user.id}}" labelName="stationWorkerList{{idx}}.user.name" labelValue="{{row.user.name}}"
							    cssClass="form-control "/>
					</td>
					
					
					<td>
						<textarea id="stationWorkerList{{idx}}_remarks" name="stationWorkerList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#stationWorkerList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var stationWorkerRowIdx = 0, stationWorkerTpl = $("#stationWorkerTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(workStation.stationWorkerList)};
					for (var i=0; i<data.length; i++){
						addRow('#stationWorkerList', stationWorkerRowIdx, stationWorkerTpl, data[i]);
						stationWorkerRowIdx = stationWorkerRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		<c:if test="${mode == 'add' || mode=='edit'}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                 </div>
		             </div>
		        </div>
		</c:if>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>