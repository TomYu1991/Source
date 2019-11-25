<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>磅单重量监控</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="weightMonitor.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">过磅重量监控</h3>
        </div>
        <div class="panel-body">

            <!-- 搜索 -->
            <div id="search-collapse">
                <div class="accordion-inner">
                    <form:form id="searchForm" modelAttribute="monitor" class="form form-horizontal well clearfix">
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <label class="label-item single-overflow pull-left" title="过磅工作站：">过磅工作站：</label>
                            <form:select path="workStation"  class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('workststion_ip')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <div class="form-group">
                                <label class="label-item single-overflow pull-left" title="过磅时间：">&nbsp;过磅时间：</label>
                                <div class="col-xs-12">
                                    <div class="col-xs-12 col-sm-5">
                                        <div class='input-group date' id='begintaretime' style="left: -10px;" >
                                            <input type='text'  name="begintaretime" class="form-control" id="bt" />
                                            <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-1">
                                        ~
                                    </div>
                                    <div class="col-xs-12 col-sm-5">
                                        <div class='input-group date' id='endtaretime' style="left: -10px;" >
                                            <input type='text'  name="endtaretime" class="form-control" id="et"/>
                                            <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <form:hidden path="searchFlag" htmlEscape="false" class=" form-control" id="searchFlag" />
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <div style="margin-top:26px">
                                <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                                <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
            <!-- 图表展示 -->
            <div id="main" style="width:1050px;height:400px;"></div>

        </div>
    </div>
</div>

<script src="/static/plugin/echarts3/echarts.min.js"></script>
<script>
    $("#search").click("click", function() {// 绑定查询按扭
        $("#searchFlag").val("1");
        var d=$("#searchForm").serializeJSON();
        console.log(d)
        if(d.workStation==null || d.workStation==""){
            jp.error("请选择地磅！");
        }else{
            console.log(d.workStation);
            jp.post("${ctx}/weightmonitor/weightmonitor/queryWeightData",d,function(data){
                if(data.success){
                    var respData = data.data;
                    console.log(respData);
                    var startTime = $("#bt").val();
                    var endTime = $("#et").val();
                    console.log(startTime);
                    console.log(endTime);
                    var myChart = echarts.init(document.getElementById("main"));
                    option = {
                        title : {
                            text : '过磅重量轨迹图',
                            subtext : 'dataZoom支持'
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter : function (params) {
                                var date = new Date(params.value[0]);
                                var time = date.getHours()+'时'+date.getMinutes()+'分'+date.getSeconds()+'秒';
                                // return date.getDate()+'/'+(date.getMonth()+1)+'/'+date.getFullYear()+':'+params.value[1];
                                return "车号："+params.name+",重量："+params.value[1]+"kg"+",时间："+time;
                            }
                        },
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                dataView : {show: true, readOnly: false},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        dataZoom: {
                            show: true,
                            start : 70
                        },
                        legend : {
                            data : ['series1']
                        },
                        grid: {
                            y2: 80
                        },
                        xAxis : [
                            {
                                type : 'time',
                                splitNumber:10
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value'
                            }
                        ],
                        series : [
                            {
                                //name: 'series1',
                                type: 'line',
                                showAllSymbol: true,
                                symbolSize: function (value){
                                    return Math.round(value[1]/20000);
                                },
                                data:respData
                            }
                        ]
                    };

                    myChart.setOption(option);
                }else{
                    jp.error(data.msg);
                }
            });
        }

    });
</script>

<script >

    $(function(){
        var divel =  $("<div style='display:inline-block;position:fixed;bottom:0;left:0;width:100%;height:20px;background:#fff;overflow:auto;' ><div style='display:inline-block;height:30px;'></div></div>").appendTo("body");

        $(".fixed-table-body").css("overflow-x","hidden");
        $(divel).find(">div").width( $(".fixed-table-body>table" ).width() + $(".fixed-table-body").width());
        $(divel).scroll(function(){
            $(".fixed-table-body").scrollLeft($(this).scrollLeft());
        });
        $(".fixed-table-body").resize(function(){
            $(divel).find(">div").width( $(".fixed-table-body>table" ).width() + $(".fixed-table-body").width());
        });
    })


</script>
</body>
</html>