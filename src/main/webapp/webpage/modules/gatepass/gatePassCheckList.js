<%@ page contentType="text/html;charset=UTF-8" %>
    <script>

var shuakavehicleNo = "";
var shuakaRFIDNo = "";
var isled=false;
var iscpzp=false;

$(function(){
    jp.get("${ctx}/checkAddr", function (data) {
        if(data.success){
            //一号岗亭
            if(data.object==4){
                watchCar();
                openCardExit();
                isled=true;
            }
            //三五号岗亭
            if(data.object==10||data.object==9){
                binding1();
                watchCar();
                openCardExit();
                iscpzp=true;
                isled=true;

            }

            //洗车池
            if(data.object==8){
                binding1();
                watchCar();
            }
            //六号岗亭
            if(data.object==6){
                //人行
                lhrxdz();
                binding1();
                watchCar();
                openCardExit();
                isled=true;
            }
        }
    });
})

$(document).ready(function() {
    $('#passCheckTable').bootstrapTable({

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
        url: "${ctx}/gatepass/passcheck/data",
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
                jp.confirm('确认要删除该出门条信息记录吗？', function(){
                    jp.loading();
                    jp.get("${ctx}/gatepass/passcheck/delete?id="+row.id, function(data){
                        if(data.success){
                            $('#passCheckTable').bootstrapTable('refresh');
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
                field: 'trnpAppNo',
                title: '出门条号',
                sortable: true,
                sortName: 'trnpAppNo'
            }
            ,{
                field: 'userName',
                title: '用户姓名',
                sortable: true,
                sortName: 'userName'

            }
            ,{
                field: 'vehicleNo',
                title: '车牌号',
                sortable: true,
                sortName: 'vehicleNo'

            },{
                field: 'validFlag',
                title: '生效标记',
                sortable: true,
                sortName: 'validFlag' ,
                    formatter:function(value, row , index){
                    value = jp.getDictLabel(${fns:toJson(fns:getDictList('pass_check_effect'))}, value, "-");
                    return value;

                }

            } ,{
                field: 'carryCompanyName',
                title: '承运公司',
                sortable: true,
                sortName: 'carryCompanyName'

            }
            ,{
                field: 'transContactPerson',
                title: '司机',
                sortable: true,
                sortName: 'transContactPerson'

            },{
                field: 'typeCode',
                title: '出门条类型',
                sortable: true,
                sortName: 'typeCode',
                formatter:function(value, row , index){
                    value = jp.getDictLabel(${fns:toJson(fns:getDictList('pass_check_type'))}, value, "-");
                    return value;

                }
            },{
                field: 'depName',
                title: '签发部门名称',
                sortable: true,
                sortName: 'depName'

            },{
                field: 'dealPersonName',
                title: '签发人员姓名',
                sortable: true,
                sortName: 'dealPersonName'

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

            }, {
                field: 'operate',
                title: '操作',
                align: 'center',
                events: {
                    'click .view': function (e, value, row, index) {
                        view(row.id);
                    },
                    'click .sublist': function (e, value, row, index) {
                        sublist(row.trnpAppNo);
                    }
                },
                formatter: function operateFormatter(value, row, index) {
                    return [

                        <shiro:hasPermission name="gatepass:passcheck:view">
                        '<a href="#" class="view" title="详情">[详情] </a>'
                        </shiro:hasPermission>,
                        <shiro:hasPermission name="passcheck:passcheck:sublist">
                        '<a href="#" class="sublist" title="子项列表" >[子项列表] </a>'
                        </shiro:hasPermission>

                ].join('');
                }
            }
        ]
    });
    if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


        $('#passCheckTable').bootstrapTable("toggleView");
    }

    $('#passCheckTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        $('#openGate').prop('disabled', ! $('#passCheckTable').bootstrapTable('getSelections').length);
        $('#closeGate').prop('disabled', ! $('#passCheckTable').bootstrapTable('getSelections').length);
        $('#view,#edit').prop('disabled', $('#passCheckTable').bootstrapTable('getSelections').length!=1);
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
                jp.downloadFile('${ctx}/gatepass/passcheck/import/template');
            },
            btn2: function(index, layero){
                var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                iframeWin.contentWindow.importExcel('${ctx}/gatepass/passcheck/import', function (data) {
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
        jp.download('${ctx}/gatepass/passcheck/export',JSON.stringify($("#searchForm").serializeJSON()));

    });


    $("#search").click("click", function() {// 绑定查询按扭
        $("#searchFlag").val("1");
        $('#passCheckTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function() {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#passCheckTable').bootstrapTable('refresh');
    });

    $('#beginDealDate').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
    $('#endDealDate').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
    $('#startTime').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
    $('#endTime').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });

});

function getIdSelections() {
    return $.map($("#passCheckTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll(){

    jp.confirm('确认要删除该出门条信息记录吗？', function(){
        jp.loading();
        jp.get("${ctx}/gatepass/passcheck/deleteAll?ids=" + getIdSelections(), function(data){
            if(data.success){
                $('#passCheckTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })
}

//刷新列表
function refresh(){
    $('#passCheckTable').bootstrapTable('refresh');
}

function view(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
    jp.openViewDialog('查看出门条信息', "${ctx}/gatepass/passcheck/form?id=" + id, '800px', '500px');
}
var nowRFID2="";
function watchCar() {
    device.CarLicence.open({position:"A02",callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
            device.CarLicence.monitor({position:"A02",isclose:false, interval:200,count:0,
                callback:function(data,ws){
                    var data = eval("("+data+")");
                    var val = data.Data;
                    // value 识别内容
                    console.log("车号识别2"+val);
                    if(val.length>3&&(nowRFID2==-1||nowRFID2=="") ){
                        val = val.substring(val.lastIndexOf("#") + 1, val.length+1);
                        plateRecognition(val,"");
                    }
                }
            })
        }
    });
}

function binding1(){
    //打开RFID读卡器301
    device.RFID.open({
        position:"302",
        //isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            //打开rfid
            var dkrfiddata = eval("("+data+")");
            //返回rfid状态
            var dkrfid = dkrfiddata.Data;
            if("2"==dkrfid || "1"==dkrfid){
                //获取数据
                device.RFID.getdata({
                    position:"302",
                    isclose:false,	//执行完毕后不关闭连接
                    count:0,	//获取数据次数，0 为一直获取
                    interval:1000,	//获取数据时间间隔(1000毫秒)
                    data:"{istid:true}",
                    callback:function(sjdata,ws){
                        //获取rfid
                        var data = eval("("+sjdata+")");
                        //返回rfid数据
                        var rfid = data.Data;
                        nowRFID2=rfid;
                        if(typeof rfid != "undefined" && rfid != "" && rfid != "-1" && rfid != "1" && rfid != "2") {
                                console.log("读到RFID2:" + rfid)
                            plateRecognition("",rfid);
                        }
                    }
                });
            }else{
                console.log("RFID:"+dkrfiddata.Describe+"异常RFID302:");
            }
        }
    });
}
var oldvalue="";
function plateRecognition(value,rfid) {


    if (localStorage.getItem(value) != null) {
        var time = localStorage.getItem(value);
        var times = Date.now() - time;
        if (times < 1000) {
            return;
        }
        device.GATEWAY.up({
            position:"702",
            callback: function (da, ws) {
                console.log("11、升道闸--------------------------------------------702 新逻辑" + Date.now())
                localStorage.setItem(value, Date.now());
            }
        });
        return;
    } else if(localStorage.getItem(value+"false") != null) {
        //临时车辆
        var time = localStorage.getItem(value+"false");
        var times = Date.now() - time;
        if (times < 15000) {
            return;
        }else{
            console.log("22、临时车辆"+value+"--------------------------------------------702 新逻辑" + Date.now());
            //localStorage.clear();
        }

    }else if(localStorage.getItem(value+"msg") != null) {
        //预约车辆
        var time = localStorage.getItem(value+"msg");
        var times = Date.now() - time;
        if (times < 15000) {
            return;
        }else{
            console.log("55、车辆"+value+"--------------------------------------------702 新逻辑" + Date.now());
            //localStorage.clear();
        }
    }else{
        console.log("0、新车出门 "+value + "---------------------------702 新逻辑"+ Date.now());
        //localStorage.clear();
    }

    console.log("A02---302---702");
    console.log(value+"----"+rfid);
      jp.get("${ctx}/gatepass/passcheck/checkPass?vehicleNo=" + value+"&rfidNo="+rfid, function (data) {
            if (data.success) {
                if(data.object=="10") {
                    device.GATEWAY.up({
                        position: "702",
                        callback: function (da, ws) {
                            console.log("升道闸--------------------------------------------701");
                        }
                    });
                    if (isled) {
                        if (localStorage.getItem(data.errorCode) != null) {
                            var time = localStorage.getItem(data.errorCode);
                            var times = Date.now() - time;
                            if (times < 1000) {
                                return;
                            } else {
                                localStorage.clear();
                            }
                        } else {
                            localStorage.setItem(data.errorCode, Date.now());
                            console.log("出门LED" + data.msg)
                            fsLED1("401", data.msg);
                        }
                    }
                    if(oldvalue!=data.errorCode) {
                        oldvalue=data.errorCode;
                        //保存车辆记录
                        jp.get("${ctx}/gatepass/passcheck/saveVehicleOut?vehicleNo=" + data.errorCode, function (data) {
                            if(data.success){
                                if(iscpzp){
                                    //出门抓拍
                                    zhuapaigate(data.errorCode,"");
                                }
                            }
                        });

                    }
                }
                if (data.object == 2) {
                    if (localStorage.getItem(data.errorCode) != null) {
                        var time = localStorage.getItem(data.errorCode);
                        var times = Date.now() - time;
                        if (times < 1000) {
                            return;
                        }else{
                            localStorage.clear();
                        }
                    } else {
                        localStorage.setItem(data.errorCode, Date.now());
                        device.GATEWAY.up({
                            position: "702",
                            callback: function (data, ws) {
                                console.log("道闸--------------------------------------702");
                            }
                        });
                        if(isled){
                            fsLED1("401", data.msg);
                        }
                    }

                    if(oldvalue!=data.errorCode){
                        oldvalue=data.errorCode;
                        //保存车辆记录
                        jp.get("${ctx}/gatepass/passcheck/saveVehicleOut?vehicleNo="+data.errorCode+"&remarks=1", function (data) {
                            if(data.success){
                                if(iscpzp){
                                    //出门抓拍
                                    zhuapaigate(data.errorCode,"");
                                }
                            }
                        });
                    }
                }else {
                    console.log("44、车辆"+data.errorCode+"--------------------------------------------702 新逻辑" + Date.now());

                    shuakavehicleNo = data.errorCode;
                    shuakaRFIDNo = rfid;
                    if (data.msg != ""&&data.errorCode!="") {
                        if(isled){
                            if (localStorage.getItem(data.msg) != null) {
                                var time = localStorage.getItem(data.msg);
                                var times = Date.now() - time;
                                if (times < 15000) {
                                    return;
                                }else{
                                    localStorage.clear();
                                }
                            } else {
                                localStorage.setItem(value+"msg", Date.now());
                                localStorage.setItem(data.msg, Date.now());
                                fsLED1("401", data.msg);
                            }

                        }
                    }
                    if(oldvalue!=data.errorCode) {
                        oldvalue=data.errorCode;
                        //保存车辆记录
                        jp.get("${ctx}/gatepass/passcheck/saveVehicleOut?vehicleNo=" + data.errorCode, function (data) {
                            if(data.success){
                                if(iscpzp){
                                    //出门抓拍
                                    zhuapaigate(data.errorCode,"");
                                }
                            }
                        });

                    }
                }

            }else{
                //storvalue = data.msg;

                console.log("33、临时车辆"+value+"--------------------------------------------702 新逻辑" + Date.now());


                shuakavehicleNo = value;
                shuakaRFIDNo = rfid;
                if (data.msg != ""&&value!="") {
                    if(isled){
                        if (localStorage.getItem(data.msg) != null) {
                            var time = localStorage.getItem(data.msg);
                            var times = Date.now() - time;
                            if (times < 15000) {
                                return;
                            }else{
                                localStorage.clear();
                            }
                        } else {
                            localStorage.setItem(value+"false", Date.now());
                            localStorage.setItem(data.msg, Date.now());
                            fsLED1("401", data.msg);
                        }
                    }
                }
                if(oldvalue!=value) {
                    oldvalue=value;
                    //保存车辆记录
                    jp.get("${ctx}/gatepass/passcheck/saveVehicleOut?vehicleNo=" + value, function (data) {
                    if(data.success){
                        if(iscpzp){
                            //出门抓拍
                            zhuapaigate(value, "");
                        }
                    }
                    });


                }
            }
        })
    }

//抓拍
function zhuapaigate(vehicleNo,rfid){
    jp.get("${ctx}/videos/videos/queryVideoCamera", function (data) {
        if (data.success) {
            var v= data.data;
            var spConfig = v.videoCamera;
            console.log("出门岗抓拍通道号："+spConfig);

            var minecode = 'gateimage';

            var value=0;
            device.Video.sendmsg({
                position:"C01",
                data:"{sendmsg:'TakePic,"+minecode+","+spConfig+"'}",
                callback:function(data,ws){
                    var data = eval("("+data+")");
                    value = data.Data;
                    if(value.length>3){
                        console.log("图片~~~~~~~~~~~~~"+value);
                        saveweightpic(value,vehicleNo,rfid);
                    }
                }
            });
        }
    });
}
function saveweightpic(value,vehicleNo,rfid) {
    //获取抓拍图片
    //判断稳定后获取抓拍.
    console.log("@@@@@@@@@@@@@@@@@@@@@");
    id = getIdSelections();
    var weight = $("#weight").val();

    var pic = value;
    var pic1 = pic.split("#")[1];

    var picz = "";

    if (pic1.split(",")[0] == "true") {
        picz = pic1.split(",")[2] + ",";
    } else {
        picz = ",";
    }

    var piczs = picz.split(",")
    var ct = piczs[0].toString();

    console.log("==========="+ct)

    jp.get("${ctx}/gatepass/passcheck/saveTackPhoto?vehicleNo=" + vehicleNo+"&rfidNo="+rfid+"&ct=" + ct, function (data) {

    })

}
/*保存日志*/
function saveLog(op,exp,vehicleNo,ickh,rfid){

    if(typeof exp == "undefined" || exp == null || exp == ""){
        exp="无"
    }
    jp.get("${ctx}/gatelog/gateLog/savePassLog?op="+op+"&exp="+exp+"&ickh="+ickh+"&vehicleNo=" + vehicleNo+"&rfidNo=" + rfid,function(data){

    })
}


//手动发送LED
function sendLED() {
    var c = $("#LEDcontent").val();
    device.LED.sendmsg({
        position:"401",
        isclose:true,
        data:"{'sendmsg':'"+c+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            if(value>0){
                jp.success("发送成功！")
            }
        }
    });
}

//出门条子项列表
function sublist(id) {
    jp.openViewDialog('查看出门条子项列表', "${ctx}/passchecksub/passCheckSub/list?trnpAppNo=" + id, '800px', '500px');
}
//打开读卡器
function openCardExit(){

    console.log("打开读卡器")
    // 开启刷卡器
    device.MWRF35.open({
        position:"222",
        //isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            //打开ic
            var icdata = eval("("+data+")");
            //返回ic状态
            var fhiczt = icdata.Data;
            if(fhiczt<0){
                console.log("发生错误，状态码为："+fhiczt);
            }else{
                readCard("222");
            }
        }
    });
}
//读取卡号
function readCard() {

    device.MWRF35.getid({
        position:"222",
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
                var value = shuakavehicleNo;
                var rfid=shuakaRFIDNo;
                console.log("识别到的车牌号："+value)
                jp.get("${ctx}/interface/postPasscheck?vehicleNo="+value+"&ickh="+ickh, function(data){

                });
                jp.get("${ctx}/swipecard/swipeCard/swipe?ickh="+ickh, function(data){
                    if(data.success){
                        device.GATEWAY.up({
                            position:"702",
                            callback: function (data, ws) {
                                console.log("读卡开道闸成功！");
                            }
                        });

                        deletePass(value,rfid,ickh);
                        saveLog("升道闸","出门手动升道闸",value,ickh,rfid);
                        shuakavehicleNo="";
                        shuakaRFIDNo="";
                    }
                });
            }
        }
    });
}

function deletePass(value,rfid,ickh) {
    jp.get("${ctx}/gatepass/passcheck/deletePass?vehicleNo="+value+"&rfidNo="+rfid+"&ickh="+ickh, function (data) {
    });
}
//六号岗亭人行道闸刷卡器
function lhrxdz() {
    //人员通过道闸保存记录
    dakaidukaqi("201",function(icno){

        if (icno!="-1"&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserAccesspower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("E702");
                }
            });
            console.log("读卡成功201"+icno)
        }
    });
    dakaidukaqi("202",function(icno){

        if (icno!="-1"&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserAccesspower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("E702");
                }
            });
            console.log("读卡成功202"+icno)
        }
    });
    dakaidukaqi("203",function(icno){

        if (icno!="-1"&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserAccesspower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("D701");
                }
            });
            console.log("读卡成功203"+icno)
        }
    });
    dakaidukaqi("204",function(icno){

        if (icno!="-1"&&icno!=null&&icno!="") {

            jp.get("${ctx}/userpasscord/userPassRecord/checkUserAccesspower?ickh="+icno, function(data){
                if(data.success){
                    kaizha("D701");
                }
            });
            console.log("读卡成功204"+icno)
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
            console.log("读卡成功sfz"+icno)
        }
    });
}
    </script>