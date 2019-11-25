<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>磅单管理管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
        var pic;//用于接收弹出图片的url
		$(document).ready(function() {
	        $('#taretime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#grosstime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });

		});
        /*function img(url){
            alert(url);
            var img_infor = "<img src='" + url + "' />";
            layer.open({
                type: 1,
                closeBtn: 1,
                shade: false,
                title: false, //不显示标题
                //skin: 'layui-layer-nobg', //没有背景色
                shadeClose: false,
                area:['auto','auto'],
                //area: [img.width + 'px', img.height+'px'],    
                content: img_infor //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响    
                //cancel: function () {    
                //layer.msg('图片查看结束！', { time: 5000, icon: 6 });    
                //}    
            });
		}*/
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
						<form:input path="consignId" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">磅单号：</label></td>
					<td class="width-35">
						<form:input path="weighNo" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
				</tr>
				<tr>

					<td class="width-15 active"><label class="pull-right">称重类型：</label></td>
					<td class="width-35">
						<form:select path="weightType" class="form-control" disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('weight_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">序号：</label></td>
					<td class="width-35">
						<form:input path="seqNo" htmlEscape="false" disabled="true" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">品名中文：</label></td>
					<td class="width-35">
						<form:input path="prodCname" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">品名代码：</label></td>
					<td class="width-35">
						<form:input path="prodCode" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">材料重量（净重）：</label></td>
					<td class="width-35">
						<form:input path="matWt" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">材料规格描述：</label></td>
					<td class="width-35">
						<form:input path="matSpecDesc" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">车船号：</label></td>
					<td class="width-35">
						<form:input path="vehicleNo" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">称重标记：</label></td>
					<td class="width-35">
						<form:select path="weightFlag" disabled="true" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('weight_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">单据号：</label></td>
					<td class="width-35">
						<form:input path="billNo" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">炉批号：</label></td>
					<td class="width-35">
						<form:input path="ponoLotNo" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">高炉号：</label></td>
					<td class="width-35">
						<form:input path="blastFurnaceNo" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">铁水罐号：</label></td>
					<td class="width-35">
						<form:input path="ladleNo" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">装卸料点：</label></td>
					<td class="width-35">
						<form:input path="warehouseno" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>过磅属性：</label></td>
					<td class="width-35">
						<form:select path="dispatchtype" class="form-control required" disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('dispatch_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">过皮时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='taretime'>
							<input type='text'  name="taretime" class="form-control " disabled="true" value="<fmt:formatDate value="${weight.taretime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">过毛时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='grosstime'>
							<input type='text'  name="grosstime" class="form-control " disabled="true" value="<fmt:formatDate value="${weight.grosstime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">子项号：</label></td>
					<td class="width-35">
						<form:input path="subNo" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">处理人员工号：</label></td>
					<td class="width-35">
						<form:input path="dealPersonNo" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">设备编码：</label></td>
					<td class="width-35">
						<form:input path="equipNum" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">客户代码：</label></td>
					<td class="width-35">
						<form:input path="customerCode" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">材料件数（根数）：</label></td>
					<td class="width-35">
						<form:input path="matNum" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">材料毛重：</label></td>
					<td class="width-35">
						<form:input path="matGrossWt" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">杂重（皮重）：</label></td>
					<td class="width-35">
						<form:input path="impWt" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control" disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('cancel_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">收货客户：</label></td>
					<td class="width-35">
						<form:input path="consigneUser" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">供应商名称：</label></td>
					<td class="width-35">
						<form:input path="supplierName" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">成品包装材料重量：</label></td>
					<td class="width-35">
						<form:input path="productPackWt" htmlEscape="false" disabled="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">默认标记：</label></td>
					<td class="width-35">
						<form:select path="defaultFlag" class="form-control" disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('default_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">备注：</label></td>
                    <td class="width-35">
                        <form:input path="remarks" htmlEscape="false" disabled="true" class="form-control "/>
                    </td>
                    <td class="width-15 active"><label class="pull-right">一次过磅工作站：</label></td>
                    <td class="width-35">
                        <form:input path="fistStation" htmlEscape="false" disabled="true" class="form-control "/>
                    </td>
                </tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">二次过磅工作站：</label></td>
                    <td class="width-35">
                        <form:input path="secondStation" htmlEscape="false" disabled="true" class="form-control "/>
                    </td>
                    <td class="width-15 active"><label class="pull-right">票据截图：</label></td>
                    <td class="width-35">
                        <img id="bill" src="${weight.billPic}" height="5%">
                    </td>
                </tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">一次过磅抓拍：</label></td>
                    <td class="width-35">
                        <img src="${weight.tareHeadPic}" height="5%">
                    </td>
                    <td class="width-15 active"><label class="pull-right">一次过磅抓拍：</label></td>
                    <td class="width-35">
                        <img src="${weight.tareTailPic}" height="5%">
                    </td>
                </tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">一次过磅抓拍：</label></td>
                    <td class="width-35">
                        <img src="${weight.tareTopPic}" height="5%">
                    </td>
                    <td class="width-15 active"><label class="pull-right">二次过磅抓拍：</label></td>
                    <td class="width-35">
                        <img src="${weight.grossHeadPic}" height="5%">
                    </td>
                </tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">二次过磅抓拍：</label></td>
                    <td class="width-35">
                        <img src="${weight.grossTailPic}" height="5%">
                    </td>
                    <td class="width-15 active"><label class="pull-right">二次过磅抓拍：</label></td>
                    <td class="width-35">
                        <img src="${weight.grossTopPic}" height="5%">
                    </td>
                </tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>