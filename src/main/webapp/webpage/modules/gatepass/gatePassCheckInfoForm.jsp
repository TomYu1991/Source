<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>宝钢德盛不锈钢有限公司物资出门证</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
	        $('#dealDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#startTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#endTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#applyTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#approveTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
        function save() {
            print()
        }
	</script>
	<script>
       /* function print() {
            var position ="E01"
            var title = "宝钢德盛不锈钢有限公司物资出门证";
            var title2 = "(废弃物类)";
            var title3 	= "";
            var time = new Date().Format("yyyy年MM月dd日");
            var printdata = "{'header':'"
                + "','header2':'"
                + "','header3':'"
                + "','companyCname':'申请单位:"
                + "','prodCname':'物资名称："
                + "','matSpecDesc':'规   格"
                + "','outStockQty':'数  量"
                +"',' measureUnit':'计量单位"
                + "','outFactory':'出厂理由"
                + "','lssueCompany':'签发单位（盖章）"
                + "','lssueDate':'签发日期"
                + "','date':'   年   月   日"
                + "','manager':'主管人"
                + "','invoice':'开票人"
                + "','takeGoods':'提货人"
                + "','doorkeeper':'门卫验收"
                + "',_file_:'passcode.xml'}";
            var s = printaa(position,printdata);*/
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="passCheck" class="form-horizontal">
		<form:hidden path="id"/>	
		<table  align="center" width="100%">
		   <thead class="table table-striped" style="border-bottom: none">
				<tr align="center">
					<td width = 10px rowspan="2"><img src=""/></td>
					<td colspan="5"  style="font-size: 20px;">宝钢德盛不锈钢有限公司物资出门证</td>
				</tr>
				<tr align="center">
					<td colspan="5" style="font-size: 20px;">(废弃物类)</td>
				</tr>
				<c:forEach items="${list}"  var="l" begin="0" end="0">
				<tr align="right">
					<td colspan="5" style="font-size: 20px;">NO:${l.passcode}</td>
				</tr>
		        </c:forEach>
		        </thead>
			    <tbody>
				<table  class="table table-bordered" align="center" width="100%">
				<c:forEach items="${list}"  var="l" begin="0" end="0">
				<tr>
					<td>申请单位</td>
					<td colspan="4">${l.companyCname}</td>
				</tr>
				</c:forEach>
				<tr>
					<td>物资名称</td>
					<td>规格</td>
					<td>数量</td>
					<td width="10%">计量单位</td>
					<td>装运车号</td>
				</tr>
				<c:forEach items="${list}"  var="l" varStatus="i">
				<tr>
					<td>${l.prodCname}</td>
					<td>${l.matSpecDesc}</td>
					<td>${l.outStockQty}</td>
					<td width="10%">${l.measureUnit}</td>
					<td>${l.vehicleNo}</td>
				</tr>
				</c:forEach>
				<tr>
					<td>出厂理由</td>
					<td colspan="4"></td>
				</tr>
				<tr>
					<td rowspan="2">签发单位（盖章）</td>
					<td colspan="2" rowspan="2"></td>
					<td colspan="2" width="10%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;签发日期</td>

				</tr>
				<tr>
					<td colspan="2" width="10%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;日</td>
				</tr>
				</table>
				</tbody>
				<tfoot>
				<table align="center" width="100%">
				<tr>
					<td>主管人</td>
					<td></td>
					<td>开票人</td>
					<td></td>
					<td>提货人</td>
					<td></td>
					<td>门卫验收</td>
					<td></td>
				</tr>
				</table>
				</tfoot>
		</table>
		<div style="height: 70px;width: 100%"></div>
	</form:form>
</body>
</html>