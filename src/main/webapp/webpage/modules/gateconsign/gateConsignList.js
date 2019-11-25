<%@ page contentType="text/html;charset=UTF-8"%>
    <script>

var shuakavehicleNo = "";
var shuakarfidNo= "";

var shuakavehicleNo3 = "";
var shuakarfidNo3= "";
var nowRFID1="";
var nowRFID3="";
var isled=false;
var ishld=false;
var nowVehicleNo="";
var oldVehicleNo="";
$(function(){
    jp.get("${ctx}/checkAddr", function (data) {
        if(data.success){
            //一号岗亭
            if(data.object==4){
                isled=true;
                console.log("一号岗亭")
                watchCar1();
                openCardEntrance();
            }
            //五号岗亭
            if(data.object==10){
                console.log("五号岗亭")
                binding1();
                watchCar1();
                openCardEntrance();
            }
            //三号岗亭
            if(data.object==9){
                console.log("三号岗亭")
                binding3();
                binding1();
                watchCar1();
                watchCar3();
                openCardEntrance();
                openCardEntrance3();
                ishld=true;
            }
            //六号岗亭
            if(data.object==7){
                isled=true;
                console.log("六号岗亭")
                //人行
                lhrxdz();
                watchCar1();
                binding1();
                openCardEntrance();
            }
        }
    });
})

$(document).ready(function() {
    //监控点通知
    info();

    //列表
        $('#consignTable').bootstrapTable({

            //请求方法
            method: 'post',
            //类型json
            dataType: "json",
            contentType: "application/x-www-form-urlencoded",
            //显示检索按钮
            showSearch: true,
            //显示刷新按钮
            showRefresh: true,
            //显示切换手机试图按钮
            showToggle: true,
            //显示 内容列下拉框
            showColumns: true,
            //显示到处按钮
            showExport: true,
            //显示切换分页按钮
            showPaginationSwitch: true,
            //最低显示2行
            minimumCountColumns: 2,
            //是否显示行间隔色
            striped: true,
            //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            cache: false,
            //是否显示分页（*）
            pagination: true,
            //排序方式
            sortOrder: "asc",
            //初始化加载第一页，默认第一页
            pageNumber:1,
            //每页的记录行数（*）
            pageSize: 10,
            //可供选择的每页的行数（*）
            pageList: [10, 25, 50, 100],
            //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
            url: "${ctx}/gateconsign/consign/data",
            //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
            //queryParamsType:'',
            ////查询参数,每次调用是会带上这个参数，可自定义
            queryParams : function(params) {
                var searchParam = $("#searchForm").serializeJSON();
                searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
                searchParam.pageSize = params.limit === undefined? -1 : params.limit;
                searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                return searchParam;
            },
            //分页方式：client客户端分页，server服务端分页（*）
            sidePagination: "server",
            contextMenuTrigger:"right",//pc端 按右键弹出菜单
            contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
            contextMenu: '#context-menu',
            onContextMenuItem: function(row, $el){
                if($el.data("item") == "edit"){
                    edit(row.id);
                }else if($el.data("item") == "view"){
                    view(row.id);
                } else if($el.data("item") == "delete"){
                    jp.confirm('确认要删除该委托单/预约单记录吗？', function(){
                        jp.loading();
                        jp.get("${ctx}/consign/consign/delete?id="+row.id, function(data){
                            if(data.success){
                                $('#consignTable').bootstrapTable('refresh');
                                jp.success(data.msg);
                            }else{
                                jp.error(data.msg);
                            }
                        })

                    });

                }
            },

            onClickRow: function(row, $el){
            },
            onShowSearch: function () {
                $("#search-collapse").slideToggle();
            },
            columns: [{
                checkbox: true

            }
                ,{
                    field: 'type',
                    title: '类别代码',
                    sortable: true,
                    sortName: 'type',
                    formatter:function(value, row , index){
                        value = jp.getDictLabel(${fns:toJson(fns:getDictList('consign_type'))}, value, "-");
                    <c:choose>
                        <c:when test="${fns:hasPermission('consign:consign:edit')}">
                        return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
                    </c:when>
                        <c:when test="${fns:hasPermission('consign:consign:view')}">
                        return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
                    </c:when>
                        <c:otherwise>
                        return value;
                    </c:otherwise>
                        </c:choose>
                    }
                }
                ,{
                    field: 'consignId',
                    title: '委托/预约单号',
                    sortable: true,
                    sortName: 'consignId'
                }
                ,{
                    field: 'startTime',
                    title: '开始时间',
                    sortable: true,
                    sortName: 'startTime'

                }
                ,{
                    field: 'endTime',
                    title: '结束时间',
                    sortable: true,
                    sortName: 'endTime'

                },{
                    field: 'status',
                    title: '状态',
                    sortable: true,
                    sortName: 'status',
                    formatter:function(value, row , index){
                        console.log(row);
                        value = jp.getDictLabel(${fns:toJson(fns:getDictList('consign_status'))}, value, "-");
                        return value;
                    }
                }
                ,{
                    field: 'vehicleNo',
                    title: '车牌号',
                    sortable: true,
                    sortName: 'vehicleNo'

                },{
                    field: 'consignDept',
                    title: '部门',
                    sortable: true,
                    sortName: 'consignDept'

                }
                ,{
                    field: 'prodCname',
                    title: '品名',
                    sortable: true,
                    sortName: 'prodCname'

                }, {
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    events: {
                        'click .view': function (e, value, row, index) {
                            view(row.id);
                        },

                    },
                    formatter:  function operateFormatter(value, row, index) {
                        return [

                            <shiro:hasPermission name="gateconsign:gateconsign:view">
                            '<a href="#" class="view" title="详情" >[详情] </a>'
                            </shiro:hasPermission>

                    ].join('');
                    }
                }
            ]

        });


        if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


            $('#consignTable').bootstrapTable("toggleView");
        }

        $('#consignTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
            'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#consignTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#consignTable').bootstrapTable('getSelections').length!=1);
            $('#openGate').prop('disabled', $('#consignTable').bootstrapTable('getSelections').length!=1);
            $('#closeGate').prop('disabled', $('#consignTable').bootstrapTable('getSelections').length!=1);
        });

        $("#btnImport").click(function(){
            jp.open({
                type: 2,
                area: [500, 200],
                auto: true,
                title:"导入数据",
                content: "${ctx}/tag/importExcel" ,
                btn: ['下载模板','确定', '关闭'],
                btn1: function(index, layero){
                    jp.downloadFile('${ctx}/consign/consign/import/template');
                },
                btn2: function(index, layero){
                    var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    iframeWin.contentWindow.importExcel('${ctx}/consign/consign/import', function (data) {
                        if(data.success){
                            jp.success(data.msg);
                            refresh();
                        }else{
                            jp.error(data.msg);
                        }
                        jp.close(index);
                    });//调用保存事件
                    return false;
                },

                btn3: function(index){
                    jp.close(index);
                }
            });
        });


        $("#export").click(function(){//导出Excel文件
            jp.download('${ctx}/consign/consign/export',JSON.stringify($("#searchForm").serializeJSON()));

        });


        $("#search").click("click", function() {// 绑定查询按扭
            $("#searchFlag").val("1");
            $('#consignTable').bootstrapTable('refresh');
        });

        $("#reset").click("click", function() {// 绑定查询按扭
            $("#searchForm  input").val("");
            $("#searchForm  select").val("");
            $("#searchForm  .select-item").html("");
            $('#consignTable').bootstrapTable('refresh');
        });

        $('#startTime').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
        $('#endTime').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
    });

function getIdSelections() {
    return $.map($("#consignTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll(){

    jp.confirm('确认要删除该委托单/预约单记录吗？', function(){
        jp.loading();
        jp.get("${ctx}/consign/consign/deleteAll?ids=" + getIdSelections(), function(data){
            if(data.success){
                $('#consignTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })
}

//刷新列表
function refresh(){
    $('#consignTable').bootstrapTable('refresh');
}

function view(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
    jp.openViewDialog('查看委托单/预约单', "${ctx}/consign/consign/form?id=" + id, '800px', '500px');
}

function binding1(){
    //打开RFID读卡器301

    device.RFID.open({
        position:"301",
        //isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            //打开rfid
            var dkrfiddata = eval("("+data+")");
            //返回rfid状态
            var dkrfid = dkrfiddata.Data;
            if("2"==dkrfid || "1"==dkrfid){
                //获取数据
                device.RFID.getdata({
                    position:"301",
                    isclose:false,	//执行完毕后不关闭连接
                    count:0,	//获取数据次数，0 为一直获取
                    interval:1000,	//获取数据时间间隔(1000毫秒)
                    data:"{istid:true}",
                    callback:function(sjdata,ws){
                        //获取rfid
                        var data = eval("("+sjdata+")");
                        //返回rfid数据
                        var rfid = data.Data;
                        nowRFID1=rfid;
                        console.log("读到RFID1:" + rfid)
                        if(typeof rfid != "undefined" && rfid != "" && rfid != "-1" && rfid != "1" && rfid != "2") {
                                consignplate("",rfid);
                        }
                    }
                });
            }else{
                console.log("RFID:"+dkrfiddata.Describe+"异常RFID301");
            }
        }
    });
}

function binding3(){
    //打开RFID读卡器301
    device.RFID.open({
        position:"303",
        //isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            //打开rfid
            var dkrfiddata = eval("("+data+")");
            //返回rfid状态
            var dkrfid = dkrfiddata.Data;
            if("2"==dkrfid || "1"==dkrfid){
                //获取数据
                device.RFID.getdata({
                    position:"303",
                    isclose:false,	//执行完毕后不关闭连接
                    count:0,	//获取数据次数，0 为一直获取
                    interval:1000,	//获取数据时间间隔(1000毫秒)
                    data:"{istid:true}",
                    callback:function(sjdata,ws){
                        //获取rfid
                        var data = eval("("+sjdata+")");
                        //返回rfid数据
                        var rfid = data.Data;
                        nowRFID3=rfid;
                        console.log("读到RFID3:" + rfid)
                        if(typeof rfid != "undefined" && rfid != "" && rfid != "-1" && rfid != "1" && rfid != "2") {
                                consignplate3("",rfid);
                        }
                    }
                });
            }else{
                console.log("RFID:"+dkrfiddata.Describe+"异常RFID303");
            }
        }
    });
}

function watchCar1(){
    device.CarLicence.open({position:"A01",callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
            device.CarLicence.monitor({position:"A01",isclose:false, interval:200,count:0,
                callback:function(data,ws){
                    var data = eval("("+data+")");
                    var val = data.Data;
                    // value 识别内容
                    console.log("车号识别1"+val);
                    if(val.length>3&&(nowRFID1==-1||nowRFID1=="") ){
                        val = val.substring(val.lastIndexOf("#") + 1, val.length+1);
                            consignplate(val,"");

                    }else{
                        if(isled){
                            fsLED1("402","");
                        }
                        if(ishld){
                            console.log("车号识别1变红"+val);
                            setRG_R2('901', '1#OFF');
                            setRG_R2('901', '2#OFF');
                        }

                    }
                }
            })
        }
    });

}
var oldcar1="";
var oldcar3="";
function watchCar3(){
    device.CarLicence.open({position:"A03",callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
            device.CarLicence.monitor({position:"A03",isclose:false, interval:200,count:0,
                callback:function(data,ws){
                    var data = eval("("+data+")");
                    var val = data.Data;
                    // value 识别内容
                    console.log("车号识别3"+val);
                    if(val.length>3&&(nowRFID3==-1||nowRFID3=="")){
                        val = val.substring(val.lastIndexOf("#") + 1, val.length+1);
                        consignplate3(val,"");

                    }else{

                        if(ishld) {
                            console.log("车号识别3变红" + val);
                            setRG_R2('901', '1#OFF');
                            setRG_R2('901', '2#OFF');
                        }
                    }
                }
            })
        }
    });

}
/* 识别车牌   目前支持的设备: 臻识,海康*/
/*出门 识别车牌和抓拍*/
//A01、701
function consignplate(vehicleNo,rfid){
    if (localStorage.getItem(vehicleNo) != null) {
        var time = localStorage.getItem(vehicleNo);
        var times = Date.now() - time;
        if (times < 1000) {
            return;
        }

        device.GATEWAY.up({
            position:"701",
            callback: function (da, ws) {
                console.log("1、升道闸--------------------------------------------701 新逻辑" + Date.now())
                localStorage.setItem(vehicleNo, Date.now());

                //isled= true时 清空LED
                if(isled) {
                    setTimeout(function () {
                        if (localStorage.getItem(vehicleNo) != null) {
                            var time2 = localStorage.getItem(vehicleNo);
                            var times2 = Date.now() - time2;
                            if (times2 > 2000) {
                                console.log("2、清除进门LED------------------------------------------402 新逻辑"+ Date.now());
                                fsLED1("402", "");
                            }
                        }
                    }, 3000);
                }
            }
        });
        return;
    } else if(localStorage.getItem(vehicleNo+"false") != null) {

        var time = localStorage.getItem(vehicleNo+"false");
        var times = Date.now() - time;
        if (times < 1000) {
            return;
        }
        localStorage.setItem(vehicleNo+"false", Date.now());
        //isled= true时 清空LED
        console.log("1、进门LED，无预约"+vehicleNo + "---------------------------402 新逻辑"+ Date.now())
        if(isled){
            setTimeout(function () {
                if (localStorage.getItem(vehicleNo+"false") != null) {
                    var time2 = localStorage.getItem(vehicleNo+"false");
                    var times2 = Date.now() - time2;

                    if (times2 > 2000) {
                        console.log("2、清除进门LED无预约------------------------------------------402 新逻辑"+ Date.now());
                        fsLED1("402", "");
                    }
                }
            },3000);
        }
        return;
    }else{
        console.log("0、新车进门 "+vehicleNo + "---------------------------701 新逻辑"+ Date.now())

    }
    console.log("A01---301---701");
    console.log("车号"+vehicleNo+"----"+rfid);
    shuakavehicleNo=vehicleNo;
    shuakarfidNo=rfid;
        //解析车牌号
   jp.get("${ctx}/gateconsign/consign/checkConsign?vehicleNo="+ vehicleNo+"&rfidNo="+rfid,function (data) {

       if (data.success){
           shuakavehicleNo=data.object;
            if(data.errorCode=="10"){

                if(isled){
                    if (localStorage.getItem(data.object) != null) {
                        var time = localStorage.getItem(data.object);
                        var times = Date.now() - time;
                        if (times < 1000) {
                            return;
                        }else{
                            localStorage.clear();
                        }
                    } else {
                        localStorage.setItem(data.object, Date.now());
                        console.log("进门LED"+data.msg)
                        fsLED1("402", data.msg);
                    }
                }
                device.GATEWAY.up({
                    position:"701",
                    callback: function (da, ws) {
                        console.log("升道闸--------------------------------------------701");
                        if(isled) {
                            setTimeout(function () {
                                // console.log("清除进门LED------------------------------------------公务车");
                                // fsLED1("402", "");
                            }, 2000);
                        }
                    }
                });

            }else{
                if (localStorage.getItem(data.object) != null) {
                    var time = localStorage.getItem(data.object);
                    var times = Date.now() - time;
                    if (times < 1000) {
                        return;
                    }else{
                        localStorage.clear();
                    }
                } else {
                    localStorage.setItem(data.object, Date.now());

                    if(isled){
                        console.log("进门LED"+data.msg)
                        fsLED1("402", data.msg);
                    }
                    device.GATEWAY.up({
                        position:"701",
                        callback: function (da, ws) {
                            console.log("升道闸--------------------------------------------701");
                            if(isled) {
                                setTimeout(function () {
                                    // console.log("清除进门LED------------------------------------------自动放行");
                                    // fsLED1("402", "");
                                }, 2000);
                            }
                        }
                    });
                }
            }
           console.log("道闸"+new Date());
           if(oldcar1!=shuakavehicleNo){
               oldcar1=shuakavehicleNo;
               //保存车辆记录
               jp.get("${ctx}/gateconsign/consign/saveVehicleIn?vehicleNo=" + shuakavehicleNo, function (data) {
               });
           }
           if(ishld) {
               setRG_G2("901", "1#ON");
               setRG_G2("901", "2#ON");
           }

       } else {

           localStorage.setItem(vehicleNo+"false", Date.now());
           console.log("进门LED，无预约"+data.msg)
           if(isled){
               console.log("进门LED"+data.msg)
               fsLED1("402", data.msg);
               setTimeout(function () {
                   console.log("清除进门LED------------------------------------------无预约");
                   fsLED1("402", "");
               },4000);
           }
           if(ishld) {
               setRG_R2("901", "1#OFF");
               setRG_R2("901", "2#OFF");
           }
       }
    });


}
//A03、703
function consignplate3(vehicleNo,rfid){
    console.log("A03---303---703");
    console.log(vehicleNo+"----"+rfid);
    shuakavehicleNo3=vehicleNo;
    shuakarfidNo3=rfid;
    //解析车牌号
    jp.get("${ctx}/gateconsign/consign/checkConsign?vehicleNo="+ vehicleNo+"&rfidNo="+rfid,function (data) {

        if (data.success) {
            if (localStorage.getItem(data.object) != null) {
                var time = localStorage.getItem(data.object);
                var times = Date.now() - time;
                if (times < 1000) {
                    return;
                }else{
                    localStorage.clear();
                }
            } else {
                localStorage.setItem(data.object, Date.now());
                device.GATEWAY.up({
                    position:"703",
                    callback: function (da, ws) {
                        console.log("升道闸--------------------------------------703");
                    }
                });
            }
            if(ishld) {
                setRG_G2("901", "1#ON");
                setRG_G2("901", "2#ON");
            }
                if(oldcar3!=shuakavehicleNo3){
                    oldcar3=shuakavehicleNo3;
                    //保存车辆记录
                    jp.get("${ctx}/gateconsign/consign/saveVehicleIn?vehicleNo=" + shuakavehicleNo3, function (data) {
                    });
                }

        } else {
            if(ishld) {
                setRG_R2("901", "1#OFF");
                setRG_R2("901", "2#OFF");
            }
        }
    });

}

/*保存日志*/
function saveLog(op,exp,vehicleNo,ickh){

    if(typeof exp == "undefined" || exp == null || exp == ""){
        exp="无"
    }
    jp.get("${ctx}/gatelog/gateLog/savePassLog?vehicleNo=" + vehicleNo+"&op="+op+"&exp="+exp+"&ickh="+ickh, function(data){
    })
}


function bcryxx() {
    jp.get("${ctx}/userpasscord/userPassRecord/saveUserPassInfo", function(data){
        if(data.success){
           jp.success(data.msg);
        }else{
            jp.error(data.msg);
        }
    });
}
function info(){
    device.Route.get({
        position:"Z01",
        data:"{key:'jiankongdianfasong'}",
        isclose:false,
        count:0,
        interval:100,
        callback:function(data){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            if(value.length>0&&value != -1){
                device.Route.set({
                    position:"Z01",
                    data:"{key:'jiankongdianfasong',value:''}",
                    callback:function(data,ws){
                        console.log(value);
                        top.layer.alert(value);
                        jp.voice();
                    }
                });
            }
        }
    });
}
//打开读卡器
function openCardEntrance(){
    console.log("打开读卡器")
    // 开启刷卡器
    device.MWRF35.open({
        position:"221",
        //isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            //打开ic
            var icdata = eval("("+data+")");
            //返回ic状态
            var fhiczt = icdata.Data;
            if(fhiczt<0){
                console.log("发生错误，状态码为："+fhiczt);
            }else{
                readCard("221");
            }
        }
    });
}
//读取卡号
function readCard() {

    device.MWRF35.getid({
        position:"221",
        isclose:false,	//执行完毕后不关闭连接
        count:0,		//获取数据次数，0 为一直获取
        data:'{docardmodel:0,convert:1}', //convert:1全部是正的卡号;docardmodel:0只获取一次卡号，需要拿卡才可获取卡号
        interval:1000,	//获取数据时间间隔(50毫秒)8秒
        callback:function(data,ws){
            //获取ic
            var data=eval("("+data+")");
            var ickh = 	data.Data;

            if(ickh.length>5){
                console.log("卡号："+ickh);
                jp.get("${ctx}/swipecard/swipeCard/swipe?ickh="+ickh, function(data){
                    if(data.success){
                        device.GATEWAY.up({
                            position:"701",
                            callback: function (data, ws) {
                                fsLED1("402", "");
                                console.log("读卡开道闸成功！");
                                jp.success(data.msg);
                            }
                        });
                        var value = shuakavehicleNo;
                        var rfid=  shuakarfidNo;
                        //保存车辆记录
                        jp.get("${ctx}/gateconsign/consign/saveVehicleIn?vehicleNo="+value+"&rfidNo"+rfid, function (data) {
                        });

                        saveLog("升道闸","进门手动升道闸",value,ickh,rfid);
                        shuakavehicleNo="";
                        shuakarfidNo="";
                    }
                });
            }
        }
    });
}
//打开3号门223读卡器
function openCardEntrance3(){
    console.log("打开读卡器")
    // 开启刷卡器
    device.MWRF35.open({
        position:"223",
        //isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            //打开ic
            var icdata = eval("("+data+")");
            //返回ic状态
            var fhiczt = icdata.Data;
            if(fhiczt<0){
                console.log("发生错误，状态码为："+fhiczt);
            }else{
                readCard3("223");
            }
        }
    });
}
//读取卡号
function readCard3() {

    device.MWRF35.getid({
        position:"223",
        isclose:false,	//执行完毕后不关闭连接
        count:0,		//获取数据次数，0 为一直获取
        data:'{docardmodel:0,convert:1}', //convert:1全部是正的卡号;docardmodel:0只获取一次卡号，需要拿卡才可获取卡号
        interval:1000,	//获取数据时间间隔(50毫秒)8秒
        callback:function(data,ws){
            //获取ic
            var data=eval("("+data+")");
            var ickh = 	data.Data;

            if(ickh.length>5){
                console.log("卡号："+ickh);
                jp.get("${ctx}/swipecard/swipeCard/swipe?ickh="+ickh, function(data){
                    if(data.success){
                        device.GATEWAY.up({
                            position:"703",
                            callback: function (data, ws) {
                                console.log("读卡开道闸成功！");
                                //jp.success(data.msg);
                            }
                        });
                        var value = shuakavehicleNo3;
                        var rfid=  shuakarfidNo3;
                        //保存车辆记录
                        jp.get("${ctx}/gateconsign/consign/saveVehicleIn?vehicleNo="+value+"&rfidNo"+rfid, function (data) {
                        });

                        saveLog("升道闸","进门手动升道闸",value,ickh,rfid);
                        shuakavehicleNo3="";
                        shuakarfidNo3="";
                    }
                });
            }
        }
    });
}
//六号岗亭人行道闸刷卡器
function lhrxdz() {
    //人员通过道闸保存记录
    dakaidukaqi("201",function(icno){
        if (icno!="-1"&&icno!=null&&icno!=""&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserAccesspower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("G704");
                }
            });
            console.log("读卡成功"+icno)

        }
    });
    dakaidukaqi("202",function(icno){

        if (icno!="-1"&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserAccesspower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("E702");
                }
            });
            console.log("读卡成功"+icno)

        }
    });
    dakaidukaqi("203",function(icno){
        if (icno!="-1"&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserAccesspower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("F703");
                }
            });
            console.log("读卡成功"+icno)
        }
    });
    //人员通过道闸保存记录
    dakaidukaqi("204",function(icno){

        if (icno!="-1"&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserAccesspower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("F703");
                }
            });
            console.log("读卡成功"+icno)

        }
    });
    dakaidukaqi("205",function(icno){

        if (icno!="-1"&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserAccesspower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("D701");
                }
            });
            console.log("读卡成功"+icno)

        }
    });
    // 获取到身份证信息
    sfzgetinfo(function(info) {

        var icno = info.CardNo;
        if (icno!="-1"&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserpower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("D701");
                }
            });
        }


    });
}
</script>