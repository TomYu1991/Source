<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>磅单管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

	        $('#taretime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#grosstime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/weight/weight/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="weight" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">委托单号：</label></td>
					<td class="width-35">
						<form:input path="consignId" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">磅单号：</label></td>
					<td class="width-35">
						<form:input path="weighNo" htmlEscape="false" disabled="true"    class="form-control "/>
					</td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">称重类型：</label></td>
					<td class="width-35">
						<form:select path="weightType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('weight_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>

					<td class="width-15 active"><label class="pull-right">品名中文：</label></td>
					<td class="width-35">
						<form:input path="prodCname" htmlEscape="false"    class="form-control "/>
					</td>

				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">打印次数：</label></td>
					<td class="width-35">
						<form:input path="printNum" htmlEscape="false"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">磅秤：</label></td>
					<td class="width-35">
						<form:select path="equipNum" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('equip_num')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>

				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">炉批号：</label></td>
					<td class="width-35">
						<form:input path="ponoLotNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">高炉号：</label></td>
					<td class="width-35">
						<form:input path="blastFurnaceNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
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
					<td class="width-15 active"><label class="pull-right">一次过磅时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='grosstime'>
							<input type='text'  name="grosstime" class="form-control "  value="<fmt:formatDate value="${weight.grosstime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">二次过磅时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='taretime'>
							<input type='text'  name="taretime" class="form-control "  value="<fmt:formatDate value="${weight.taretime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>

				<tr>

					<td class="width-15 active"><label class="pull-right">材料重量（净重）：</label></td>
					<td class="width-35">
						<form:input path="matWt" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">材料毛重：</label></td>
					<td class="width-35">
						<form:input path="matGrossWt" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">杂重（皮重）：</label></td>
					<td class="width-35">
						<form:input path="impWt" htmlEscape="false"    class="form-control required "/>
					</td>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('weigh_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货客户：</label></td>
					<td class="width-35">
						<form:input path="consigneUser" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">供应商名称：</label></td>
					<td class="width-35">
						<form:input path="supplierName" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">锁皮标记：</label></td>
					<td class="width-35">
						<form:select path="defaultFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('default_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35">
						<form:input path="remarks" htmlEscape="false"  class="form-control "/>
					</td>

				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">过磅标记：</label></td>
					<td class="width-35">
						<form:select path="weightFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('weight_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>


				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>