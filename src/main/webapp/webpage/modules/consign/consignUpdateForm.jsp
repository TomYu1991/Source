<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>委托单/预约单管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

	        $('#startTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#endTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/consign/consign/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="consign" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>

				<tr>
					<td class="width-15 active"><label class="pull-right">委托/预约人：</label></td>
					<td class="width-35">
						<form:input  path="consignUser" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">委托/预约部门：</label></td>
					<td class="width-35">
						<form:input  path="consignDept" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">称重类型：</label></td>
					<td class="width-35">
						<form:select   path="weightType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('weight_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">磅秤：</label></td>
					<td class="width-35">
						<form:select   path="equipNum" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('equip_num')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">品名中文：</label></td>
					<td class="width-35">
						<form:input  path="prodCname" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">牌号（钢级）：</label></td>
					<td class="width-35">
						<form:input  path="sgSign" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">供货方：</label></td>
					<td class="width-35">
						<form:input  path="supplierName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">收货方：</label></td>
					<td class="width-35">
						<form:input  path="consigneUser" htmlEscape="false"    class="form-control "/>
					</td>

				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开始时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='startTime'>
							<input type='text'  name="startTime" class="form-control "  value="<fmt:formatDate value="${consign.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">结束时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='endTime'>
							<input type='text'   name="endTime" class="form-control "  value="<fmt:formatDate value="${consign.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">被访者：</label></td>
					<td class="width-35">
						<form:input  path="dealPersonNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">被访部门：</label></td>
					<td class="width-35">
						<form:input  path="dealDept" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">车牌号：</label></td>
					<td class="width-35">
						<form:input  path="vehicleNo" htmlEscape="false"    class="form-control "/>
					</td>

					<td class="width-15 active"><label class="pull-right">来访人：</label></td>
					<td class="width-35">
						<form:input  path="userName" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">来访公司：</label></td>
					<td class="width-35">
						<form:input  path="carryCompanyName" htmlEscape="false"    class="form-control "/>
					</td>

					<td class="width-15 active"><label class="pull-right">来访人联系号码：</label></td>
					<td class="width-35">
						<form:input  path="tel" htmlEscape="false"    class="form-control "/>
					</td>

				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">高炉号：</label></td>
					<td class="width-35">
						<form:input  path="blastFurnaceNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">铁水灌号：</label></td>
					<td class="width-35">
						<form:input  path="ladleNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">委托总量：</label></td>
					<td class="width-35">
						<form:input  path="totalWt" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">确认收货标记：</label></td>
					<td class="width-35">
						<form:select   path="field1" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('sure_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">过磅状态：</label></td>
					<td class="width-35">
						<form:select   path="weightState" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('weight_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:select   path="status" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('consign_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">锁皮标记：</label></td>
					<td class="width-35">
						<form:select   path="defaultFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('default_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">单次生效标记：</label></td>
					<td class="width-35">
						<form:select   path="ponderFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('ponder_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">放行码：</label></td>
					<td class="width-35">
						<form:input  path="passCode" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">当前完成总量：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="surplusWt" htmlEscape="false"    class="form-control "/>
					</td>

				</tr>
		   <tr>
			   <td class="width-15 active"><label class="pull-right">修改原因：</label></td>
			   <td class="width-35">
				   <form:input  path="remarks" htmlEscape="false"    class="form-control required"/>
			   </td>
		   </tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>