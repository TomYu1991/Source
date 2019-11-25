<%@ page contentType="text/html;charset=UTF-8" %>
    <script>

var listnum=false;
var hwstate = false;
var selectdata = null;
var hldstate ="";
var spConfig = "";
var hwStatus = true;
var oldweight = 0;//仪表稳定
var ybwd=false;
var currentweight=0;
var check = "";
//刷新页面和修改车号公共函数
var  xgflag=0;//
var nowrfid=false;
var rfid="";//接收rfid卡号
var billpic = "";//保存抓拍到的照片路径
var savestate=false;
$(document).ready(function() {
    device.debug=true;
    watchcar();
    $('#dy').attr("disabled",true);
    $("#showweight").hide();
    dqsj("101");
    kqHW("501");
    dqsjHW("501");
    binding();//RFID
    dkewm("sendrecive");
    dqewm("sendrecive");
    common();
    $('#consignTable').bootstrapTable({
        //请求方法
        method: 'post',
        //类型json
        dataType: "json",
        contentType: "application/x-www-form-urlencoded",
        //最低显示2行
        minimumCountColumns: 2,
        //是否显示行间隔色
        striped: false,
        //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        cache: false,
        //是否显示分页（*）
        pagination: false,
        //排序方式
        sortOrder: "desc",
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        //查询委托单
        url: "${ctx}/consign/consign/showweights",
        //分页方式：client客户端分页，server服务端分页（*）
        queryParams : function(params) {
            var searchParam = $("#searchForm").serializeJSON();
              return searchParam;
/*
            var req = new Object();
            req.vehicleNo = str;
            return req;*/
        },
        sidePagination: "server",
        contextMenuTrigger:"right",//pc端 按右键弹出菜单
        contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。

        onClickRow: function(row, $el){
            console.log($el);
            if($('#dy').attr("disabled") == "disabled" && selectdata != null){
                return;
            }
            if ($($el).hasClass("info")) {
                $('.info').removeClass('info');//移除class
                selectdata = null;
                $('#dy').attr("disabled",true);

            }else {
                selectdata = row;
                if(row.weightState == 1){
                    fssjyy("601", "请放好票据，点击请求过磅");
                }

                if(ybwd==true){
                    $('#dy').attr("disabled",false);
                }
                $('.info').removeClass('info');//移除class
                $($el).addClass('info');//添加class

            }


        },
        onShowSearch: function () {
            $("#search-collapse").slideToggle();
        },
        columns: [
            {
                field: 'prodCname',
                title: '物资名称',
                sortName: 'prodCname',
                formatter: function(value,row,index) {
                    var a = "";
                    if(typeof(value)=="undefined"){
                        var a = '<span style="font-size: 40px"></span>';
                    }else{
                        var a = '<span style="font-size: 40px">'+value+'</span>';
                    }
                    return a;
                },
                align:'center'
            }
            ,{
                field: 'consigneUser',
                title: '收货单位',
                sortName: 'consigneUser',
                formatter: function(value,row,index) {
                    var a = "";
                    if(typeof(value)=="undefined"){
                        var a = '<span style="font-size: 40px"></span>';
                    }else{
                        var a = '<span style="font-size: 40px">'+value+'</span>';
                    }
                    return a;
                },
                align:'center'
            }
            ,{
                field: 'supplierName',
                title: '发货单位',
                sortName: 'supplierName',
                formatter: function(value,row,index) {
                    var a = "";
                    if(typeof(value)=="undefined"){
                        var a = '<span style="font-size: 40px"></span>';
                    }else{
                        var a = '<span style="font-size: 40px">'+value+'</span>';
                    }
                    return a;
                },
                align:'center'
            }
            ,{
                field: 'matGrossWt',
                title: '毛重',
                sortName: 'matGrossWt',
                formatter: function(value,row,index) {
                    var a = "";
                    if(typeof(value)=="undefined"){
                        var a = '<span style="font-size: 40px"></span>';
                    }else{
                        var a = '<span style="font-size: 40px">'+value+'</span>';
                    }
                    return a;
                },
                align:'center'
            }
            ,{
                field: 'impWt',
                title: '皮重',
                sortName: 'impWt',
                formatter: function(value,row,index) {
                    var a = "";
                    if(typeof(value)=="undefined"){
                        var a = '<span style="font-size: 40px"></span>';
                    }else{
                        var a = '<span style="font-size: 40px">'+value+'</span>';
                    }
                    return a;
                },
                align:'center'
            }
            ,{
                field: 'weightState',
                title: '称重状态',
                sortable: true,
                sortName: 'weightState',
                formatter:function(value, row , index){
                    value = jp.getDictLabel(${fns:toJson(fns:getDictList('weight_state'))}, value, "-");
                    return value;
                }
            }
            ,{
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

                        '<a href="#" class="view" title="详情" >[详情] </a>',

                    ].join('');
                }
            }
        ]

    });
})

function common(){
    device.Route.get({
        position:"Z01",
        count:0,
        isclose:false,
        interval:50,
        data:"{key:'xiugaichehao' }",
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            console.log("长连接接受数据："+value);
            if(value.length>0&&value != -1){
                if(value == "001"){
                    console.log("刷新")
                    reload();
                }
                if(value == "002"){

                    jp.success("请重新扫描二维码");
                    fssjyy("601","请重新扫描二维码")
                    dkewm("sendrecive");
                    dqewm("sendrecive");
                }
                if(value == "003"){
                    hwStatus  = true;
                }
                if(value == "005"){
                    console.log("回退提醒！");
                    layer.close(index);
                    jp.error("您的请求已回退");
                    fssjyy("601","您的请求已回退");
                    reload();
                }
                if(value == "006"){
                    console.log("过二磅后刷新表格");
                    layer.close(index);
                    listnum=false;
                    refresh();
                }
                if(value == "004"){
                    hwStatus  = false;
                }else{
                    console.log("修改车号:"+value);
                    xgflag=1;
                    ConsignplateRecognition(value,"",ws);
                }
                device.Route.set({
                    position:"Z01",
                    data:"{key:'xiugaichehao',value:''}",
                    callback:function(data,ws){
                    }
                });
            }
        }
    });
}

function getIdSelections() {
    if (selectdata!=null) {
        return selectdata.id;
    }
    return "";
}
function getStateSelections() {
    if (selectdata!=null) {
        return selectdata.weightState;
    }
    return "";
}


function ybwending(picnames) {
    console.log("仪表重量"+currentweight);
    if (currentweight*1 != oldweight && Math.abs(currentweight*1-oldweight*1)>60) {
        console.log("重量不稳定，请稍等！");
        oldweight = currentweight;
        ybwd=false;
        window.setTimeout("ybwending('"+picnames+"')",4000);

    } else {
        console.log("稳定判断"+Date.now())
        oldweight = 0;
        if(!savestate){
            url(currentweight);
        }
    }
}

function meter(value){
    document.getElementById("weight").value=value;
	var vname= $("#vehicleNo").val();
    	console.log("value:"+value);
	console.log("vname:"+vname);
        if (parseFloat(value) >100 && parseFloat(value)>0 ) {

            if (hldstate!="red" || hldstate=="") {
                setRG_R2("901","1#OFF");
                hldstate="red";
            }
             currentweight = value;
            if (currentweight*1 != oldweight || Math.abs(currentweight*1-oldweight*1)>60) {
                console.log("重量不稳定，请稍等！");
                oldweight = currentweight;
                window.setTimeout("ybwending('"+picnames+"')",4000);
                ybwd=false;
            } else {
                console.log("稳定判断"+Date.now())
                oldweight = 0;
                ybwd=true;
            }
        } else {
            var v = $("#vehicleNo").val();
            if(v!=null&&v!=""){
                reload();
            }
            if (hldstate!="green" || hldstate==""){
                if(hldstate !=""){
                   reload();
                }
                hldstate="green";
                setRG_G("901","1#ON");

            }
        }
	console.log('来了...');
	if(parseFloat(value)>1000){
		console.log('进来了...');
	jp.get("${ctx}/weightmonitor/weightmonitor/saveWeightData?weightValue="+value+"&vehicleNo="+vname,function (data){
		console.log(data.msg);
	       });
}
	console.log('到底了...');
   
}

function changePrint(weighNo,ip,flag){

    jp.get("${ctx}/weight/weight/changePrint?weighNo="+weighNo+"&ip="+ip+"&flag="+flag, function (data) {
        if (data.success) {
            console.log("打印票据结束"+Date.now())
            $('#consignTable').bootstrapTable('refresh');
        }
    });
}
//刷新列表
function refresh(){
    $('#consignTable').bootstrapTable('refresh');
}


function view(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
    jp.openViewDialog('查看委托单', "${ctx}/consign/consign/form?id=" + id, '1000px', '500px');
}
var index;
function sureweight() {

    $('#dy').attr("disabled", true);
    $('#sx').attr("disabled", true);
    console.log("点击请求打印");

    index= layer.load(0, { //icon支持传入0-2
        shade: [0.1, 'gray'], //0.5透明度的灰色背景
        content: '正在处理，请稍等···',
        success: function (layero) {
            layero.find('.layui-layer-content').css({
                'padding-top': '39px',
                'width': '400px',
                'font-size':'40px'
            });
        }
    });
            var state = getStateSelections();
            console.log("委托单状态"+state);
            if (state == 1) {
                WPYSY();
            } else {
                ybwending('');
            }
}

//文拍仪调用方法
function WPYSY(){
    console.log("文拍仪开始抓拍-------")
    device.WPY.picture({
        position:"WPY",
        callback:function(data,w){
            console.log("文拍仪抓拍成功-------")
            var data = eval("("+data+")");
            var value = data.Data;
            billpic= value;
            console.log("高拍仪抓拍上传"+value+"票据截图"+Date.now())
           ybwending('');
        }
    });
}

function url(currentweight){
    try {
        //获取抓拍图片
        //判断稳定后获取抓拍.
        var id = getIdSelections();
        var weight = currentweight;
        savestate=true;
    console.log("开始保存"+Date.now())
    if(weight<50){
        jp.alert("重量小于50kg");
        return;
    }
        var state = getStateSelections();
    if(state==0){
        jp.get("${ctx}/weight/weight/saveweightConsign?id="+id+"&zl="+weight,function (data) {
            if (data.success) {
                console.log("------保存成功==========="+data.object)
                dypj(data.data);
                zp(data.object);
                refresh();
                listnum=false;
            } else {
                layer.close(index);
                fssjyy("601",data.msg);
            }
        })
    }else{
        jp.get("${ctx}/weight/weight/updateweightConsign?id="+id+"&zl="+weight+"&billpic="+billpic,function (data) {
            if (data.success) {
                var weighNo =data.object;
                console.log("------保存成功==========="+data.errorCode)
                zp(weighNo);
            } else {
                layer.close(index);
                fssjyy("601",data.msg);
            }
        })
    }
    } catch (e1) {
        console.log(e1)
    }
    }

//格式化日期
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
//打印票据
function dypj(print){
    console.log("打印票据开始"+Date.now());

            layer.close(index);
            if(print==""){
                console.log("票据数据为空")
                return;
            }

            if(typeof(print.supplierName) == "undefined"){
                print.supplierName =""
            }
            if(typeof(print.consigneUser) == "undefined"){
                print.consigneUser =""
            }
            if(typeof(print.vehicleNo) == "undefined"){
                print.vehicleNo =""
            }
            if(typeof(print.prodCname) == "undefined"){
                print.prodCname =""
            }
            if(typeof(print.workStation) == "undefined"){
                print.workStation =""
            }
            if(typeof(print.defaultFlag) == "undefined"){
                print.defaultFlag =""
            }
            var pria = print.supplierName;
            var pric = print.consigneUser;
            var prib = "";
            var prid = "";
            var prie = "";
            var prif = "";

            if(pria.length>12){
                prib=pria.substring(0,12)
                prid=pria.substring(12,pria.length)
            }else{
                prib=pria;
                prid="";
            }
            if(pric.length>12){
                //b=b.substring(0,12)+"\n"+b.substring(12,b.length)
                prie=pric.substring(0,12)
                prif=pric.substring(12,pric.length)
            }else{
                prie=pric;
                prif="";
            }

            if(print.defaultFlag == 1){
                var def = "锁皮";
            }else{
                def = "";
            }

            if(typeof(print.supplierName) == "undefined"){
                print.supplierName =""
            }
            if(typeof(print.consigneUser) == "undefined"){
                print.consigneUser =""
            }
            if(typeof(print.vehicleNo) == "undefined"){
                print.vehicleNo =""
            }
            if(typeof(print.prodCname) == "undefined"){
                print.prodCname =""
            }
            if(typeof(print.workStation) == "undefined"){
                print.workStation =""
            }
            if(typeof(print.defaultFlag) == "undefined"){
                print.defaultFlag =""
            }
            var qrcode =print.weighNo.toString()

    var printdata = "{'header':'宝钢德盛不锈钢"
        + "','header2':'有限公司"
        + "','header3':'物料签收单"
        + "','grossweighttime':'日期:" + (new Date(print.grosstime)).Format("yyyy/MM/dd/ HH:mm")
        + "','vehicleno':'车号:"+print.vehicleNo
        + "','prodcname':' 货物名称  "+print.prodCname
        + "','grossname':' 毛重(Kg) ','grossweight':'  "+(isNaN(parseInt(print.matGrossWt)) ? "" : parseInt(print.matGrossWt))
        + "','tarename':' 皮重(Kg) ','tareweight':' "+(isNaN(print.impWt) ? "" : parseInt(print.impWt))
        + "','standardname':' 净重(Kg) ','standardweight':' "+(isNaN(print.matWt)? "" : print.matWt)
        + "','custname':' 发货单位  "+prib
        + "','custname2':'           "+prid
        + "','recustname':' 收货单位  "+prie
        + "','recustname2':'           "+prif
        + "','unloadingaddress':'装卸货地址 "
        + "','receiver':' 签 收 人 "
        + "','weighNo':'"+print.weighNo.toString()
        + "','qrcode':'"+qrcode
        + "','def':'"+def
        + "','workStation':' "+print.workStation
        + "',_file_:'billweight.xml'}";
    device.ip=print.fistStation;
    var ip = device.ip;
    printaa(printdata,print.weighNo,ip,"1");

}


function getRFID(value){
    rfid = value;
}

//车号识别
function watchcar() {
    device.CarLicence.open({position:"A01",callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
            device.CarLicence.monitor({position:"A01",isclose:false, interval:100, count:0,
                callback:function(data,ws){
                    var data = eval("("+data+")");
                    var val = data.Data;
                    // value 识别内容
                    if(val.length>10&&nowrfid==false){
                        val = val.substring(val.lastIndexOf("#") + 1, val.length+1);
                        ConsignplateRecognition(val, "", ws);
                    }
                }
            })
        }
    });
}
var nowve=false;
//RFID
function binding(){
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
                        if(typeof rfid != "undefined" && rfid != "" && rfid != "-1" && rfid != "1" && rfid != "2") {
                            nowrfid=true;
                            if(!nowve){
                                ConsignplateRecognition("", rfid, ws);
                            }

                        }
                    }
                });
            }else{
                console.log("RFID:"+dkrfiddata.Describe+"异常RFID301:"+rfid);
            }
        }
    });
}

function ConsignplateRecognition(val,rfid,_websocket){
    check=val;
    var ws=_websocket;//websocket对象 下面需要关闭的
    if(xgflag==1){
        if(ws){
            ws.close();
        }
    }

    if(hwStatus){
        if (!hwstate) {
            jp.error("红外线被遮挡,请移动车辆");
            return;
        }
    }

    if(listnum==false){
        jp.get("${ctx}/gateconsign/consign/checkConsignByWeight?vehicleNo="+val+"&rfidNo="+rfid, function (data) {
            if (data.success) {
                console.log("#"+data.object+"#");
                if(data.object!=""){
                    try{
                        listnum=true;
                        $("#showweight").show();
                        var str =data.object;
                        $("#vehicleNo").val(str);
                        $("#SvehicleNo").val(str);
                        device.Route.set({ position:"Z01",isclose:true,data:"{key:'fanxiansschehao',value:'"+str+"'}" });
                        // value 识别内容
                        refresh();
                    }catch (e) {
                        console.log(e)
                    }
                }

            }else{
                if(data.errorCode=="2"){
                    nowrfid=false;
                    nowve=true;
                }
                jp.error(data.msg);
            }
        })
    }


};

function reload() {
    window.location.reload();
}
function zp(weighNo){
    jp.get("${ctx}/videos/videos/queryVideoCamera", function (data) {
        if (data.success) {
            var v= data.data;
            spConfig = v.videoCamera;
    console.log("抓拍！！！！！！！！！！！！！！！！！"+spConfig);

    var minecode = 'image';
    var spConfig = spConfig;
    var value=0;
    device.Video.sendmsg({
        position:"C01",
        data:"{sendmsg:'TakePic,"+minecode+","+spConfig+"'}",
        callback:function(data,ws){
            var data = eval("("+data+")");
            value = data.Data;
            console.log("图片~~~~~~~~~~~~~"+value);
            saveweightpic(value,weighNo);
        }
    });
        }
    });
}
function saveweightpic(value,weighNo) {
    //获取抓拍图片
    //判断稳定后获取抓拍.
    console.log("@@@@@@@@@@@@@@@@@@@@@");

    var pic = value;
    var pic1 = pic.split("#")[1];
    var pic2 = pic.split("#")[2];
    var pic3 = pic.split("#")[3];
    var picz = "";

    if (pic1.split(",")[0] == "true") {
        picz = pic1.split(",")[2] + ",";
    } else {
        picz = ",";
    }
    if (pic2.split(",")[0] == "true") {
        picz = picz + pic2.split(",")[2] + ",";
    } else {
        picz = picz + ",";
    }
    if (pic3.split(",")[0] == "true") {
        picz = picz + pic3.split(",")[2] + ",";
    } else {
        picz = picz + ",";
    }
    var piczs = picz.split(",")
    var ct = piczs[0].toString();
    var cw = piczs[1].toString();
    var cd = piczs[2].toString();
    console.log("==========="+ct)
    console.log("==========="+cw)
    console.log("==========="+cd)

    jp.get("${ctx}/weight/weight/saveweightpic?weighNo=" + weighNo + "&ct=" + ct + "&cw=" + cw + "&cd=" + cd, function (data) {
    })

}

//根据二维码查询委托单
function reback(ewm) {
    console.log(ewm);
    jp.get("${ctx}/weight/weight/analysis?ewm="+ ewm, function (data) {
        if(data.success){
            $("#showweight").show();
            var cons = data.object;
            var str =check;
           //console.log("consignId："+cons);
            listnum=true;
                $("#SvehicleNo").val(str);
                $("#SconsignNo").val(cons);
                device.Route.set({ position:"Z01",isclose:true,data:"{key:'fanxiansschehao',value:'"+str+"'}" });
                refresh();
        }
    })
}



</script>

