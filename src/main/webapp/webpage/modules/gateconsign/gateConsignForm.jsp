<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>门岗管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function () {

            $('#startTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#endTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
        });

        function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if (!isValidate) {
                return false;
            } else {
                jp.loading();
                jp.post("${ctx}/consign/consign/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        jp.getParent().refresh();
                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                        parent.layer.close(dialogIndex);
                        jp.success(data.msg)

                    } else {
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
            <td class="width-15 active"><label class="pull-right">类型：</label></td>
            <td class="width-35">
                <input type="text" value="${consign.typeName}" readonly="true" class="form-control ">
            </td>
            <td class="width-15 active"><label class="pull-right">委托/预约单号：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="consignId" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">委托/预约人：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="consignUser" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">委托/预约部门：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="consignDept" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">称重类型：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="weightType" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">设备编码：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="equipNum" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">品名代码：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="prodCode" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">品名中文：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="prodCname" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">牌号代码：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="sgCode" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">牌号（钢级）：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="sgSign" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">材料规格描述：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="matSpecDesc" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">单据号：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="billNo" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">开始时间：</label></td>
            <td class="width-35">
                <input class="form-control " readonly="true" type="text"
                       value="<fmt:formatDate value="${consign.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />">
            </td>
            <td class="width-15 active"><label class="pull-right">结束时间：</label></td>
            <td class="width-35">
                <input class="form-control " readonly="true" type="text"
                       value="<fmt:formatDate value="${consign.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />">
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">材料件数(根数)：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="matNum" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">总重量：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="totalWt" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">溢装：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="moreRate" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">供货方：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="supplierName" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">收货方：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="consigneUser" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">船号：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="shipNo" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">退货描述：</label></td>
            <td class="width-35">
                <form:textarea readonly="true" path="returnDesc" htmlEscape="false" rows="4" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">来访事由：</label></td>
            <td class="width-35">
                <form:textarea readonly="true" path="content" htmlEscape="false" rows="4" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">被访者：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="dealPersonNo" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">被访部门：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="dealDept" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">被访者联系号码：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="telNum" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">车牌号：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="vehicleNo" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">来访人：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="userName" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">来访公司：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="carryCompanyName" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">来访人联系号码：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="tel" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">组批代码：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="groupCode" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">放行码：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="passCode" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">高炉号：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="blastFurnaceNo" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">铁水灌号：</label></td>
            <td class="width-35">
                <form:input readonly="true" path="ladleNo" htmlEscape="false" class="form-control "/>
            </td>

        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>