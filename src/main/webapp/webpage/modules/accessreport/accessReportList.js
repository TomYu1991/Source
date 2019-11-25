<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	// $('#reportTable').bootstrapTable({
	// 	method: 'post',
	// 	//类型json
	// 	dataType: "json",
	// 	contentType: "application/x-www-form-urlencoded",
	// 	//显示刷新按钮
	// 	showRefresh: true,
	// 	//显示切换手机试图按钮
	// 	showToggle: true,
	// 	//显示 内容列下拉框
	// 	showColumns: true,
	// 	//显示导出按钮
	// 	showExport: true,
	// 	//是否缓存
	// 	cache: false,
	// 	//是否显示分页（*）
	// 	pagination: true,
	// 	//排序方式
	// 	sortOrder: "asc",
	// 	//初始化加载第一页，默认第一页
	// 	pageNumber:1,
	// 	//每页的记录行数（*）
	// 	pageSize: 10,
	// 	//可供选择的每页的行数（*）
	// 	pageList: [10, 25, 50, 100],
	// 	sidePagination: "server",
	// 	contextMenuTrigger:"right",//pc端 按右键弹出菜单
	// 	contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
	// 	contextMenu: '#context-menu',
	// 	//请求地址
	// 	// url: "${ctx}/accessreport/accessreport/data",
	// 	//请求参数
	// 	queryParams : function(params) {
	// 		var searchParam = $("#searchForm").serializeJSON();
	// 		console.log(searchParam);
	// 		return searchParam;
	// 	},
	// 	onShowSearch: function () {
	// 		$("#search-collapse").slideToggle();
	// 	},
	// 	columns:
	// 		[
	// 			// [
	// 			// 	{
	// 			// 		title:"安环日报表",
	// 			// 		halign:"center",
	// 			// 		align:"center",
	// 			// 		colspan: 15,
	// 			// 		fontsize:'20px'
	// 			// 	}
	// 			// ],
	// 		[
	// 			{
	// 			title: '车辆/车次',
	// 			colspan: 8,
	// 			align: 'center'
	// 		},
	// 			{
	// 			title:'人员/人次',
	// 			colspan: 4,
	// 			align:'center'
	// 		},
	// 			{
	// 				title:'物料/张',
	// 				colspan:3,
	// 				align:'center'
	// 			}
	// 		],
	// 		[
	// 			{
	// 			field: 'vehicleIn',
	// 			title: '进门车辆',
	// 			align: 'center'
	// 		},
	// 			{
	// 			field: 'vehicleOut',
	// 			title: '出门车辆',
	// 			align: 'center',
	// 		},
	// 			{
	// 			field: 'vehicleConsign',
	// 			title: '预约车辆',
	// 			align: 'center'
	// 		},
	// 			{
	// 				field:'vehicleToFactory',
	// 				title:'预约入厂车辆',
	// 				align:'center',
	// 				formatter:function(value, row , index){
	// 					var url="${ctx}/vehicleaccessrecord/vehicleAccessRecord/list";
	// 					var title = '查看预约入厂车辆';
	// 					return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
	// 				}
	// 			},
	// 			{
	// 				field:'vehicleManual',
	// 				title:'进门手动放行',
	// 				align:'center',
	// 				formatter:function(value, row , index){
	// 					var url="${ctx}/vehicleaccessrecord/vehicleAccessRecord/list";
	// 					var title='查看手动放行记录'
	// 					return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
	// 				}
	// 			},
	// 			{
	// 				field:'approveVehicle',
	// 				title:'已批过夜车辆',
	// 				align:'center',
	// 				formatter:function(value, row , index){
	// 					var url="${ctx}/warninginfo/warningInfo/list";
	// 					var title='查看手动放行记录'
	// 					return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
	// 				}
	// 			},
	// 			{
	// 				field:'unapproveVehicle',
	// 				title:'未批过夜车辆',
	// 				align:'center',
	// 				formatter:function(value, row , index){
	// 					var url="${ctx}/warninginfo/warningInfo/list";
	// 					var title='查看手动放行记录';
	// 					return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
	// 				}
	// 			},
	// 			{
	// 				field:'illegalVehicle',
	// 				title:'违章车辆',
	// 				align:'center',
	// 				formatter:function(value, row , index){
	// 					var url="${ctx}/warninginfo/warningInfo/list";
	// 					var title='查看手动放行记录';
	// 					return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
	// 				}
	// 			},
	// 			{
	// 				field:'userIn',
	// 				title:'进门人员',
	// 				align:'center'
	// 			},
	// 			{
	// 				field:'userOut',
	// 				title:'出门人员',
	// 				align:'center'
	// 			},
	// 			{
	// 				field:'userConsign',
	// 				title:'预约人员',
	// 				align:'center'
	// 			},
	// 			{
	// 				field:'userToFactory',
	// 				title:'预约入厂人员',
	// 				align:'center',
	// 				formatter:function(value, row , index){
	// 					var url="${ctx}/userpasscord/userPassRecord/list";
	// 					var title='查看手动放行记录';
	// 					return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
	// 				}
	// 			},
	// 			{
	// 				field:'gatePass',
	// 				title:'出门条',
	// 				align:'center',
	// 				formatter:function(value, row , index){
	// 					var url="${ctx}/passcheck/passCheck/list";
	// 					var title='查看手动放行记录';
	// 					return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
	// 				}
	// 			},
	// 			{
	// 				field:'gatePassu',
	// 				title:'出门条未出厂',
	// 				align:'center',
	// 				formatter:function(value, row , index){
	// 					var url="${ctx}/passcheck/passCheck/list";
	// 					var title='查看手动放行记录';
	// 					return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
	// 				}
	// 			},
	// 			{
	// 				field:'breakIn',
	// 				title:'重要部位闯入',
	// 				align:'center',
	// 				formatter:function(value, row , index){
	// 					var url="${ctx}/illegalinfo/illegalInfo/list";
	// 					var title='查看手动放行记录';
	// 					return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
	// 				}
	// 			}
	// 		]
	// 	]
	// });
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#reportTable').bootstrapTable("toggleView");
		}
	  
	  $('#reportTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
          $('#remove').prop('disabled', ! $('#reportTable').bootstrapTable('getSelections').length);
		  $('#view,#edit').prop('disabled', $('#reportTable').bootstrapTable('getSelections').length!=1);
          $('#deblocking').prop('disabled', $('#reportTable').bootstrapTable('getSelections').length!=1);
          $('#deleteImpWtH').prop('disabled', $('#reportTable').bootstrapTable('getSelections').length!=1);

      });
		  

		

		    
	  $("#search").click("click", function() {// 绑定查询按扭
          $("#searchFlag").val("1");
		  $('#reportTable').bootstrapTable('refresh');
		  $('#reportTable').bootstrapTable({
			  method: 'post',
			  //类型json
			  dataType: "json",
			  contentType: "application/x-www-form-urlencoded",
			  //显示刷新按钮
			  showRefresh: true,
			  //显示切换手机试图按钮
			  showToggle: true,
			  //显示 内容列下拉框
			  showColumns: true,
			  //显示导出按钮
			  showExport: true,
			  //是否缓存
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
			  sidePagination: "server",
			  contextMenuTrigger:"right",//pc端 按右键弹出菜单
			  contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
			  contextMenu: '#context-menu',
			  //请求地址
			  url: "${ctx}/accessreport/accessreport/data",
			  //请求参数
			  queryParams : function(params) {
				  var searchParam = $("#searchForm").serializeJSON();
				  console.log(searchParam);
				  return searchParam;
			  },
			  onShowSearch: function () {
				  $("#search-collapse").slideToggle();
			  },
			  columns:
				  [
					  // [
					  // 	{
					  // 		title:"安环日报表",
					  // 		halign:"center",
					  // 		align:"center",
					  // 		colspan: 15,
					  // 		fontsize:'20px'
					  // 	}
					  // ],
					  [
						  {
							  title: '车辆/车次',
							  colspan: 8,
							  align: 'center'
						  },
						  {
							  title:'人员/人次',
							  colspan: 4,
							  align:'center'
						  },
						  {
							  title:'物料/张',
							  colspan:3,
							  align:'center'
						  }
					  ],
					  [
						  {
							  field: 'vehicleIn',
							  title: '进门车辆',
							  align: 'center'
						  },
						  {
							  field: 'vehicleOut',
							  title: '出门车辆',
							  align: 'center',
						  },
						  {
							  field: 'vehicleConsign',
							  title: '预约车辆',
							  align: 'center'
						  },
						  {
							  field:'vehicleToFactory',
							  title:'预约入厂车辆',
							  align:'center',
							  formatter:function(value, row , index){
								  var url="${ctx}/vehicleaccessrecord/vehicleAccessRecord/list";
								  var title = '查看预约入厂车辆';
								  return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
							  }
						  },
						  {
							  field:'vehicleManual',
							  title:'进门手动放行',
							  align:'center',
							  formatter:function(value, row , index){
								  var url="${ctx}/vehicleaccessrecord/vehicleAccessRecord/list";
								  var title='查看手动放行记录'
								  return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
							  }
						  },
						  {
							  field:'approveVehicle',
							  title:'已批过夜车辆',
							  align:'center',
							  formatter:function(value, row , index){
								  var url="${ctx}/warninginfo/warningInfo/list";
								  var title='查看手动放行记录'
								  return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
							  }
						  },
						  {
							  field:'unapproveVehicle',
							  title:'未批过夜车辆',
							  align:'center',
							  formatter:function(value, row , index){
								  var url="${ctx}/warninginfo/warningInfo/list";
								  var title='查看手动放行记录';
								  return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
							  }
						  },
						  {
							  field:'illegalVehicle',
							  title:'违章车辆',
							  align:'center',
							  formatter:function(value, row , index){
								  var url="${ctx}/warninginfo/warningInfo/list";
								  var title='查看手动放行记录';
								  return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
							  }
						  },
						  {
							  field:'userIn',
							  title:'进门人员',
							  align:'center'
						  },
						  {
							  field:'userOut',
							  title:'出门人员',
							  align:'center'
						  },
						  {
							  field:'userConsign',
							  title:'预约人员',
							  align:'center'
						  },
						  {
							  field:'userToFactory',
							  title:'预约入厂人员',
							  align:'center',
							  formatter:function(value, row , index){
								  var url="${ctx}/userpasscord/userPassRecord/list";
								  var title='查看手动放行记录';
								  return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
							  }
						  },
						  {
							  field:'gatePass',
							  title:'出门条',
							  align:'center',
							  formatter:function(value, row , index){
								  var url="${ctx}/passcheck/passCheck/list";
								  var title='查看手动放行记录';
								  return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
							  }
						  },
						  {
							  field:'gatePassu',
							  title:'出门条未出厂',
							  align:'center',
							  formatter:function(value, row , index){
								  var url="${ctx}/passcheck/passCheck/list";
								  var title='查看手动放行记录';
								  return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
							  }
						  },
						  {
							  field:'breakIn',
							  title:'重要部位闯入',
							  align:'center',
							  formatter:function(value, row , index){
								  var url="${ctx}/illegalinfo/illegalInfo/list";
								  var title='查看手动放行记录';
								  return "<a href='javascript:view(\""+url+"\")'>"+value+"</a>";
							  }
						  }
					  ]
				  ]
		  });
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#reportTable').bootstrapTable('refresh');
		});
		
		$('#date').datetimepicker({
			 format: "YYYY-MM-DD"
		});

	});
		
  function getIdSelections() {
        return $.map($("#reportTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
function getVeSelections() {
    return $.map($("#reportTable").bootstrapTable('getSelections'), function (row) {
        return row.vehicleNo
    });
}
  function deleteAll(){

		// jp.confirm('确认要删除该委托单/预约单记录吗？', function(){
		// 	jp.loading();
		// 	jp.get("${ctx}/consign/consign/cancel?ids=" + getIdSelections(), function(data){
        //  	  		if(data.success){
        //  	  			$('#reportTable').bootstrapTable('refresh');
        //  	  			jp.success(data.msg);
        //  	  		}else{
        //  	  			jp.error(data.msg);
        //  	  		}
        //  	  	})
        //
		// })
  }

    //刷新列表
  function refresh(){
  	$('#reportTable').bootstrapTable('refresh');
  }
  
  //  function add(){
	//   jp.openSaveDialog('新增委托单/预约单', "${ctx}/consign/consign/form",'800px', '500px');
  // }


  
  //  function edit(id){//没有权限时，不显示确定按钮
  //      if(id == undefined){
	//       id = getIdSelections();
	// }
	// jp.openSaveDialog('编辑委托单/预约单', "${ctx}/consign/consign/consignUpdateForm?id=" + id, '800px', '500px');
  // }

//查看预约入厂车辆
 function view(url){//没有权限时，不显示确定按钮
        jp.openTab(url,'查看记录',false);
        // var data={
		// 	// type:
		// 	// vehicleNo:
	 	// 	// consignId:
		//  	// prodCname:
		//  	// status:
		//  	// field2:
		//  	// ponderFlag:
		//  	// field1:
		//  	startTime: '2019-05-09 07:52:59',
	 	// 	endTime: '2019-05-22 07:53:05',
	 	// 	searchFlag: 1,
	 	// 	pageNo: 1,
	 	// 	pageSize: 10
	 	// 	// orderBy:
		//  }
        // jp.post(url=url,data=data,function(data){
		// 	jp.success('<table border="1px">' +
		// 		'<tr>' +
		// 		'<td>列1</td>' +
		// 		'<td>列2</td>' +
		// 		'<td>列3</td>' +
		// 		'</tr>' +
		// 		'<tr>' +
		// 		'<td>a</td>' +
		// 		'<td>b</td>' +
		// 		'<td>c</td>' +
		// 		'</tr>' +
		// 		'</table>')
		// })
 }


</script>