<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>作废磅单</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
            }else{
                jp.loading();
                jp.post("${ctx}/weight/weight/cancel",$('#inputForm').serialize(),function(data){
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
<form:form id="inputForm" modelAttribute="weight" action="${ctx}/weight/weight/cancel" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <table class="table table-bordered">
        <tbody>

        <tr>
            <td class="width-15 active"><label class="pull-right">磅单号：</label></td>
            <td class="width-35">
                <form:input path="weighNo" readonly="true" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">车牌号：</label></td>
            <td class="width-35">
                <form:input path="vehicleNo" readonly="true" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">铁水罐号：</label></td>
            <td class="width-35">
                <form:input path="ladleNo" readonly="true" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">品名中文：</label></td>
            <td class="width-35">
                <form:input path="prodCname" readonly="true" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">作废原因：</label></td>
            <td class="width-35">
                <form:textarea  path="remarks"  htmlEscape="false" rows="4"     class="form-control required " />
            </td>
        </tr>

        </tbody>
    </table>

</form:form>
</body>
</html>