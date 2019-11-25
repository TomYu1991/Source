<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="属性名"%>
<%@ attribute name="txtName" type="java.lang.String" required="false" description="属性名"%>
<%@ attribute name="sqlid" type="java.lang.String" required="true" description="查找的Sql"%>
<%@ attribute name="cssClass" type="java.lang.String" required="true" description="css样式"%>
<%@ attribute name="conditionId" type="java.lang.String" required="false" description="需要级联查询的ID"%>
<%@ attribute name="info" type="java.lang.String" required="false" description="需要提示的信息"%>
<%@ attribute name="value" type="java.lang.String" required="false" description="隐藏域值（ID）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="false" description="输入框值（Name）"%>
<%@ attribute name="readonly" type="java.lang.Boolean" required="false" description="文本框可填写"%>
<%@ attribute name="onchange" type="java.lang.String" required="false" description="onchange方法"%>
<%@ attribute name="onblur" type="java.lang.String" required="false" description="onblur方法"%>

<input id="${id}" name="${name}" type="hidden" value="${value}"/>
<input id="${id}_txt" name="${txtName}" autocomplete="off" type="text" class="${cssClass}" value="${labelValue}" ${readonly?'readonly="readonly"':''} placeholder="请输入车号"/>

<script type="text/javascript">
    $(document).ready(function(){
        var condition="";
        var objects = {};
        $("#${id}_txt").typeahead({
            source: function(query, process) { //query是输入框输入的文本内容, process是一个回调函数
                if("${conditionId}" !=null && "${conditionId}" !=""){
                    if($("#${conditionId}Id").val()==""){
                        jp.warning("请先选择${info}!");
                        return true;
                    }
                    condition= $("#${conditionId}Id").val();
                }
                $.post("${pageContext.request.contextPath}/widget/autocomplete/list", {sqlid: "${sqlid}", keyvalue:query, condition:condition}, function(data) {
                    if (data == "" || data.length == 0) {
                        return;
                    };
                    var results = [];
                    for (var i = 0; i < data.length; i++) {
                        objects[data[i].VALUE] = data[i].ID;
                        results.push(data[i].VALUE);
                    }
                    process(results);
                });
            },
            afterSelect: function (item) {       //选择项之后的事件，item是当前选中的选项
//                console.log(item);
                $("#${id}").val(objects[item]); //为隐藏输入框赋值
                  if("${onchange}" !=null && "${onchange}" !=""){
                    ${onchange};
                  }
                if("${onblur}" !=null && "${onblur}" !=""){
                    ${onblur};
                }

            },
            matcher: function (item) {
                return true;
            },
        });
    })
</script>
