<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
    var obj={};

	$('#deviceCheckConfigTable').bootstrapTable({
		 
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
               url: "${ctx}/check/deviceCheckConfig/data",
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
                        jp.confirm('确认要删除该员工通行记录记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/check/deviceCheckConfig/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#deviceCheckConfigTable').bootstrapTable('refresh');
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
			// ,{
		    //     field: 'id',
		    //     title: '记录ID',
		    //     sortable: true,
		    //     sortName: 'id'
		    //
		    // }
			// ,{
		    //     field: 'workingGroup',
		    //     title: '班组',
		    //     sortable: true,
		    //     sortName: 'workingGroup'
		    //
		    // }
			,{
		        field: 'workingArea',
		        title: '作业区域',
		        sortable: true,
		        sortName: 'workingArea'

		    }

		   // ,{
			//    field: 'groupLeader',
			//    title: '班组长',
			//    sortable: false,
			//    // sortName: 'checkContent'
		   //
		   // }
		   ,{
					   field: 'checkCycle',
					   title: '点检周期',
					   sortable: false,
					   formatter:function (value,row,index) {
						   if(value=="day"){
							   return "每日"
						   }else if(value=="week"){
							   return "每周"
						   }
						   return "每月"
					   }
					   // sortName: 'deviceName'

				   },
		   {
			   field: 'checkDate',
			   title: '点检频率',
			   sortable: true,
			   sortName: 'checkContent'

		   },
				   {
				   	field:'isEnable',
				   title:'是否启用',
					   formatter:function(value,row,index){
				   			if(value=="1"){
				   				return "是";
							}
				   			return "否"
					   }
				   },
				   {
					   field: 'operate',
					   title: '操作',
					   align: 'center',
					   events: {
						   'click .addItem': function (e, value, row, index) {
							   addItem(row.id);
						   },
						   'click .viewDetail': function (e, value, row, index) {
						   	alert(row.id);
							   veiwDetail(row.id);
						   }
					   },
					   formatter:  function operateFormatter(value, row, index) {
						   return [
							   <shiro:hasPermission name="check:deviceCheckItem:list">
							   '<a href="#" class="addItem" title="查看子项" >【查看子项】</a>',
					   </shiro:hasPermission>
						   <shiro:hasPermission name="check:deviceCheckItem:null">
							   '<a href="#" class="veiwDetail" title="查看详情">【查看详情】 </a>'
							   </shiro:hasPermission>
					   ].join('');
					   }
				   }


			// ,

		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


		  $('#deviceCheckConfigTable').bootstrapTable("toggleView");
		}
	  
	  $('#deviceCheckConfigTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#deviceCheckConfigTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#deviceCheckConfigTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/userpasscord/userPassRecord/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/userpasscord/userPassRecord/import', function (data) {
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
		
		
	 // $("#export").click(function(){//导出Excel文件
		// 	 jp.download('${ctx}/userpasscord/userPassRecord/export',JSON.stringify($("#searchForm").serializeJSON()));
	 //
     // });

		    
	  $("#search").click("click", function() {// 绑定查询按扭
          $("#searchFlag").val("1");
		  $('#deviceCheckConfigTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#deviceCheckConfigTable').bootstrapTable('refresh');
		});
    // $('#checkDate').datetimepicker({
    //     format: "YYYY-MM-DD HH:mm:ss"
    // });
    // $('#beginCheckDate').datetimepicker({
    //     format: "YYYY-MM-DD HH:mm:ss"
    // });
    // $('#endCheckDate').datetimepicker({
    //     format: "YYYY-MM-DD HH:mm:ss"
    // });
	});
		
  function getIdSelections() {
        return $.map($("#deviceCheckConfigTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该条配置信息吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/check/deviceCheckConfig/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#deviceCheckConfigTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#deviceCheckConfigTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('设备点检配置', "${ctx}/check/deviceCheckConfig/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑设备点检配置信息', "${ctx}/check/deviceCheckConfig/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看员工通行记录', "${ctx}/userpasscord/userPassRecord/form?id=" + id, '800px', '500px');
 }

//添加配置项目
function addItem(id){
	if(id == undefined){
		id = getIdSelections();
	}
	jp.openSaveDialog('编辑设备点检配置项目明细', "${ctx}/check/deviceCheckItem/list?configId=" + id, '800px', '500px');
}
//查看配置详情
function veiwDetail(id){
	if(id == undefined){
		id = getIdSelections();
	}
	jp.openSaveDialog('编辑设备点检配置项目明细', "${ctx}/check/deviceCheckConfig/viewDetail?id=" + id, '800px', '500px');
}
</script>