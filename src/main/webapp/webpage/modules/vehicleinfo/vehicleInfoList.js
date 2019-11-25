<%@ page contentType="text/html;charset=UTF-8" %>
<script>

$(document).ready(function() {
	$('#vehicleInfoTable').bootstrapTable({
		 
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
               url: "${ctx}/vehicleinfo/vehicleInfo/data",
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
                        jp.confirm('确认要删除该车辆信息记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/vehicleinfo/vehicleInfo/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#vehicleInfoTable').bootstrapTable('refresh');
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
		        field: 'typeCode',
		        title: '类别代码',
		        sortable: true,
		        sortName: 'typeCode',
                       formatter:function(value, row , index){
                           value = jp.getDictLabel(${fns:toJson(fns:getDictList('vehicle_type'))}, value, "-");
                           return value;

                       }
		       
		    }
			,{
		        field: 'vehicleNo',
		        title: '车牌号',
		        sortable: true,
		        sortName: 'vehicleNo'
		       
		    }
			,{
		        field: 'passCode',
		        title: '放行码',
		        sortable: true,
		        sortName: 'passCode'
		       
		    }
			,{
		        field: 'rfidNo',
		        title: 'RFID卡号1',
		        sortable: true,
		        sortName: 'rfidNo'
		       
		    }
		   ,{
			   field: 'srfidNo',
			   title: 'RFID卡号2',
			   sortable: true,
			   sortName: 'srfidNo'

		   }
		   ,{
			   field: 'groupCode',
			   title: '组批代码',
			   sortable: true,
			   sortName: 'groupCode'

		   }
			,{
		        field: 'groupCompanyName',
		        title: '集团公司名称',
		        sortable: true,
		        sortName: 'groupCompanyName'
		       
		    }
			,{
		        field: 'transContactPerson',
		        title: '运输联系人',
		        sortable: true,
		        sortName: 'transContactPerson'
		       
		    }
			,{
		        field: 'transContactPersonTel',
		        title: '运输联系人电话',
		        sortable: true,
		        sortName: 'transContactPersonTel'
		       
		    }
			,{
		        field: 'wagonType',
		        title: '局车类型',
		        sortable: true,
		        sortName: 'wagonType'
		    }
			,{
		        field: 'qty',
		        title: '荷载数量',
		        sortable: true,
		        sortName: 'qty'
		       
		    }
			,{
		        field: 'approvePersonNo',
		        title: '审批人工号',
		        sortable: true,
		        sortName: 'approvePersonNo'
		       
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
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#vehicleInfoTable').bootstrapTable("toggleView");
		}
	  
	  $('#vehicleInfoTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#vehicleInfoTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#vehicleInfoTable').bootstrapTable('getSelections').length!=1);
            $('#binding').prop('disabled', $('#vehicleInfoTable').bootstrapTable('getSelections').length!=1);
            $('#bindingRail').prop('disabled', $('#vehicleInfoTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/vehicleinfo/vehicleInfo/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/vehicleinfo/vehicleInfo/import', function (data) {
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
         jp.download('${ctx}/vehicleinfo/vehicleInfo/export',JSON.stringify($("#searchForm").serializeJSON()));

	  });

		    
	  $("#search").click("click", function() {// 绑定查询按扭
          $("#searchFlag").val("1");
		  $('#vehicleInfoTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#vehicleInfoTable').bootstrapTable('refresh');
		});
		
		$('#dealTime').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#approveTime').datetimepicker({
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
        return $.map($("#vehicleInfoTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该车辆信息记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/vehicleinfo/vehicleInfo/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#vehicleInfoTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#vehicleInfoTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增车辆信息', "${ctx}/vehicleinfo/vehicleInfo/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑车辆信息', "${ctx}/vehicleinfo/vehicleInfo/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看车辆信息', "${ctx}/vehicleinfo/vehicleInfo/form?id=" + id, '800px', '500px');
 }

function updateRFID(id,rfid) {
    jp.get("${ctx}/vehicleinfo/vehicleInfo/updateRFID?id="+ id+"&rfidNo="+rfid,function (data) {
        if (data.success) {
            refresh();
            jp.success(data.msg);
        }else{
            jp.error(data.msg);
        }
    });
}

function updateRailRFID(id,rfid2){
    jp.get("${ctx}/vehicleinfo/vehicleInfo/updateRailRFID?id="+ id+"&srfidNo="+rfid2,function (data) {
            if (data.success) {
                refresh();
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
});
}
function bindinga(){
    jp.alert("请将RFID卡放置在读卡设备上")
    id = getIdSelections();
    //打开RFID读卡器301
    device.RFID.open({
        position:"301",
        //isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            var dkrfiddata = eval("("+data+")");
            //返回rfid状态
            var dkrfid = dkrfiddata.Data;
            if("2"==dkrfid|"1"==dkrfid){
                //获取数据
                device.RFID.getdata({
                    position:"301",
                    isclose:false,	//执行完毕后不关闭连接
                    count:2,	//获取数据次数，0 为一直获取
                    interval:1000,	//获取数据时间间隔(1000毫秒)
                    data:"{istid:true}",
                    callback:function(sjdata,ws){
                        //获取rfid
                        var data = eval("("+sjdata+")");
                        //返回rfid数据
                        var rfid = data.Data;

                        if(typeof rfid != "undefined" && rfid != "" && rfid != "-1" && rfid != "1" && rfid != "2"){
                            console.log("绑定的rfid卡1为："+rfid);
                            jp.get("${ctx}/vehicleinfo/vehicleInfo/repeat?id="+id+"&flag=1",function (data) {
                                if (data.success) {
                                    updateRFID(id,rfid);
                                } else{
                                    jp.confirm('确定要覆盖之前的记录吗？', function(){
                                        updateRFID(id,rfid);
                                    });
                                }
                            });
                        }
                    }
                });
            }else{
                console.log("RFID:"+dkrfiddata.Describe+"异常RFID301:"+fhrfidzt);
            }
        }
    });
}
function bindingRail(){
    jp.alert("请将第二张RFID卡放置在读卡设备上")
    id = getIdSelections();
    //打开RFID读卡器301
    device.RFID.open({
        position:"301",
        //isclose:false,	//执行完毕后不关闭连接
        callback:function(data,ws){ //回调函数
            var dkrfiddata = eval("("+data+")");
            var dkrfid = dkrfiddata.Data;
            if("2"==dkrfid|"1"==dkrfid){
                //获取数据
                device.RFID.getdata({
                    position:"301",
                    isclose:false,	//执行完毕后不关闭连接
                    count:2,	//获取数据次数，0 为一直获取
                    interval:1000,	//获取数据时间间隔(1000毫秒)
                    data:"{istid:true}",
                    callback:function(sjdata,ws){
                        //获取rfid
                        var data = eval("("+sjdata+")");
                        //返回rfid数据
                        var rfid2 = data.Data;

                        if(typeof rfid2 != "undefined" && rfid2 != "" && rfid2 != "-1" && rfid2 != "1" && rfid2 != "2"){
                            console.log("绑定的rfid卡2为："+rfid2)

                            jp.get("${ctx}/vehicleinfo/vehicleInfo/repeat?id="+id+"&flag=2",function(data){
                                if (data.success) {
                                    updateRailRFID(id,rfid2);
                                }else{
                                    jp.confirm('确定要覆盖之前的记录吗？', function(){
                                        updateRailRFID(id,rfid2);
                                    });
                                }
                            });
                        }
                    }
                });
            }else{
                console.log("RFID:"+dkrfiddata.Describe+"异常RFID301:"+fhrfidzt);
            }
        }
    });
}
</script>