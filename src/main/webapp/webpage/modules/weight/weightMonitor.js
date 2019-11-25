<%@ page contentType="text/html;charset=UTF-8" %>

<script>
$(document).ready(function() {

	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


		  $('#weightTable').bootstrapTable("toggleView");
		}
	  // $("#search").click("click", function() {// 绑定查询按扭
      //     $("#searchFlag").val("1");
      //     var d=$("#searchForm").serializeJSON();
      //     if(d.workStation==null || d.workStation==""){
      //         jp.error("请选择地磅！");
      //     }
      //     console.log(d.workStation);
		//   jp.post("${ctx}/weightmonitor/weightmonitor/queryWeightData",d,function(data){
		//       if(data.success){
		//           var list=data.data;
		//           for(var i=0;i<list.length;i++){
		//               console.log(list[i].workStation);
      //             }
      //
      //         }else{
		//           jp.error(data.msg);
      //         }
      //     });
		// });




	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#weightTable').bootstrapTable('refresh');
		});

		$('#taretime').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#grosstime').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
        $('#begingrosstime').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
        $('#endgrosstime').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
        $('#begintaretime').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
        $('#endtaretime').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
	});







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
</script>