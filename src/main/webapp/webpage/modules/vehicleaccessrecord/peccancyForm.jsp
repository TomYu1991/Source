<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>违章信息</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
            }else{
                jp.loading();
                jp.post("${ctx}/vehicleaccessrecord/vehicleAccessRecord/savePeccancyInfo",$('#inputForm').serialize(),function(data){
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
<form:form id="inputForm" modelAttribute="vehicleAccessRecord" class="form-horizontal">
    <form:hidden path="id"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right">车牌号：</label></td>
            <td class="width-35">
                <form:input path="vehicleNo" htmlEscape="false" readonly="true"  class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">违章照片：</label></td>
            <td class="width-35">
                <sys:fileUpload path="pic" fileNumLimit="1" value="${vehicleAccessRecord.pic}" type="image" uploadPath="/pic"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">违章信息：</label></td>
            <td class="width-35">
                <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="100" class="form-control required"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>