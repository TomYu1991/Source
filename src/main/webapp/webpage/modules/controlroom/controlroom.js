<%@ page contentType="text/html;charset=UTF-8" %>
    <script>
    var loginip = "";
    var port = "";
    var username = ""
    var password = "";
    var cameraOrder = "";
    var channelNum="";
    var pic;//用于接收弹出图片的url
    var hldstate ="";
    var yb="101";
    var led="401";
    var yy="601";
$(document).ready(function() {
    //轨道衡提醒
    var weight = JSON.parse(localStorage.getItem("weight"))

    if(weight!=null){
        $("#station").val(weight.stationId);
        device.ip = weight.deviceId;
        $("#weighNo").val(weight.weighNo);
        $("#blastFurnaceNo").val(weight.blastFurnaceNo);
        $("#consigneUser").val(weight.consigneUser);
        $("#supplierName").val(weight.supplierName);
        $("#consignId").val(weight.consignId);
        $("#oldImpWt").val(weight.oldImpWt);
        $("#rfidNo").val(weight.rfidNo);
        $("#impWt").val(weight.impWt);
        $("#matGrossWt").val(weight.matGrossWt);
        $("#prodCname").val(weight.prodCname);
        $("#vehicleNo_txt").val(weight.vehicleNo);
        $("#vehicleNo_txt").attr("readonly",true);
        $("#matWt").val(weight.matWt);
        pic = weight.billPic;
        document.getElementById("picture1").src = weight.billPic;
        getVideo(weight.stationId)
    }else{
        device.ip =  localStorage.getItem("ip");
        console.log("change"+device.ip)
        $("#station").val(localStorage.getItem("station"));
        var a = localStorage.getItem("station")
        getVideo(a)
    }
    // gdhtx();
    dqsj("101");
    var queueNum = 0;
    $("#queueNum").hide();
    var seatName=$("#seatName").val();
    if(seatName!=""){
        var timer = setInterval(function ()   //刷新排队页面
        {
            jp.get("${ctx}/controlqueue/controlQueue/quereState", function(data){
                if(data.success) {
                    var queue  = data.data;
                    var Num = queue.queueNum;
                    if(Num>queueNum){
                        jp.toastr.warning("有新的请求！请及时处理！");
                        gdhVoice();
                    }
                    queueNum=Num;
                    document.getElementById("dqpd").value="当前排队 "+queue.queueNum;
                }

            })
        }, 1000);
    }

});
function gdhtx() {
    device.Route.get({
        position:"Z01",
        data:"{key:'guidaohengtixing'}",
        isclose:false,
        count:0,
        interval:100,
        callback:function(data){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            if (value.length>0&&value != -1) {
                console.log("轨道衡提醒")
                jp.alert("轨道磅有车辆上磅！请及时处理");
                jp.gdhVoice();
                device.Route.set({
                    position:"Z01",
                    data:"{key:'guidaohengtixing',value:''}",
                    callback:function(data){
                    }
                });
            }
        }
    });
}
//排队列表
function queuelist() {
    $("#video").css('display','none');
    top.layer.open({
        type: 2,
        area: ["1000px", "500px"],
        title: "排队列表",
        auto:true,
        maxmin: true, //开启最大化最小化按钮
        content: "${ctx}/controlqueue/controlQueue/list" ,
        btn: ['关闭'],
        yes:function(index, layero) {
            $("#video").css('display','block');
            var a = localStorage.getItem("station")
            getVideo(a);
            top.layer.closeAll();
        },
        cancel: function(index){
            $("#video").css('display','block');
            var a = localStorage.getItem("station")
            getVideo(a)
        }
    });
    //jp.openViewDialog('排队列表', "${ctx}/controlqueue/controlQueue/list", '1000px', '500px');
}


function sure() {
    var id = $("#station").val();
    localStorage.clear();
    if(typeof id != "undefined" && id != null && id != ""){
    jp.get("${ctx}/controlroom/controlroom/sure?id=" + id, function(data) {
        if (data.success) {
            localStorage.setItem("ip",data.msg);
            localStorage.setItem("station",id)
            window.location.reload();
            //获取仪表数据，填入页面
            dqsj(yb);
        }
    })
    }
}
//接收仪表的值
function meter(value){
    document.getElementById("weight").value=value;
    if (parseFloat(value) >100 && parseFloat(value)>0 ) {

        if (hldstate != "red" || hldstate == "") {
            hldstate = "red";
            hqch();//返显车牌号
        }
    }
}

//发送语音
function sendyy() {
    var msg = $("#yyts").val();
    device.TTS.sendmsg({
        position:yy,
        isclose:true,
        data:"{'sendmsg':'"+msg+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            if(value>0){
                jp.alert("发送语音成功")
            }else{
                jp.alert("发送语音失败")
            }
        }
    });
}
//发送LED
function sendled() {
    var msg = $("#ledts").val();
    device.LED.sendmsg({
        position:led,
        isclose:true,
        data:"{'sendmsg':'"+msg+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            if(value>0){
                jp.alert("发送LED成功")
            }else{
                jp.alert("发送LED失败")
            }
            console.log("led"+new Date()+msg)
        }
    });
}
//仪表清零
function ybql() {
    zero();
}

//视频加载
function spjz(){
    $("#station").val(localStorage.getItem("station"));
    var a = localStorage.getItem("station");
    //alert(a)
    getVideo(a);
}
//刷新和修改车牌公共方法
function common(xgcph) {
     //device.ip = '127.0.0.1'

    if(device.ip == "" || device.ip == null){
        jp.info("请选择工作站");
        return;
    }
    if(xgcph==null|| typeof(xgcph) == "undefined"){
        xgcph="";
    }
    //gg  表示 控制 过磅页面刷新 还是设置车号标致
     device.Route.set({
        position:"Z01",
        isclose:true,
        data:"{key:'xiugaichehao',value:'"+xgcph+"'}",
        callback:function(data){
            console.log("修改车牌号"+xgcph);
        }
    });

}
// 返显车牌号
function hqch() {
    device.Route.get({
        position:"Z01",
        data:"{key:'fanxiansschehao'}",
        isclose:false,
        count:0,
        interval:100,
        callback:function(data){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            if (value.length>0&&value != -1) {
                    document.getElementById("fxvehicleNo").value=value;
                device.Route.set({
                    position:"Z01",
                    data:"{key:'fanxiansschehao',value:''}",
                    callback:function(data){
                    }
                });
            }
        }
    });
}

//修改坐席状态
function updateSeatState() {
    var seatState = $("#seatState").val();
    jp.get("${ctx}/controlroom/controlroom/updateSeatState?seatState=" + seatState, function(data){
        if(data.success){
           jp.success(data.msg);
        }else{
            jp.error(data.msg);
        }
    })
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

//打印磅单
function dybd(){
    device.Route.set({
        position:"Z01",
        isclose:true,
        data:"{key:'xiugaichehao',value:'006'}",
        callback:function(data){
        }
    });

        $("#dybd").attr("disabled",true);
            var print = JSON.parse(localStorage.getItem("weight"));
            //上传磅单
            jp.get("${ctx}/weight/weight/uploadWeight?weighNo="+print.weighNo, function(data){
                if(data.success){
                    jp.success(data.msg);
                }else{
                    jp.alert(data.msg);
                }
            });
            //打印磅单
            if(print==""){
                console.log("数据为空");
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
            if(typeof(print.blastFurnaceNo) == "undefined"){
                print.blastFurnaceNo =""
            }
            if(typeof(print.prodCname) == "undefined"){
                print.prodCname =""
            }
            if(typeof(print.workStation) == "undefined"){
                print.workStation =""
            }
            var vehicleNo =print.vehicleNo +"   "+ print.blastFurnaceNo;
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

            var qrcode ="磅单号："+print.weighNo.toString()
                + ",货物名称:"+print.prodCname
                + ",毛重(Kg):"+(isNaN(parseInt(print.matGrossWt)) ? "" : parseInt(print.matGrossWt))
                + ",皮重(Kg):"+(isNaN(parseInt(print.impWt)) ? "" : parseInt(print.impWt))
                + ",净重(Kg):"+(isNaN(parseInt(print.matWt))? "" : parseInt(print.matWt))

            var printdata = "{'qrcode':'" + qrcode
                + "','header':' 宝钢德盛不锈钢有限公司"
                + "','header2':' 物料检斤单"
                + "','grossweighttime':'日期:" + (new Date(print.taretime)).Format("yyyy年MM月dd日")
                + "','time':'时间:"+ (new Date(print.taretime)).Format("HH:mm:ss")
                + "','vehicleno':'车   号     "+ vehicleNo
                + "','prodcname':'货物名称    "+print.prodCname
                + "','grossname':' 毛重(Kg)','grossweight':' "+(isNaN(parseInt(print.matGrossWt)) ? "" : parseInt(print.matGrossWt))
                + "','tarename':' 皮重(Kg)','tareweight':' "+(isNaN(parseInt(print.impWt)) ? "" : parseInt(print.impWt))
                + "','standardname':' 净重(Kg)','standardweight':' "+(isNaN(parseInt(print.matWt))? "" : parseInt(print.matWt))
                + "','custname':'发货单位    "+prib
                + "','custname2':'            "+prid
                + "','recustname':'收货单位    "+prie
                + "','recustname2':'            "+prif
                + "','weighNo':'磅单号:"+print.weighNo.toString()
                + "','workStation':' "+print.workStation
                + "',_file_:'weight.xml'}";
            device.ip=print.secondStation;
            var ip =device.ip;
            if( device.ip != "10.12.242.40"&&device.ip != "10.12.242.41"){
                printaa(printdata,print.weighNo,ip,"1");
            }else{
                fssjyy("601", "下磅,下磅,下磅")
            }

           device.ip = "10.12.241.161";
            var jkip = device.ip;
            //集控室打印
            printaa(printdata,print.weighNo,jkip,"1");
    localStorage.clear();
}

    function changePrint(weighNo,ip,flag){
        var weighNo  = $('#weighNo').val();
        console.log("开始保存日志----------------"+Date.now())

        jp.get("${ctx}/weight/weight/changePrint?weighNo="+weighNo+"&ip="+ip+"&flag="+flag, function (data) {
            if (data.success) {
                console.log("打印磅单完成----------------"+Date.now())
            }
        });
    }


//登陆视频
function loginvideo(){
    var ret = clickLogin(loginip,port,username,password);
    openvideo()
    //setTimeout("openvideo()", 300);

}
//关闭视频
function closevideo(){
    for (var i=0;i<4;i++ ){
            g_iWndIndex=i;
            if (i==0){clickStopRealPlay();}
            if (i==1){clickStopRealPlay();}
            if (i==2){clickStopRealPlay();}
            if (i==3){clickStopRealPlay();}
        myclickStopRealPlay(i);
    }
    clickLogout(loginip);
}
//打开视频
function openvideo(){
    var channels=channelNum.split(",");
    for (var i=0;i<channels.length;i++ ){
        g_iWndIndex=i;
        if (i==0){ clickStartRealPlay(channels[i],0);}
        if (i==1){ clickStartRealPlay(channels[i],1); }
        if (i==2){ clickStartRealPlay(channels[i],2);}
        if (i==3){ clickStartRealPlay(channels[i],3);}
    };

}
//关闭视频
function closevideo(){
    for (var i=0;i<4;i++ ){

        myclickStopRealPlay(i);
    }
    clickLogout(loginip);
}
// 获取视频
function getVideo(id){
    console.log(id)
    var dbid =id
    jp.get("${ctx}/videos/videos/getvideo?dbid="+dbid, function(data){

        if(data.success){
            console.log(id)
            console.log(data.data)
            var videos = data.data;
                loginip = videos.cameraIp;
                port = videos.cameraPort;
                username = videos.username;
                password = videos.password;
                cameraOrder = videos.cameraOrder;
                channelNum = videos.videoCamera;
                device.ip = videos.stationIp;
            closevideo();
            loginvideo()
            //window.setTimeout(function () { loginvideo(); }, 500);
        }else{
            jp.error(data.msg);
        }
    })
}

function show() {
    var value = $("#picture").val();
    jp.showPic(value);
}
/*$('#vehicleNo').editableSelect({
    effects: 'slide'
});*/
//清除缓存
function clear() {
    for(var key in obj){
        var t= Date.now()-obj[key];
        if(t>30000){
            delete obj.key;
        }
    }
    window.setTimeout("clear()",10000);
}

function openRed() {
    setRG_R2("901", '1#OFF');
    setRG_R2("901", '2#OFF');
    alert("打开红灯成功");
}
function openGreen() {
    setRG_G("901", '1#ON');
    setRG_G("901", '2#ON');
    alert("打开绿灯成功");

}

function gdhVoice() {
    var audio = document.createElement("audio");
    audio.src = ctxStatic+"/common/voice/40509.mp3";
    audio.play();
}
</script>