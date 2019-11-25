<%@ page contentType="text/html;charset=UTF-8" %>
    <script>
var cp = "A01";//车牌识别
var led = "401";//LED
var hld = "901";//红绿灯
var dz="701";  //道闸
var vehicleNo = "鲁A4562";
var spzp = "C02";//视频抓拍
$(document).ready(function() {

});

/*识别车牌*/
function cmsbcp(){
    vehicleNo = sbch(cp);
    var url = zhuapai(spzp);

    jp.get("${ctx}/gatepass/passcheck/checkAuthority?vehicleNo=" + vehicleNo+"&spzp="+spzp+"&url="+url, function (data) {
        if (data.success) {
            var d =data.data;
            jp.success(data.msg);

            setRG_G(hld,"1#ON");
            var msg = "车牌号:"+d.vehicleNo+",可进入！"
            fsLED1(led,msg);
        } else {
            jp.warning(data.msg);

            setRG_R(hld,"1#OFF");
            var msg = "车牌号:"+d.vehicleNo+",不可进入，请离开！"
            fsLED1(led,msg);
        }
    })
}


/*保存日志*/
function saveLog(op,exp,vehicleNo,pos){

    if(typeof exp == "undefined" || exp == null || exp == ""){
        exp="无"
    }
    jp.get("${ctx}/gatelog/gateLog/savePassLog?vehicleNo=" + vehicleNo+"&pos="+pos+"&op="+op+"&exp="+exp, function(data){
        if(data.success){
            $('#gateLogTable').bootstrapTable('refresh');
        }else{
            jp.error(data.msg);
        }
    })
}


</script>