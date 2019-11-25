<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>磅单信息记录管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			jp.ajaxForm("#inputForm",function(data){
				if(data.success){
				    jp.success(data.msg);
					jp.go("${ctx}/weightrecord/weightRecord");
				}else{
				    jp.error(data.msg);
				    $("#inputForm").find("button:submit").button("reset");
				}
			});
			
	        $('#grosstime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#taretime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
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
				<a class="panelButton" href="${ctx}/weightrecord/weightRecord"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="weightRecord" action="${ctx}/weightrecord/weightRecord/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
				<div class="form-group">
					<label class="col-sm-2 control-label">磅单号：</label>
					<div class="col-sm-10">
						<form:input path="weighNo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">委托单号：</label>
					<div class="col-sm-10">
						<form:input path="consignId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">车牌号：</label>
					<div class="col-sm-10">
						<form:input path="vehicleNo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">铁水罐号：</label>
					<div class="col-sm-10">
						<form:input path="ladleNo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">品名：</label>
					<div class="col-sm-10">
						<form:input path="prodCname" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">钢级：</label>
					<div class="col-sm-10">
						<form:input path="sgSign" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">净重：</label>
					<div class="col-sm-10">
						<form:input path="matWt" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">毛重：</label>
					<div class="col-sm-10">
						<form:input path="matGrossWt" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">皮重：</label>
					<div class="col-sm-10">
						<form:input path="impWt" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">状态：</label>
					<div class="col-sm-10">
						<form:select path="status" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('weigh_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">收货方：</label>
					<div class="col-sm-10">
						<form:input path="consigneUser" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">发货方：</label>
					<div class="col-sm-10">
						<form:input path="supplierName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">一次过磅工作站：</label>
					<div class="col-sm-10">
						<form:select path="fistStation" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('workststion_ip')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">二次过磅工作站：</label>
					<div class="col-sm-10">
						<form:select path="secondStation" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('workststion_ip')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">一次过磅时间：</label>
					<div class="col-sm-10">
						<div class='input-group form_datetime' id='grosstime'>
							<input type='text'  name="grosstime" class="form-control "  value="<fmt:formatDate value="${weightRecord.grosstime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">二次过磅时间：</label>
					<div class="col-sm-10">
						<div class='input-group form_datetime' id='taretime'>
							<input type='text'  name="taretime" class="form-control "  value="<fmt:formatDate value="${weightRecord.taretime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">锁皮标记：</label>
					<div class="col-sm-10">
						<form:select path="defaultFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('default_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">预留字段：</label>
					<div class="col-sm-10">
						<form:input path="readyOne" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">预留字段：</label>
					<div class="col-sm-10">
						<form:input path="readyTwo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">预留字段：</label>
					<div class="col-sm-10">
						<form:input path="readyThree" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">预留字段：</label>
					<div class="col-sm-10">
						<form:input path="readyFour" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">初始磅单数据表：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">打印记录：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">修改磅单记录表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#initWeightList', initWeightRowIdx, initWeightTpl);initWeightRowIdx = initWeightRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>车牌号</th>
						<th>铁水罐号</th>
						<th>品名</th>
						<th>称重时间</th>
						<th>重量</th>
						<th>锁皮标记</th>
						<th>过磅工作站</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="initWeightList">
				</tbody>
			</table>
			<script type="text/template" id="initWeightTpl">//<!--
				<tr id="initWeightList{{idx}}">
					<td class="hide">
						<input id="initWeightList{{idx}}_id" name="initWeightList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="initWeightList{{idx}}_delFlag" name="initWeightList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="initWeightList{{idx}}_vehicleNo" name="initWeightList[{{idx}}].vehicleNo" type="text" value="{{row.vehicleNo}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="initWeightList{{idx}}_ladleNo" name="initWeightList[{{idx}}].ladleNo" type="text" value="{{row.ladleNo}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="initWeightList{{idx}}_prodCname" name="initWeightList[{{idx}}].prodCname" type="text" value="{{row.prodCname}}"    class="form-control "/>
					</td>
					
					
					<td>
						<div class='input-group form_datetime' id="initWeightList{{idx}}_weightTime">
		                    <input type='text'  name="initWeightList[{{idx}}].weightTime" class="form-control "  value="{{row.weightTime}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					<td>
						<input id="initWeightList{{idx}}_weightWt" name="initWeightList[{{idx}}].weightWt" type="text" value="{{row.weightWt}}"    class="form-control "/>
					</td>
					
					
					<td>
						<select id="initWeightList{{idx}}_defaultFlag" name="initWeightList[{{idx}}].defaultFlag" data-value="{{row.defaultFlag}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('default_flag')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="initWeightList{{idx}}_stationIp" name="initWeightList[{{idx}}].stationIp" data-value="{{row.stationIp}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('workststion_ip')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<textarea id="initWeightList{{idx}}_remarks" name="initWeightList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#initWeightList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var initWeightRowIdx = 0, initWeightTpl = $("#initWeightTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(weightRecord.initWeightList)};
					for (var i=0; i<data.length; i++){
						addRow('#initWeightList', initWeightRowIdx, initWeightTpl, data[i]);
						initWeightRowIdx = initWeightRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane fade">
			<a class="btn btn-white btn-sm" onclick="addRow('#printRecordList', printRecordRowIdx, printRecordTpl);printRecordRowIdx = printRecordRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>备注信息</th>
						<th>操作</th>
						<th>打印位置</th>
						<th>车牌号</th>
						<th>铁水罐号</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="printRecordList">
				</tbody>
			</table>
			<script type="text/template" id="printRecordTpl">//<!--
				<tr id="printRecordList{{idx}}">
					<td class="hide">
						<input id="printRecordList{{idx}}_id" name="printRecordList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="printRecordList{{idx}}_delFlag" name="printRecordList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<textarea id="printRecordList{{idx}}_remarks" name="printRecordList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					
					<td>
						<input id="printRecordList{{idx}}_operation" name="printRecordList[{{idx}}].operation" type="text" value="{{row.operation}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="printRecordList{{idx}}_stationIp" name="printRecordList[{{idx}}].stationIp" type="text" value="{{row.stationIp}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="printRecordList{{idx}}_vehicleNo" name="printRecordList[{{idx}}].vehicleNo" type="text" value="{{row.vehicleNo}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="printRecordList{{idx}}_ladelNo" name="printRecordList[{{idx}}].ladelNo" type="text" value="{{row.ladelNo}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#printRecordList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var printRecordRowIdx = 0, printRecordTpl = $("#printRecordTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(weightRecord.printRecordList)};
					for (var i=0; i<data.length; i++){
						addRow('#printRecordList', printRecordRowIdx, printRecordTpl, data[i]);
						printRecordRowIdx = printRecordRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-3" class="tab-pane fade">
			<a class="btn btn-white btn-sm" onclick="addRow('#updateWeightRecordList', updateWeightRecordRowIdx, updateWeightRecordTpl);updateWeightRecordRowIdx = updateWeightRecordRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>操作</th>
						<th>操作内容</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="updateWeightRecordList">
				</tbody>
			</table>
			<script type="text/template" id="updateWeightRecordTpl">//<!--
				<tr id="updateWeightRecordList{{idx}}">
					<td class="hide">
						<input id="updateWeightRecordList{{idx}}_id" name="updateWeightRecordList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="updateWeightRecordList{{idx}}_delFlag" name="updateWeightRecordList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<select id="updateWeightRecordList{{idx}}_operation" name="updateWeightRecordList[{{idx}}].operation" data-value="{{row.operation}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('weight_operation')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<textarea id="updateWeightRecordList{{idx}}_content" name="updateWeightRecordList[{{idx}}].content" rows="4"    class="form-control ">{{row.content}}</textarea>
					</td>
					
					
					<td>
						<textarea id="updateWeightRecordList{{idx}}_remarks" name="updateWeightRecordList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#updateWeightRecordList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var updateWeightRecordRowIdx = 0, updateWeightRecordTpl = $("#updateWeightRecordTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(weightRecord.updateWeightRecordList)};
					for (var i=0; i<data.length; i++){
						addRow('#updateWeightRecordList', updateWeightRecordRowIdx, updateWeightRecordTpl, data[i]);
						updateWeightRecordRowIdx = updateWeightRecordRowIdx + 1;
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