<%@ page contentType="text/html;charset=UTF-8" %>
    <script >

var selectdata = null;
var spConfig = "";
var hwstate = true;
var hwStatus = true;
var weight = 0;
var hldstate = "";
var listnum = false;
var oldrfid = "";
var state = "";
var Cid = "";
var BFNo = "";
var con;
var index;
var weightnum = new Array();
var tempunm = 0;
var tempList= new Array();
var tempIndex = 0;
var isStable = false;
var isFinish = true;
var consignId = "";
// var isRed = false;
$(document).ready(function () {
    device.debug = true;
    $('#dy').attr("disabled", true);
    $('#vehicleNo').attr("disabled", false);
    dqsj("101");//仪表
    binding();
    // common();
    setRG_G("901", '1#ON');
    setRG_G("901", '2#ON');
    //rglmonitor();
    // value 识别内容
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
        sortOrder: "asc",
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        //查询委托单
        url: "${ctx}/consign/consign/gdhshowweights",
        //分页方式：client客户端分页，server服务端分页（*）
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            return searchParam;
            /*
                        var req = new Object();
                        req.vehicleNo = str;
                        return req;*/
        },
        //分页方式：client客户端分页，server服务端分页（*）
        sidePagination: "server",
        contextMenuTrigger: "right",//pc端 按右键弹出菜单
        contextMenuTriggerMobile: "press",//手机端 弹出菜单，click：单击， press：长按。

        onClickRow: function (row, $el) {
            console.log($el);

            if ($('#dy').attr("disabled") == "disabled" && selectdata != null) {
                return;
            }
            if ($($el).hasClass("info")) {
                $('.info').removeClass('info');//移除class
                selectdata = null;
                $('#dy').attr("disabled", true);

            } else {
                $('#dy').attr("disabled", false);
                $('.info').removeClass('info');//移除class
                $($el).addClass('info');//添加class
                selectdata = row;
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
                formatter: function (value, row, index) {
                    var a = "";
                    if (typeof (value) == "undefined") {
                        var a = '<span style="font-size: 30px"></span>';
                    } else {
                        var a = '<span style="font-size: 30px">' + value + '</span>';
                    }
                    return a;
                },
                align: 'center'
            }, {
                field: 'blastFurnaceNo',
                title: '高炉号',
                sortName: 'blastFurnaceNo',
                formatter: function (value, row, index) {
                    var a = "";
                    if (typeof (value) == "undefined") {
                        var a = '<span style="font-size: 30px"></span>';
                    } else {
                        var a = '<span style="font-size: 30px">' + value + '</span>';
                    }
                    return a;
                },
                align: 'center'
            }
            , {
                field: 'consigneUser',
                title: '收货单位',
                sortName: 'consigneUser',
                formatter: function (value, row, index) {
                    var a = "";
                    if (typeof (value) == "undefined") {
                        var a = '<span style="font-size: 30px"></span>';
                    } else {
                        var a = '<span style="font-size: 30px">' + value + '</span>';
                    }
                    return a;
                },
                align: 'center'
            }

            , {
                field: 'supplierName',
                title: '发货单位',
                sortName: 'supplierName',
                formatter: function (value, row, index) {
                    var a = "";
                    if (typeof (value) == "undefined") {
                        var a = '<span style="font-size: 30px"></span>';
                    } else {
                        var a = '<span style="font-size: 30px">' + value + '</span>';
                    }
                    return a;
                },
                align: 'center'
            }
            , {
                field: 'matGrossWt',
                title: '毛重',
                sortName: 'matGrossWt',
                formatter: function (value, row, index) {
                    var a = "";
                    if (typeof (value) == "undefined") {
                        var a = '<span style="font-size: 30px"></span>';
                    } else {
                        var a = '<span style="font-size: 30px">' + value + '</span>';
                    }
                    return a;
                },
                align: 'center'
            }
            , {
                fd: 'impWt',
                title: '皮重',
                sortName: 'impWt',
                formatter: function (value, row, index) {
                    var a = "";
                    if (typeof (value) == "undefined") {
                        var a = '<span style="font-size: 30px"></span>';
                    } else {
                        var a = '<span style="font-size: 30px">' + value + '</span>';
                    }
                    return a;
                },
                align: 'center'
            }
            , {
                field: 'weightState',
                title: '称重状态',
                sortable: true,
                sortName: 'weightState',
                formatter: function (value, row, index) {
                    value = jp.getDictLabel(${fns:toJson(fns:getDictList('weight_state'))}, value, "-");
                    return value;
                }
            }
            , {
                field: 'operate',
                title: '操作',
                align: 'center',
                events: {
                    'click .view': function (e, value, row, index) {
                        view(row.id);
                    },

                },
                formatter: function operateFormatter(value, row, index) {
                    return [

                        '<a href="#" class="view" title="详情" >[详情] </a>',

                    ].join('');
                }
            }
        ]

    });
})

function xgch() {
    var v = $("#vehicleNo").val();
    console.log("修改车牌=========" + v);
    if (v != null && v != "") {
        listnum = true;
        plateRecogn(v, "");
    }
}

//刷新页面和修改车号公共函数
/*

function common(){
    device.Route.get({
        position:"Z01",
        count:0,
        isclose:false,
        interval:500,
        data:"{key:'xiugaichehao' }",
        callback:function(d,ws){
            var data_ = eval("("+d+")");
            var value = data_.Data;
            //console.log("长连接接收"+value)
            if(value.length>0&&value != -1&&value != ""){

                if(value == "005"){
                    console.log("回退提醒！");
                    jp.error("您的请求已回退");
                    fssjyy("601","请稍等");
                    clearWeightflag1();
                    layer.close(index);
                    hwstate  = true;
                    listnum=true;
                }
                if(value == "006"){
                    layer.close(index);
                  console.log("006");
                }else{
                    if(value!="001"&&value!="003"&&value!="004"&&value!="005"&&value!="006"){
                        console.log("修改铁水罐号:"+value);
                        listnum=true;
                       /!* $("#SladleNo").val(value);
                        refresh();
                        document.getElementById("vehicleNo").value=value;*!/
                        plateRecogn(value,"");
                    }
                }
                if(value!=""){
                    device.Route.set({
                        position:"Z01",
                        data:"{key:'xiugaichehao',value:''}",
                        callback:function(data,ws){
                        }
                    });
                }

            }
        }
    })

}
*/

function getIdSelections() {
    if (selectdata != null) {
        return selectdata.id;
    }
    return "";
}


var ip = "";

function saveWeight() {
    isStable=false;
    isFinish=false;
    tempList.length=0;
    tempIndex=0;
    var zl = $("#weight").val();
    /*if(hwStatus){
      if (!hwstate) {
          jp.error("未完全上磅,请移动车辆");
          return;
      }
  }*/

    $('#dy').attr("disabled", true);
    index = layer.load(0, { //icon支持传入0-2
        shade: [0.1, 'gray'], //0.5透明度的灰色背景
        content: '正在处理，请稍等···',
        success: function (layero) {
            layero.find('.layui-layer-content').css({
                'padding-top': '39px',
                'width': '400px',
                'font-size': '40px'
            });
        }
    });

    if (state == 0 || state == "") {
        jp.get("${ctx}/weight/weight/saveweightConsign?id=" + Cid + "&zl=" + zl, function (data) {
            if (data.success) {
                layer.close(index);
                jp.success("保存成功");
                $('#vehicleNo').attr("disabled", false);
                var v = $("#vehicleNo").val();
                if (v == null || v == "") {
                    document.getElementById("vehicleNo").value = "强制过磅";
                }
                var w = data.data;
                device.ip = w.fistStation;
                ip = w.fistStation;
                console.log("ip=================" + ip);
                listnum = false;
                refresh();
                fssjyy("601", "请下磅，请下磅")
                setRG_G("901", '1#ON');
                setRG_G("901", '2#ON');
                // isRed=false;
                console.log('保存成功，变绿灯-------------'+Date.now())
                dypj(data.data);
                zp(data.object);

            } else {
                layer.close(index);
                jp.error(data.msg);
            }
        })
    } else {
        jp.get("${ctx}/weight/weight/updateweightConsign?id=" + Cid + "&zl=" + zl, function (data) {
            if (data.success) {
                refresh();
                listnum = false;
                var w = data.data;
                layer.close(index);
                device.ip = w.secondStation;
                ip = w.secondStation;
                console.log("ip=================" + ip);
                jp.success("保存成功")
                $('#vehicleNo').attr("disabled", false);
                fssjyy("601", "请下磅，请下磅")
                setRG_G("901", '1#ON');
                setRG_G("901", '2#ON');
                // isRed=false;
                console.log('保存成功，变绿灯-------------'+Date.now())
                dybd(data.data);
                zp(data.object);
            } else {
                layer.close(index);
                jp.error(data.msg);
            }
        })
    }
}

function meter(value) {
    document.getElementById("weight").value = value;
    var vname = $("#vehicleNo").val();
    if (parseFloat(value) < 1000) {
        var v = $("#vehicleNo").val();
        if (v != null && v != "") {
            reload();
        }
        if (hldstate != "green" || hldstate == "") {
            if (hldstate != "") {
                reload();
            }
            hldstate = "green";
        }
    } else {
        var v = $("#vehicleNo").val();
        if (v == null || v == "") {
            weightnum[tempunm] = value;
            // console.log(weightnum);
            tempunm = tempunm + 1;
            if (tempunm > 2) {
                var weightAver1 = weightnum[tempunm - 1];
                var weightAver2 = weightnum[tempunm - 2];
                if (weightAver1 > 30000 && weightAver1 == weightAver2) {
                    if (ip != "") {
                        device.ip = ip;
                    }
                    setRG_R2("901", '1#OFF');
                    setRG_R2("901", '2#OFF');
                }
            }
        }
        $('#dy').attr("disabled", false);
        weight = value;
        var v = $("#vehicleNo").val();
        if (hldstate != "red" || hldstate == "") {
            if (hldstate != "") {
                device.ip = "10.12.241.10";
                txjks();
                device.ip = "10.12.241.20";
                txjks();
                device.ip = "10.12.242.30";
                txjks();
                device.ip = "10.12.242.40";
                txjks();
                device.ip = "10.12.242.41";
                txjks();
            }
            hldstate = "red";
        }
        if(isFinish==true && v != null && v != "" ){
            tempList[tempIndex]=value;
            console.log(tempList);
            tempIndex = tempIndex+1;
            if(tempIndex > 20){
                var w1 = tempList[tempIndex - 1];
                var w2 = tempList[tempIndex - 2];
                var w3 = tempList[tempIndex - 3];
                var w4 = tempList[tempIndex - 4];
                var w5 = tempList[tempIndex - 5];
                var w6 = tempList[tempIndex - 6];
                var w7 = tempList[tempIndex - 7];
                var w8 = tempList[tempIndex - 8];
                var w9 = tempList[tempIndex - 9];
                var w10 = tempList[tempIndex - 10];
                var w11 = tempList[tempIndex - 11];
                var w12 = tempList[tempIndex - 12];
                var w13 = tempList[tempIndex - 13];
                var w14 = tempList[tempIndex - 14];
                var w15 = tempList[tempIndex - 15];
                var w16 = tempList[tempIndex - 16];
                var w17 = tempList[tempIndex - 17];
                var w18 = tempList[tempIndex - 18];
                var w19 = tempList[tempIndex - 19];
                var w20 = tempList[tempIndex - 20];
                if(w1>30000 && w1 == w3 && w3 == w5 && w5==w7 && w7==w9 && w9==w11 && w11==w13 && w13==w15 && w15==w17 && w17==w20){
                    isStable=true;
                    console.log('isStable is true!');
                }
            }
        }

        //自动过磅
        if (isStable == true && v != null && v != "") {
            isFinish=false;
            isStable=false;
            //一次过磅
            console.log('重量稳定，车牌号不为空');
            console.log(state);
            if (state == 0 || state == "") {
                console.log('===============一次过磅');
                //如果一次过磅重量在80000~120000之间，系统自动过磅
                if (weight >= 80000 && weight <= 130000) {
                    saveWeight();

                    console.log('一次自动过磅成功');
                }
            }
            //二次回皮
            if (state == 1) {
                jp.get("${ctx}/weight/weight/checkWeight?consignId="+consignId,function (data) {
                    if(data.success){
                        var bd = data.data;
                        var grossTime = new Date(bd.grosstime).getTime();
                        if(grossTime!=null && grossTime>0){
                            currentTime=Date.now();
                            if(currentTime-grossTime<=600000){
                                jp.alert("一次过磅与二次过磅时间间隔小于10分钟，不能自动过磅！");
                            }else{
                                console.log('================二次过磅');
                                //如果二次过磅重量在40000~60000之间，系统自动过磅
                                if (weight >= 40000 && weight <= 60000) {
                                    saveWeight();
                                    console.log('二次自动过磅成功');
                                }
                            }
                        }
                    }else{
                        jp.alert(data.msg);
                    }
                })


            }
        }

        if (value > 30000) {
            jp.get("${ctx}/weightmonitor/weightmonitor/saveWeightData?weightValue=" + value + "&vehicleNo=" + v, function (data) {
                console.log(data.msg);
            });
        }

    }


}

function txjks() {
    device.Route.set({
        position: "Z01",
        isclose: true,
        data: "{key:'guidaohengtixing',value:'007'}",
        callback: function (data) {
        }
    });
}


//刷新列表
function refresh() {
    $('#consignTable').bootstrapTable('refresh');
}

function view(id) {//没有权限时，不显示确定按钮
    if (id == undefined) {
        id = getIdSelections();
    }
    jp.openViewDialog('查看委托单', "${ctx}/consign/consign/form?id=" + id, '1000px', '500px');
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


function binding() {
    console.log("开始读卡");
    //打开RFID读卡器301
    device.RFID.open({
        position: "301",
        //isclose:false,	//执行完毕后不关闭连接
        callback: function (data, ws) { //回调函数
            //打开rfid
            var dkrfiddata = eval("(" + data + ")");
            //返回rfid状态
            var dkrfid = dkrfiddata.Data;
            if ("2" == dkrfid || "1" == dkrfid) {
                //获取数据
                device.RFID.getdata({
                    position: "301",
                    isclose: false,	//执行完毕后不关闭连接
                    count: 0,	//获取数据次数，0 为一直获取
                    interval: 500,	//获取数据时间间隔(1000毫秒)
                    data: "{istid:true}",
                    callback: function (sjdata, ws) {
                        //获取rfid
                        var data = eval("(" + sjdata + ")");
                        //返回rfid数据
                        var rfid = data.Data;
                        //console.log("#########rfid"+rfid);
                        var v = $("#vehicleNo").val();
                        if (rfid == "-1" && v == "强制过磅") {
                            document.getElementById("vehicleNo").value = "";
                        }
                        if (typeof rfid != "undefined" && rfid != "" && rfid != "-1" && rfid != "1" && rfid != "2") {

                            if (oldrfid != rfid && weight > 1000) {
                                oldrfid = rfid;
                                console.log("#########ip" + device.ip);
                                $('#dy').attr("disabled", false);
                                listnum = true;
                                console.log("#########" + listnum);
                                plateRecogn("", rfid);
                            }


                        }
                    }
                });
            } else {
                console.log("RFID:" + dkrfiddata.Describe + "异常RFID:301");
            }
        }
    });
}

function plateRecogn(val, rfid) {

    /* if(hwStatus){
         if (!hwstate) {
             jp.error("未完全上磅,请移动车辆");
             return;
         }
     }*/
    var result = val;
    if (val != "") {
        result = val.replace('#', '%23');
    }
    console.log("是否存在列表" + listnum);
    if (listnum) {
        jp.get("${ctx}/weight/weight/checkConsignGdh?rfidNo=" + rfid + "&ladleNo=" + result, function (data) {
            if (data.success) {
                ip=data.object;
                var wei = data.data;
                document.getElementById("vehicleNo").value = wei.ladleNo;
                $('#vehicleNo').attr("disabled", true);
                console.log("修改车牌=========" + wei.ladleNo);
                if (wei.ladleNo != null && wei.ladleNo != "") {
                    $("#SladleNo").val(wei.ladleNo);
                }
                if (rfid != null && rfid != "") {
                    $("#SrfidNo").val(rfid);
                }
                refresh();
                //console.log("识别到卡号"+rfid+"%%%%%%%%%铁水罐号"+wei.ladleNo);
                state = wei.weightState;
                isFinish = true;
                Cid = wei.id;
                consignId = wei.consignId;
                BFNo = wei.blastFurnaceNo;
                console.log("ip================" + device.ip);
                if (ip != "") {
                    device.ip = ip;
                }
                if (weight > 1000) {
                    setRG_R2("901", '1#OFF');
                    setRG_R2("901", '2#OFF');
                }

                listnum = false;
                device.Route.set({
                    position: "Z01",
                    isclose: true,
                    data: "{key:'fanxiansschehao',value:'" + wei.ladleNo + "'}"
                });
                console.log("识别到卡号" + rfid + "%%%%%%%%%铁水罐号" + wei.ladleNo + "变红灯"+Date.now());
                // isRed=true;
            } else {
                jp.error(data.msg);
            }
        })

    }

}

function reload() {
    $('#vehicleNo').attr("disabled", false);
    $('#dy').attr("disabled", true);
    console.log("device.ip=================" + device.ip);
    if (ip != "") {
        device.ip = ip;
    }
    weightnum = new Array();
    tempunm = 0;
    selectdata = null;
    spConfig = "";
    hwstate = true;
    hwStatus = true;
    weight = 0;
    hldstate = "";
    listnum = false;
    oldrfid = "";
    state = "";
    Cid = "";
    BFNo = "";
    tempList=[];
    tempIndex = 0;
    isStable = false;
    isFinish = true;
    consignId = "";
    // isRed=false;
    setRG_G("901", '1#ON');
    setRG_G("901", '2#ON');
    $("#vehicleNo").val("");
    $("#SrfidNo").val("");
    $("#SladleNo").val("");
    refresh();
}

function zp(weighNo) {
    jp.get("${ctx}/videos/videos/queryVideoCamera", function (data) {
        if (data.success) {
            var v = data.data;
            spConfig = v.videoCamera;
            console.log("抓拍！！！！！！！！！！！！！！！！！" + spConfig);

            var minecode = 'image';
            var spConfig = spConfig;
            var value = 0;
            device.ip = data.msg;
            device.Video.sendmsg({
                position: "C01",
                data: "{sendmsg:'TakePic," + minecode + "," + spConfig + "'}",
                callback: function (data, ws) {
                    var data = eval("(" + data + ")");
                    value = data.Data;
                    console.log("图片~~~~~~~~~~~~~" + value);
                    if (value != "" && value != "-1") {
                        saveweightpic(value, weighNo);
                    }

                }
            });
        }
    });
}

function saveweightpic(value, weighNo) {
    //获取抓拍图片
    //判断稳定后获取抓拍.
    console.log("@@@@@@@@@@@@@@@@@@@@@");
    id = getIdSelections();
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
    console.log("===========" + ct)
    console.log("===========" + cw)
    console.log("===========" + cd)

    jp.get("${ctx}/weight/weight/saveweightpic?weighNo=" + weighNo + "&ct=" + ct + "&cw=" + cw + "&cd=" + cd, function (data) {
    })

}

var oldcg3 = "FE 02 01 0F D1 98 ";
var oldcg1 = "FE 02 01 0D 50 59 ";
var oldcg2 = "FE 02 01 0B D0 5B ";
var oldcg4 = "FE 02 01 09 51 9A ";

//获取磁钢状态
function rglmonitor() {
    device.RGLight.monitor({
        position: "903",
        interval: 10,   //无效 仅当获取数据的时候有??  获取数据间隔
        //无效 仅当获取数据的时候有??  获取数据次数
        count: 0,
        isclose: false,
        callback: function (data, ws) {

            if (data.length > 15) {
                var data_m = eval("(" + data + ")");
                var mvalue = data_m.Data;
                var cal = mvalue.substring(0, 18);

                //console.log(cal + "开启磁钢-------------");
                if (cal == oldcg1 || cal == oldcg2 || cal == oldcg3 || cal == oldcg4) {
                    hwstate = true;
                } else {
                    hwstate = false;
                }
            }
        }
    });
}

//打印票据
function dypj(print) {
    console.log("打印票据开始" + Date.now());

    layer.close(index);
    if (print == "") {
        console.log("票据数据为空")
        return;
    }

    if (typeof (print.supplierName) == "undefined") {
        print.supplierName = ""
    }
    if (typeof (print.consigneUser) == "undefined") {
        print.consigneUser = ""
    }
    if (typeof (print.vehicleNo) == "undefined") {
        print.vehicleNo = ""
    }
    if (typeof (print.prodCname) == "undefined") {
        print.prodCname = ""
    }
    if (typeof (print.workStation) == "undefined") {
        print.workStation = ""
    }
    if (typeof (print.defaultFlag) == "undefined") {
        print.defaultFlag = ""
    }
    var pria = print.supplierName;
    var pric = print.consigneUser;
    var prib = "";
    var prid = "";
    var prie = "";
    var prif = "";

    if (pria.length > 12) {
        prib = pria.substring(0, 12)
        prid = pria.substring(12, pria.length)
    } else {
        prib = pria;
        prid = "";
    }
    if (pric.length > 12) {
        //b=b.substring(0,12)+"\n"+b.substring(12,b.length)
        prie = pric.substring(0, 12)
        prif = pric.substring(12, pric.length)
    } else {
        prie = pric;
        prif = "";
    }

    if (print.defaultFlag == 1) {
        var def = "锁皮";
    } else {
        def = "";
    }

    if (typeof (print.supplierName) == "undefined") {
        print.supplierName = ""
    }
    if (typeof (print.consigneUser) == "undefined") {
        print.consigneUser = ""
    }
    if (typeof (print.vehicleNo) == "undefined") {
        print.vehicleNo = ""
    }
    if (typeof (print.prodCname) == "undefined") {
        print.prodCname = ""
    }
    if (typeof (print.workStation) == "undefined") {
        print.workStation = ""
    }
    if (typeof (print.defaultFlag) == "undefined") {
        print.defaultFlag = ""
    }
    var qrcode = print.weighNo.toString()

    var printdata = "{'header':'宝钢德盛不锈钢"
        + "','header2':'有限公司"
        + "','header3':'物料签收单"
        + "','grossweighttime':'日期:" + (new Date(print.grosstime)).Format("yyyy/MM/dd/ HH:mm")
        + "','vehicleno':'车号:" + print.vehicleNo
        + "','prodcname':' 货物名称  " + print.prodCname
        + "','grossname':' 毛重(Kg) ','grossweight':'  " + (isNaN(parseInt(print.matGrossWt)) ? "" : parseInt(print.matGrossWt))
        + "','tarename':' 皮重(Kg) ','tareweight':' " + (isNaN(print.impWt) ? "" : parseInt(print.impWt))
        + "','standardname':' 净重(Kg) ','standardweight':' " + (isNaN(print.matWt) ? "" : print.matWt)
        + "','custname':' 发货单位  " + prib
        + "','custname2':'           " + prid
        + "','recustname':' 收货单位  " + prie
        + "','recustname2':'           " + prif
        + "','unloadingaddress':'装卸货地址 "
        + "','receiver':' 签 收 人 "
        + "','weighNo':'" + print.weighNo.toString()
        + "','qrcode':'" + qrcode
        + "','def':'" + def
        + "','workStation':' " + print.workStation
        + "',_file_:'billweight.xml'}";
    device.ip = "10.12.241.161";
    var ip = device.ip;
    printaa(printdata, print.weighNo, ip, "1");

}

//打印磅单
function dybd(print) {
    //上传磅单
    jp.get("${ctx}/weight/weight/uploadWeight?weighNo=" + print.weighNo, function (data) {
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
    console.log("打印磅单开始" + Date.now());

    if (print == "") {
        console.log("磅单数据为空")
        return;
    }
    if (typeof (print.supplierName) == "undefined") {
        print.supplierName = ""
    }
    if (typeof (print.consigneUser) == "undefined") {
        print.consigneUser = ""
    }
    if (typeof (print.vehicleNo) == "undefined") {
        print.vehicleNo = ""
    }
    if (typeof (print.blastFurnaceNo) == "undefined") {
        print.blastFurnaceNo = ""
    }
    if (typeof (print.prodCname) == "undefined") {
        print.prodCname = ""
    }
    if (typeof (print.workStation) == "undefined") {
        print.workStation = ""
    }
    var vehicleNo = print.vehicleNo + "   " + print.blastFurnaceNo;
    var pria = print.supplierName;
    var pric = print.consigneUser;
    var prib = "";
    var prid = "";
    var prie = "";
    var prif = "";

    if (pria.length > 12) {
        prib = pria.substring(0, 12)
        prid = pria.substring(12, pria.length)
    } else {
        prib = pria;
        prid = "";
    }
    if (pric.length > 12) {
        //b=b.substring(0,12)+"\n"+b.substring(12,b.length)
        prie = pric.substring(0, 12)
        prif = pric.substring(12, pric.length)
    } else {
        prie = pric;
        prif = "";
    }

    var qrcode = "磅单号：" + print.weighNo.toString()
        + ",货物名称:" + print.prodCname
        + ",毛重(Kg):" + (isNaN(parseInt(print.matGrossWt)) ? "" : parseInt(print.matGrossWt))
        + ",皮重(Kg):" + (isNaN(parseInt(print.impWt)) ? "" : parseInt(print.impWt))
        + ",净重(Kg):" + (isNaN(parseInt(print.matWt)) ? "" : parseInt(print.matWt))

    var printdata = "{'qrcode':'" + qrcode
        + "','header':' 宝钢德盛不锈钢有限公司"
        + "','header2':' 物料检斤单"
        + "','grossweighttime':'日期:" + (new Date(print.taretime)).Format("yyyy年MM月dd日")
        + "','time':'时间:" + (new Date(print.taretime)).Format("HH:mm:ss")
        + "','vehicleno':'车   号     " + vehicleNo
        + "','prodcname':'货物名称    " + print.prodCname
        + "','grossname':' 毛重(Kg)','grossweight':' " + (isNaN(parseInt(print.matGrossWt)) ? "" : parseInt(print.matGrossWt))
        + "','tarename':' 皮重(Kg)','tareweight':' " + (isNaN(parseInt(print.impWt)) ? "" : parseInt(print.impWt))
        + "','standardname':' 净重(Kg)','standardweight':' " + (isNaN(parseInt(print.matWt)) ? "" : parseInt(print.matWt))
        + "','custname':'发货单位    " + prib
        + "','custname2':'            " + prid
        + "','recustname':'收货单位    " + prie
        + "','recustname2':'            " + prif
        + "','weighNo':'磅单号:" + print.weighNo.toString()
        + "','workStation':' " + print.workStation
        + "',_file_:'weight.xml'}";

    device.ip = "10.12.241.161";
    var jkip = device.ip;
    //集控室打印
    printaa(printdata, print.weighNo, jkip, "1");
}

function changePrint(weighNo, ip, flag) {

    jp.get("${ctx}/weight/weight/changePrint?weighNo=" + weighNo + "&ip=" + ip + "&flag=" + flag, function (data) {
        if (data.success) {
            console.log("打印结束" + Date.now())
            $('#consignTable').bootstrapTable('refresh');
        }
    });
}

</script>

