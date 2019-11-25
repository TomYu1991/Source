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
					<td class="width-15 active"><label class="pull-right">类别代码：</label></td>
					<td class="width-35">
						<form:select disabled="true"  path="type" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('consign_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">委托/预约单号：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="consignId" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">委托/预约人：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="consignUser" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">委托/预约部门：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="consignDept" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">称重类型：</label></td>
					<td class="width-35">
						<form:select disabled="true"  path="weightType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('weight_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">磅秤：</label></td>
					<td class="width-35">
						<form:select disabled="true"  path="equipNum" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('equip_num')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">品名代码：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="prodCode" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">品名中文：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="prodCname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">牌号代码：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="sgCode" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">牌号（钢级）：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="sgSign" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">材料规格描述：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="matSpecDesc" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">单据号：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="billNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开始时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='startTime'>
							<input type='text' disabled="true"   name="startTime" class="form-control "  value="<fmt:formatDate value="${consign.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">结束时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='endTime'>
							<input type='text' disabled="true"   name="endTime" class="form-control "  value="<fmt:formatDate value="${consign.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">总重量：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="totalWt" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">供货方：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="supplierName" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货方：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="consigneUser" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">来访事由：</label></td>
					<td class="width-35">
						<form:textarea disabled="true"  path="content" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">被访者：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="dealPersonNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">被访部门：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="dealDept" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">被访者联系号码：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="telNum" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">车牌号：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="vehicleNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">来访人：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="userName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">来访公司：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="carryCompanyName" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">来访人联系号码：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="tel" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">放行码：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="passCode" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">高炉号：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="blastFurnaceNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">铁水灌号：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="ladleNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">运输联系人电话：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="transContactPersonTel" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">公司中文名称：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="companyCname" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">运输联系人：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="transContactPerson" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">客户供应商身份证号：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="IDCard" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">RFID卡号：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="rfidNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">当前完成总量：</label></td>
					<td class="width-35">
						<form:input disabled="true" path="surplusWt" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>