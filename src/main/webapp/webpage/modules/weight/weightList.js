<%@ page contentType="text/html;charset=UTF-8" %>
<script>
    var dyj = "E01";
    var printIp = "";
$(document).ready(function() {

	$('#weightTable').bootstrapTable({

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
    	       //showColumns: true,
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
               url: "${ctx}/weight/weight/data",
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

               onClickRow: function(row, $el){
               },
               	onShowSearch: function () {
			$("#search-collapse").slideToggle();
		},
               columns: [{
		        checkbox: true

		    },{
                   title: '序号',
                   field: '',
                   formatter: function (value, row, index) {
                       return index+1;
                   }
               }
                   ,{
                       field: 'weighNo',
                       title: '磅单号',
                       sortable: true,
                       sortName: 'weighNo'
                       ,formatter:function(value, row , index){
                           value = jp.unescapeHTML(value);
                       <c:choose>
                           <c:when test="${fns:hasPermission('weight:weight:view')}">
                           return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
                       </c:when>
                           </c:choose>
                       }

                   },{
                       field: 'consignId',
                       title: '委托单号',
                       sortable: true,
                       sortName: 'consignId'

                   },{
                       field: 'vehicleNo',
                       title: '车牌号',
                       sortable: true,
                       sortName: 'vehicleNo'

                   }

                   ,{
                       field: 'grosstime',
                       title: '一次过磅时间',
                       sortable: true,
                       sortName: 'grosstime'

                   } ,{
                       field: 'taretime',
                       title: '二次过磅时间',
                       sortable: true,
                       sortName: 'taretime'

                   },{
                       field: 'prodCname',
                       title: '物品名称',
                       sortable: true,
                       sortName: 'prodCname'

                   }
                   ,{
                       field: 'consigneUser',
                       title: '收货单位',
                       sortable: true,
                       sortName: 'consigneUser'

                   }
                   ,{
                       field: 'supplierName',
                       title: '发货单位',
                       sortable: true,
                       sortName: 'supplierName'

                   }
                   ,{
                       field: 'matWt',
                       title: '材料重量（净重）',
                       sortable: true,
                       sortName: 'matWt'

                   }
                   ,{
                       field: 'matGrossWt',
                       title: '材料毛重',
                       sortable: true,
                       sortName: 'matGrossWt'

                   }
                   ,{
                       field: 'impWt',
                       title: '杂重（皮重）',
                       sortable: true,
                       sortName: 'impWt'

                   },{
                       field: 'printNum',
                       title: '磅单打印次数',
                       sortable: true,
                       sortName: 'printNum'

                   },{
                       field: 'status',
                       title: '状态',
                       sortable: true,
                       sortName: 'status',
                       formatter:function(value, row , index){
                           value = jp.getDictLabel(${fns:toJson(fns:getDictList('weigh_status'))}, value, "-");
                           return value;

                       }

                   },{
                       field: 'fistStation',
                       title: '一次过磅工作站',
                       sortable: true,
                       sortName: 'fistStation',
                       formatter:function(value, row , index){
                           value = jp.getDictLabel(${fns:toJson(fns:getDictList('workststion_ip'))}, value, "-");
                           return value;

                       }
                   },{
                       field: 'secondStation',
                       title: '二次过磅工作站',
                       sortable: true,
                       sortName: 'secondStation',
                       formatter:function(value, row , index){
                           value = jp.getDictLabel(${fns:toJson(fns:getDictList('workststion_ip'))}, value, "-");
                           return value;

                       }
                   },{
                       field: 'billPic',
                       title: '票据截图',
                       formatter:function(value, row , index){

                           if(value ==''){
                               return '<img height="40px" src="">';
                           }else{
                               return '<img onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                           }
                       }
                   },
                   {
                       field: 'tareHeadPic',
                       title: '一次过磅抓拍',
                       formatter:function(value, row , index){

                           if(value ==''){
                               return '<img height="40px" src="">';
                           }else{
                               return '<img onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                           }
                       }
                   },
                   {
                       field: 'tareTailPic',
                       title: '一次过磅抓拍',
                       formatter:function(value, row , index){

                           if(value ==''){
                               return '<img height="40px" src="">';
                           }else{
                               return '<img onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                           }
                       }
                   },
                   {
                       field: 'tareTopPic',
                       title: '一次过磅抓拍',
                       formatter:function(value, row , index){

                           if(value ==''){
                               return '<img height="40px" src="">';
                           }else{
                               return '<img onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                           }
                       }
                   }, {
                       field: 'grossHeadPic',
                       title: '二次过磅抓拍',
                       formatter:function(value, row , index){

                           if(value ==''){
                               return '<img height="40px" src="">';
                           }else{
                               return '<img onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                           }
                       }
                   },
                   {
                       field: 'grossTailPic',
                       title: '二次过磅抓拍',
                       formatter:function(value, row , index){

                           if(value ==''){
                               return '<img height="40px" src="">';
                           }else{
                               return '<img onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                           }
                       }
                   },
                   {
                       field: 'grossTopPic',
                       title: '二次过磅抓拍',
                       formatter:function(value, row , index){

                           if(value ==''){
                               return '<img height="40px" src="">';
                           }else{
                               return '<img onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                           }
                       }
                   },
                   {
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
                               <shiro:hasPermission name="weight:weight:view">
                               '<a href="#" class="view" title="详情" >[详情] </a>'
                               </shiro:hasPermission>
                       ].join('');
                       }
                   }
		     ]

		});
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


		  $('#weightTable').bootstrapTable("toggleView");
		}

	  $('#weightTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
          $('#change').prop('disabled', ! $('#weightTable').bootstrapTable('getSelections').length);
          $('#cancel').prop('disabled', ! $('#weightTable').bootstrapTable('getSelections').length);
          $('#view,#edit').prop('disabled', $('#weightTable').bootstrapTable('getSelections').length!=1);
          $('#weightRecord').prop('disabled', $('#weightTable').bootstrapTable('getSelections').length!=1);
          $('#bdbill,#bdweight').prop('disabled', $('#weightTable').bootstrapTable('getSelections').length!=1);
          $('#back').prop('disabled', $('#weightTable').bootstrapTable('getSelections').length!=1);
        });


	 $("#export").click(function(){//导出Excel文件
         jp.download('${ctx}/weight/weight/export',JSON.stringify($("#searchForm").serializeJSON()));

	  });


	  $("#search").click("click", function() {// 绑定查询按扭
          $("#searchFlag").val("1");
		  $('#weightTable').bootstrapTable('refresh');
		});

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

  function getIdSelections() {
        return $.map($("#weightTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

  function deleteAll(){

      jp.confirm('确认要删除该磅单管理记录吗？确认要删除该磅单管理记录吗？确认要删除该磅单管理记录吗？', function(){
			jp.loading();
			jp.get("${ctx}/weight/weight/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#weightTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})

		})

  }


function cancel(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
    jp.openSaveDialog('作废磅单', "${ctx}/weight/weight/weightCancel?id=" + id, '800px', '500px')

}
    //刷新列表
  function refresh(){
  	$('#weightTable').bootstrapTable('refresh');
  }


   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑磅单管理', "${ctx}/weight/weight/form?id=" + id, '800px', '500px');
  }

 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看磅单管理', "${ctx}/weight/weight/checkform?id=" + id, '1000px', '500px');
 }
function getweighNo() {
    return $.map($("#weightTable").bootstrapTable('getSelections'), function (row) {
        return row.weighNo
    });
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


function changePrint(weighNo,ip,flag){
    weighNo = getWeighNoSelections();
    console.log("开始保存日志----------------"+Date.now())
    jp.get("${ctx}/weight/weight/changePrint?weighNo="+weighNo+"&ip="+ip+"&flag="+flag, function (data) {
        if (data.success) {
            console.log("打印磅单完成----------------"+Date.now())
        }
    });
}
	//跳转到进出厂车辆过磅页面
	function weightConsign() {

		window.open("${ctx}/weight/weight/weightConsign");
	}
    //跳转到内转车辆、轨道衡过磅页面
    function gdbweightConsign() {

        window.open("${ctx}/weight/weight/weightgdh");
    }

    //毛重皮重互换
    function change() {
        var id = getIdSelections();
        jp.get("${ctx}/weight/weight/change?id="+id, function(data){
            if(data.success){
                $('#weightTable').bootstrapTable('refresh');
                jp.success("互换成功！");
            }else{
                jp.error(data.msg);
            }
        })
    }

function getWeighNoSelections() {
    return $.map($("#weightTable").bootstrapTable('getSelections'), function (row) {
        return row.weighNo;
    });
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
    function bdbill(){

        var weighNo = getWeighNoSelections();
        if(weighNo == null || weighNo == "" ||typeof(weighNo)=="undefined"){
            jp.alert("打印失败！")
            return;
        }
        if(printIp == ""){
            jp.alert("请选择打印机");
            return;
        }
        jp.get("${ctx}/weight/weight/dyPrint?weighNo="+weighNo, function (data) {

                if (data.success) {

                var print = data.data;

                var matGrossWt ="";
                var impWt ="";
                if(print.weightType == "01"||print.weightType == "05"||print.weightType == "08"||print.weightType == "09"){
                    matGrossWt ="";
                    impWt =parseInt(print.impWt);
                }else{
                    matGrossWt =parseInt(print.matGrossWt);
                    impWt ="";
                }

                var pria = print.supplierName;
                var pric = print.consigneUser;
                var prib = "";
                var prid = "";
                var prie = "";
                var prif = "";

                if(pria.length>12){
                    //a=a.substring(0,12)+"\n"+a.substring(12,a.length)
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
                    var qrcode =weighNo.toString()

                    var printdata = "{'header':'宝钢德盛不锈钢"
                        + "','header2':'有限公司"
                        + "','header3':'物料签收单"
                        + "','grossweighttime':'日期:" + (new Date(print.grosstime)).Format("yyyy/MM/dd/ HH:mm")
                        + "','vehicleno':'车号:"+print.vehicleNo
                        + "','prodcname':' 货物名称  "+print.prodCname
                        + "','grossname':' 毛重(Kg) ','grossweight':'  "+ matGrossWt
                        + "','tarename':' 皮重(Kg) ','tareweight':' "+ impWt
                        + "','standardname':' 净重(Kg) ','standardweight':'   "
                        + "','custname':' 发货单位  "+prib
                        + "','custname2':'           "+prid
                        + "','recustname':' 收货单位  "+prie
                        + "','recustname2':'           "+prif
                        + "','unloadingaddress':'装卸货地址 "
                        + "','receiver':' 签 收 人 "
                        + "','weighNo':'"+weighNo.toString()
                        + "','qrcode':'"+qrcode
                        + "','def':'"+def
                        + "','workStation':' "+print.workStation
                        + "',_file_:'billweight.xml'}";
                    if(typeof printIp !="undefined" &&  printIp!=null){
                        device.ip = printIp;
                    }
                    var ip = device.ip;
                printaa(printdata,weighNo,ip,"true");
            } else {
                jp.error(data.msg);
            }
        });
    }
    function bdweight(){
        var weighNo = getWeighNoSelections();
        if(weighNo == null || weighNo == "" ||typeof(weighNo)=="undefined"){
            jp.alert("打印失败！");
            return;
        }
        if(printIp == ""){
            jp.alert("请选择打印机");
            return;
        }

            var position ="E01"
            jp.get("${ctx}/weight/weight/dyWeight?weighNo="+weighNo,function (data) {

                if (data.success) {

                    var print = data.data;
                    var pria = print.supplierName;
                    var pric = print.consigneUser;
                    var prib = "";
                    var prid = "";
                    var prie = "";
                    var prif = "";

                    if(pria.length>12){
                        //a=a.substring(0,12)+"\n"+a.substring(12,a.length)
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
                    console.log("11111"+print.workStation);
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
                        + "','weighNo':'磅单号:"+weighNo.toString()
                        + "','workStation':' "+print.workStation
                        + "',_file_:'weight.xml'}";

                    if(typeof printIp !="undefined" &&  printIp!=null){
                        device.ip = printIp;
                    }
                    var ip= device.ip
                    printaa(printdata,weighNo,ip,"false");
                } else {
                    jp.error(data.msg);
                }
            });
    }
    function synData(){
        var weighNo = getweighNo();
        var msg="确认要手动同步磅单数据吗？";
      jp.confirm(msg, function(){
            jp.loading();
            jp.get("${ctx}/weight/weight/cheshijk?weighNos="+weighNo, function(data){
                if(data.success){
                    jp.success("同步成功")
                    var sj = data.data;
                }else{
                    jp.error("同步失败")
                }
            });

        })

    }
function mergeWeight(){
    var weighNo = getweighNo();
    var msg="确认要手动合并磅单数据吗？";
    jp.confirm(msg, function(){
        jp.loading();
        jp.get("${ctx}/weight/weight/mergeWeight?weighNos="+weighNo, function(data){
            if(data.success){
                jp.success(data.msg)
                refresh();
            }else{
                jp.error(data.msg);
            }
        });

    })

}
    function selectprint(){
        var obj = document.getElementById('selectprint'); //定位id
        var index = obj.selectedIndex; // 选中索引
        var value = obj.options[index].value; // 选中值
        //alert(value)
        printIp = value;
}
function weightRecord(){
    var weighNo = getWeighNoSelections();
    localStorage.setItem("weighNo",weighNo);
    if(weighNo == null || weighNo == "" ||typeof(weighNo)=="undefined"){
        jp.alert("请选择要查看的磅单！")
        return;
    }
    jp.openViewDialog('查看打印记录', "${ctx}/weight/weight/printRecord",'1000px', '700px');
}
function weightUpdateRecord(){
    var weighNo = getWeighNoSelections();
    localStorage.setItem("weighNo",weighNo);
    if(weighNo == null || weighNo == "" ||typeof(weighNo)=="undefined"){
        jp.alert("请选择要查看的磅单！")
        return;
    }
    jp.openViewDialog('查看打印记录', "${ctx}/weight/weight/weightUpdateRecord",'1000px', '700px');
}

function backQuit(){
    var weighNo = getWeighNoSelections();
    if(weighNo == null || weighNo == "" ||typeof(weighNo)=="undefined"){
        jp.alert("请选择需要回退的数据！")
        return;
    }
    jp.get("${ctx}/weight/weight/back?weighNo="+weighNo, function(data){
        if(data.success){
            jp.success(data.msg)
            refresh();
        }else{
            jp.error(data.msg)
        }
    });
}
</script>