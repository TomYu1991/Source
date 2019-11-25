<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>点检表管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function() {

            $('#checkDate').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
        });
        function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
            }else{
                jp.loading();
                jp.post("${ctx}/checkall/checkes/save",$('#inputForm').serialize(),function(data){
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
<form:form id="inputForm" modelAttribute="checkes" class="form-horizontal">
    <form:hidden path="id"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right">点检标准号：</label></td>
            <td class="width-35">
                <form:input path="checkNumber" htmlEscape="false"  readonly="true"  class="form-control  isIntGteZero"/>
            </td>
            <td class="width-15 active"><label class="pull-right">设备名称：</label></td>
            <td class="width-35">
                <sys:gridselect url="${ctx}/checkequipment/equipment/data"    id="equipment" name="equipment.id" value="${checkes.equipment.id}" labelName="equipment.equipment" labelValue="${checkes.equipment.equipment}"
                                title="选择设备名称" cssClass="form-control " fieldLabels="设备名称" fieldKeys="equipment" searchLabels="设备名称" searchKeys="equipment" ></sys:gridselect>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">点检人：</label></td>
            <td class="width-35">
                <form:input path="checkInspector.id" htmlEscape="false"  readonly="true"  class="form-control "/>
                    <%--<sys:userselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="office.deputyPerson.name" labelValue="${office.deputyPerson.name}" cssClass="form-control" isMultiSelected="false"/>--%>
            </td>
            <td class="width-15 active"><label class="pull-right">点检日期：</label></td>
            <td class="width-35">
                <div class='input-group form_datetime' id='checkDate'>
                    <input type='text'  name="checkDate" class="form-control " readonly="true" value="<fmt:formatDate value="${checkes.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                    <span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">点检内容：</label></td>
            <td class="width-35">
                <form:input path="checkContect" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">点检结果：</label></td>
            <td class="width-35">
                <form:select path="checkResult" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('dic_check_result')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">点检状态：</label></td>
            <td class="width-35">
                <form:select path="checkState" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('dic_check_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15 active"><label class="pull-right">审核人：</label></td>
            <td class="width-35">
                <form:input path="checkUsage.id" htmlEscape="false"    class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">备注：</label></td>
            <td class="width-35">
                <form:input path="remarks" htmlEscape="false"    class="form-control "/>
            </td>
            <td class="width-15 active"></td>
            <td class="width-35" ></td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>