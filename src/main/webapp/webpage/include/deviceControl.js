
/*************************************************************************
 * 道闸    目前支持的设备 :
 新红门主板,红门主板,东度主板(艾威尔主板)
 ************************************************************************/
function kqdz(str,id){
    var value=0;
    device.GATEWAY.open({
        position:str,
        callback:function(data,ws){ //回调函数
            var data_ = eval("("+data+")");
             value = data_.Data;
            var describe = data_.Describe;

        }
    });
    return value;
}

function sdz(str,id){

    device.GATEWAY.up({
        position:str,
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            console.log("道闸"+new Date()+str);
        }
    });
}

function ldz(str,id){
    alert("落道闸");
    var i=0,j=0;
    device.GATEWAY.down({
        position:str,
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            if (value==1) {
                alert("成功");
            }
            if (value<0) {
                alert("失败");
                alert(data.Describe);
            }
        }
    });
}

function  gbdz(str) {
    alert("关闭道闸")
    device.GATEWAY.close({position:str,callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
            if (value==1) {
                alert("成功");
            }
            if (value<0) {
                alert("失败");
            }
        }
    });
}
/***************************************************************
 *刷卡器_MWRF35 -开启刷卡器
 ****************************************************************/
function kqdkq(str,s){
    device.MWRF35.open({
        position:str,
        isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            document.getElementById(s).innerHTML= (value);
            document.getElementById('z').innerHTML= (describe);
        }
    });
}

function dk(str,s){
    device.MWRF35.getid({
        position:str,
        isclose:false,	//执行完毕后不关闭连接
        count:0,		//获取数据次数，0 为一直获取
        interval:50,	//获取数据时间间隔(50毫秒)
        data:"{docardmodel:1,convert:1}", //convert:1全部是正的卡号;docardmodel:0只获取一次卡号，需要拿卡才可获取卡号
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            document.getElementById(s).innerHTML= (value);
            document.getElementById('z').innerHTML= (describe);
        }
    });
}
//江苏灌装写卡
function gzxk(str,s){
    var xlh=document.getElementById("gzxlh").value;
    if(""==xlh){
        alert("序列号不能为空"); return false;
    }
    var hexxlh=parseInt(xlh).toString(16);
    //alert(hexxlh.length);
    var hexxlhstr=""+hexxlh;

    for(var i=0;i<8-hexxlh.length;i++){
        hexxlhstr="0"+hexxlhstr;
    }
    //alert(hexxlhstr);
    var yzl=document.getElementById("gzyzl").value;
    if(""==yzl){
        alert("预装量不能为空"); return false;
    }
    var hexyzl=(parseInt(yzl)*1000).toString(16);
    var hexyzlstr=""+hexyzl;
    for(var i=0;i<8-hexyzl.length;i++){
        hexyzlstr="0"+hexyzlstr;
    }
    var hw=document.getElementById("gzhw").value;
    if(""==hw){
        alert("鹤位不能为空"); return false;
    }else if(hw.length!=4){
        alert("鹤位编码长度必须为4位"); return false;
    }
    //076A+装车序号（4字节）+鹤位代码（双字节 0000~0008）+预装数量（四字节）+00000000
    var icstring="076a"+hexxlhstr+hw+hexyzlstr+"00000000";
    device.MWRF35.seticbyte({
        position:str,
        isclose:false,
        //interval:1000,
        count:1,
        data:"{sec:2,block:1,icstring:'"+icstring+"',passmodel:4,password:'FFFFFFFFFFFF'}",
        callback:function(data){
            var wtresult=eval("("+data+")");
            if("1"==wtresult.Data){
                //写卡成功
                document.getElementById(s).innerHTML= (wtresult.Data);
                document.getElementById('z').innerHTML= (wtresult.Describe);
            }else{
                //写卡失败
                document.getElementById(s).innerHTML= (wtresult.Data);
                document.getElementById('z').innerHTML= (wtresult.Describe);
            }
        }
    });
}

function setIC(str,s){
    device.MWRF35.setic({
        position:str,
        isclose:false,
        data:"{'sec':3,'block':1,'icstring':'2#'}",
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            document.getElementById(s).innerHTML= (value);
            document.getElementById('z').innerHTML= (describe);
        }
    });
}

function gb(str,s){
    device.MWRF35.close({
        position:str,
        callback:function(data,ws){ //回调函数
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            document.getElementById(s).innerHTML= (value);
            document.getElementById('z').innerHTML= (describe);
        }
    });
}

/**************************************************************************
 *仪表 -开启仪表
 *************************************************************************/
function kqyb(str,id){
    var value = 0;
    device.Meter.open({
        position:str,
        isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
        }
    });
    return value;
}
function dqsj(str,id){
    var value=0;
    device.Meter.getdata({
        position:str,
        isclose:false,	//执行完毕后不关闭连接
        count:0,		//获取数据次数，0 为一直获取
        interval:500,	//获取数据时间间隔(500毫秒)
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            value = data_.Data;
                meter(value);
        }
    });
    //return value;
}
function gbyb(str,id){
    alert("关闭仪表");
    device.Meter.close({
        position:str,
        callback:function(data,ws){ //回调函数
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            if (value==1) {
                alert("成功");
            }
            if (value<0) {
                alert("失败");
                alert(data.Describe);
            }
        }
    });
}
//仪表清零
function zero(){
    //给仪表发送清零的指令
        device.Meter.ybclear({
            position:"101",
            data:"{cmd:'5A'}",
            callback:function(data,ws){
            }
        });
        alert("清零指令已发送");
}

/* ***********************************************************
 *	红绿灯--继电器  1#ON 一路开，绿灯，1#OFF一路关，红灯
 ************************************************************/
function kqRG(str,str2,id){
  var value = 0;
    device.RGLight.open({
        position:str,
        callback:function(data,ws){
            var data_ = eval("("+data+")");
             value = data_.Data;
            var describe = data_.Describe;
            if (value<0) {
                alert(data.Describe);
            }
        }
    });
    return value;
}
function setRG_R(str,str2,id){

    device.RGLight.setrgl({
        position:str,
        data:"{'data':'"+str2+"'}",
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            console.log("红灯1")
            setTimeout("setRG_G('901', '1#ON')",15000);
}
    });
}
function setRG_R2(str,str2,id){
    device.RGLight.setrgl({
        position:str,
        data:"{'data':'"+str2+"'}",
        callback:function(data,ws){
        }
    });
}
function setRG_G(str,str2,id){
    device.RGLight.setrgl({
        position:str,
        data:"{'data':'"+str2+"'}",
        callback:function(data,ws){
        }
    });
}
function setRG_G2(str,str2,id){

    device.RGLight.setrgl({
        position:str,
        data:"{'data':'"+str2+"'}",
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;

        }
    });
}
function gbRG(str,id){
    alert("关闭红绿灯");
    device.RGLight.close({
        position:str,
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;

            if (value<0) {
                alert(data.Describe);
            }
        }
    });
}


/* ***********************************************************
 *	红外  普通类型 type : 1   dllpath: 1  classname : 1
目前支持的设备 :4144开关量采集模块 ,目前主板的GPIO模块,新主板的打印机接口GPIO模块
 ************************************************************/
function kqHW(str){
    var value = 0;
    device.HWGPIO.open({
        position:str,
        isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            var data_ = eval("("+data+")");
             value = data_.Data;
            var describe = data_.Describe;

        }
    });
    return value;
}

function dqsjHW(str){

    var value=0;
    device.HWGPIO.gethwalldata({
        position:str,
        isclose:false,	//执行完毕后不关闭连接
        count:0,		//获取数据次数，0 为一直获取
        interval:500,	//获取数据时间间隔(50毫秒)
        data:"{'count':'8'}", //count 是获取前几个红外的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            value = data_.Data;
            var count = 0;
            while (value.indexOf("1") != -1) {
                var index = value.indexOf("1");
                value = value.substr(index+1);
                count++
            }
            if (count == 2) {
                hwstate = true;
            } else {
                hwstate= false;
            }
            //console.log("==================="+hwstate)
        }
    });
    return value;
}

function gbHW(str){
    alert("关闭红外");
    var i = 0;
    device.HWGPIO.close({
        position:str,
        callback:function(data,ws){ //回调函数
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            if (value==1) {
                alert("成功");
            }
            if (value<0) {
                alert("失败");
                alert(data.Describe);
            }
        }
    });
}

/* ***********************************************************
 *	LED -发送数据
 ************************************************************/
var start;
function fsLED1(str,msg){

    device.LED.sendmsg({
        position:str,
        isclose:true,
        data:"{'sendmsg':'"+msg+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            console.log("led"+new Date()+msg)
        }
    });
}

/* ************* *********************************************
 *	语音
 ************************************************************/
function fssjyy(str,msg){

    device.TTS.sendmsg({
        position:str,
        isclose:true,
        readmodel:0,
        data:"{'sendmsg':'"+msg+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            if(value>0){
                console.log("发送成功！")
            }
        }
    });
}

/***************************************************************
 *  视频抓拍图片 海康 var spConfig = '0|1#1|1#2|1#3|1#5|1#6|1#7|1#8|1';
 **************************************************************/
function zhuapai(str,spConfig){

    console.log("抓拍");
    console.log(spConfig)
    var minecode = 'image';
    var spConfig = spConfig;
    var value=0;
    device.Video.sendmsg({
        position:str,
        data:"{sendmsg:'TakePic,"+minecode+","+spConfig+"'}",
        callback:function(data,ws){
            var data = eval("("+data+")");
            value = data.Data;
            console.log("视频抓拍"+Date.now())
            url(value);
        }
    });
}

/***************************************************************
 *   短信
 **************************************************************/
function opensms1(){

}
function sendsms(){
    var pnum = document.getElementById("pnum").value;
    var sendsm = document.getElementById("sendsm").value;
    device.SMS.sendmsg({
        position:"D01",
        isclose:true,
        data:"{num:'"+pnum+"',sendmsg:'"+sendsm+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            document.getElementById('z').innerHTML= (data);
        }
    });
}22//

/***************************************************************
 *   工作站
 **************************************************************/
function getGZZ(){
    device.Command.method({
        callback:function(data){//回调函数
            i++;
            document.getElementById('o').innerHTML= (data +i) ;
        }
    });
}

/***************************************************************
 *   工作站
 **************************************************************/
function getGZZ(){
    device.Command.method({
        callback:function(data){//回调函数
            i++;
            document.getElementById('o').innerHTML= (data +i) ;
        }
    });
}
/***************************************************************
 *   打印机
 **************************************************************/
var num = 0;
var outside = 0;
function printaa(msg,weighNo,ip,flag){//参数为json格式
    console.log("开始打印");
    device.Print.sendmsg({
        position:"E01",
        data:msg,
        interval:500,
        callback:function (data,ws){
            device.ip = ip;
            var data_ = eval("("+data+")");
            value = data_.Data;
            if(value>0){
                jp.success("打印成功");
                console.log(device.ip + "打印成功"+ip);
                fssjyy("601", "称重完成，请下磅")
                changePrint(weighNo,ip,flag);
            } else {
                if(num >= 2){
                    num=1;
                    jp.confirm('本次打印失败，是否要重新打印？', function(){
                        printaa(msg,weighNo,ip,flag)
                    });
                }else{
                    num++;
                    printaa(msg,weighNo,ip,flag);
                }
            }
        }
    });
   /* outside  ++;
    if(outside  == 2){
        console.log("打印失败！")
        return;
    }*/
}

//开启打印机
function kqprint(str){
    var value = 0;
    device. Printer.open({position:str, callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
        }
    });
    return value;
}
//关闭打印机
function gbprint(str) {
    alert("关闭打印机");
    device. Printer.close({position:str, callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
            if (value==1) {
                alert("成功");
            }
            if (value<0) {
                alert("失败");
            }
        }
    });
}

//开启车号识别
function kqchsb(str){
    var value = 0;
    device. CarLicence.open({position:str,callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
        }
    });
    return value;
}
//关闭车号识别
function gbchsb(str){
    alert("关闭车号识别")
    device. CarLicence.close({position: str,callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
            if (value==1) {
                alert("成功");
            }
            if (value<0) {
                alert("失败");
            }
        }
    });
}
var obj={};
function clear() {
    for(var key in obj){
        var t= Date.now()-obj[key];
        if(t>30000){
            delete obj.key;
        }
    }
    window.setTimeout("clear()",10000);
}
//识别车号
function sbch(str) {
    device.CarLicence.monitor({position:str,isclose:false, interval:200,count:0,
        callback:function(data,ws){
            var data = eval("("+data+")");
            var value = data.Data;
            // value 识别内容
            if(value.length>3){
                value = value.substring(value.lastIndexOf("#") + 1, value.length+1);
                    plateRecognition(value,"",ws);
            }
        }
    })
}


function hqRFID(str){
    var value = 0;
    device.RFID.getdata({position: str,isclose:false, count:0,
        callback:function(data,ws){
            var data = eval("("+data+")");
            // data.Data 获取卡号
            value = data.Data;

        }
    });
    return value;
}
function openRFID(rfidstr){
    //打开RFID读卡器301
    device.RFID.open({
        position:rfidstr,
        //isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            //打开rfid
            var dkrfiddata = eval("("+data+")");
            //返回rfid状态
            var fhrfidzt = dkrfiddata.Data;
            if("2"==fhrfidzt||"1"==fhrfidzt){
                //获取数据
                device.RFID.getdata({
                    position:"301",
                    isclose:false,	//执行完毕后不关闭连接
                    count:0,		//获取数据次数，0 为一直获取
                    interval:1000,	//获取数据时间间隔(1000毫秒)
                    data:"{istid:true}",
                    callback:function(sjdata,ws){
                        //获取rfid
                        var rfiddata = eval("("+sjdata+")");
                        //返回rfid数据
                        var fhrfidsj = rfiddata.Data;

                        if("-1"==fhrfidsj||""==fhrfidsj){
                            //alert("获取RFID数据失败");
                        }else{
                            //alert(fhrfidsj);
                            getRFID(fhrfidsj);
                        }

                    }
                });
            }else{
                //alert("错误信息："+dkrfiddata.Describe+",状态码为："+fhrfidzt);
            }
        }
    });
}
function closeRFID(){// 刷卡器_RFID -关闭刷卡器
    device.RFID.close({
        position:"301",
        callback:function(data,ws){ //回调函数
            //打开ic
            var closeicdata = eval("("+data+")");
            //返回ic状态
            var fhcloseic = closeicdata.Data;

            if("2"==fhcloseic||"1"==fhcloseic){
                alert("RFID已关闭");
            }
            if("-1"==fhcloseic){
                alert("RFID读卡器退出失败");
            }
        }
    });
}
//RTPS 抓拍
function RTPSzhuapai(str){
    var value = 0;
    device.Video.sendmsg({
        position:str,
        data:"{sendmsg:'TakePic,000001,0|1#1|1'}",
        callback:function(data,ws){
            var data = eval("("+data+")");
            value = data.Data;
        }
    });
    return value;
}

// 开始读取身份证
function sfzgetinfo(callback) {
    device.Sfz.getinfo({
        position:"sfz",
        count:0,
        interval:500,
        isclose:false,
        callback:function(data,ws) {
            var data_ = eval("("+data+")");
            var value = data_.Data;
            if (value =="") {
                return;
            }
            var describe = data_.Describe;
            //console.log(value);
            var value_ = eval("("+value+")");

            callback(value_)
        }
    });
}
//
function dukaqi(callback){
    device.MWRF35.getid({
        position:"201",
        isclose:false,	//执行完毕后不关闭连接
        count:0,		//获取数据次数，0 为一直获取
        interval:50,	//获取数据时间间隔(50毫秒)
        data:"{docardmodel:1,convert:1}", //convert:1全部是正的卡号;docardmodel:0只获取一次卡号，需要拿卡才可获取卡号
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            if (value!="" && value!="-1") {
                callback(value)
            }
        }
    });
}

function ryled(msg){
    device.LED.sendmsg({
        position:"401",
        isclose:true,
        data:"{'sendmsg':'"+msg+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            console.log("led发送成功")
        }
    });
}

//----------------------------------------------------------------



//阻塞方法
function sleep(numberMillis) {
    var now = new Date();
    var exitTime = now.getTime() + numberMillis;
    while (true) {
        now = new Date();
        if (now.getTime() > exitTime)
            return;
    }
}


function fsryyy(msg){

console.log("语音")
    device.TTS.sendmsg({
        position:"601",
        isclose:true,
        data:"{'sendmsg':'"+msg+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            console.log("语音成功")
        }
    });
}

function kaizha(pos){
    var msg = "FE1000030002040004000A416B"
    device.SendRecive.send({
        position:pos,
        isclose:true,
        data:"{'cmd':'"+msg+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
        }
    });
}

function kaizha2(pos){
    var msg = "FE1000080002040004000A00D8"
    device.SendRecive.send({
        position:pos,
        isclose:true,
        data:"{'cmd':'"+msg+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
        }
    });
}

function dakaidukaqi(pos,callback){

    device.MWRF35.open({
        position:pos,
        isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            console.log("打开读卡器："+value)
            console.log("状态："+describe )
            duka(pos,callback);
        }
    });
}



function duka(pos,callback){
    device.MWRF35.getid({
        position:pos,
        isclose:false,	//执行完毕后不关闭连接
        count:0,		//获取数据次数，0 为一直获取
        interval:50,	//获取数据时间间隔(50毫秒)
        data:"{docardmodel:1,convert:1}", //convert:1全部是正的卡号;docardmodel:0只获取一次卡号，需要拿卡才可获取卡号
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            var describe = data_.Describe;
            if (value=="-1") {
                return;
            }
            callback(value);
        }
    });
}


//发送二维码命令，读取一次后关闭
function dkewm(str) {
        var msg = "37540D";
        device.SendRecive.send({
            position:str,
            isclose:true,
            data:"{'cmd':'"+msg+"'}", //sendmsg 要发送的数据
            callback:function(data,ws){
                console.log(data);
            }
        });
    }
    //关闭二维码扫描
function gbewm(str) {
    var msg = "37550D";
    device.SendRecive.send({
        position:str,
        isclose:true,
        data:"{'cmd':'"+msg+"'}", //sendmsg 要发送的数据
        callback:function(data,ws){

        }
    });
}
//读取二维码
function dqewm(str) {
    device.SendRecive.recive({
        position:str,
        isclose:false,
        count:0,
        callback:function(data,ws){
            var data_ = eval("("+data+")");
            var value = data_.Data;
            if(value.length>1){
                reback(value);
            }

        }
    });
}
