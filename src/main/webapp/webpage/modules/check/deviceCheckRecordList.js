<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
    var obj={};

	$('#deviceCheckRecordTable').bootstrapTable({
		 
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
               url: "${ctx}/check/deviceCheckRecord/data",
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
                       	jp.get("${ctx}/userpasscord/userPassRecord/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#deviceCheckRecordTable').bootstrapTable('refresh');
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
		        field: 'id',
		        title: '记录ID',
		        sortable: true,
		        sortName: 'id'
		       
		    }
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
			//    sortable: false
		   //
		   // }
		   // ,{
			//    field: 'workingWorker',
			//    title: '点检人员'
           //
		   // }
		   ,{
			   field: 'checkCycle',
			   title: '点检周期',
			   sortable: true,
			   sortName: 'checkCycle',
			   formatter:function (value,row,index) {
				   if(value=="day"){
				   		return "每日"
				   }else if(value=="week"){
				   	return "每周"
				   }
				   return "每月"
			   }

		   }
		   ,{
			   field: 'checkDate',
			   title: '周期时间',
			   sortable: false,
				sortName:'checkDate'

		   },{


               		field:'createDate',
				   title:'记录生成时间',
				   sortable:true,
				   sortName:'createDate',
					   formatter:function (value,row,index) {
						var today = new Date().getDate();
						var recordDate = new Date(value).getDate();
						if(today!=recordDate){
							return "<del>记录过期</del>";
						}
						return value
					   }
				   }
				   ,{
					   field: 'operate',
					   title: '操作',
					   align: 'center',
					   events: {
						   'click .addItem': function (e, value, row, index) {
							   addItem(row.id);
						   },
						   'click .cancelRecord': function (e, value, row, index) {
							   // alert(row.id);
							   cancelRecord(row.id);
						   }
					   },
					   formatter:  function operateFormatter(value, row, index) {
						   return [
							   <shiro:hasPermission name="check:deviceCheckItem:list">
							   '<a href="#" class="addItem" title="查看子项" >【查看子项】</a>',
					   </shiro:hasPermission>
						   <shiro:hasPermission name="check:deviceCheckItem:list">
							   '<a href="#" class="cancelRecord" title="取消">【取消】 </a>'
							   </shiro:hasPermission>
					   ].join('');
					   }
				   }

			   ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#deviceCheckRecordTable').bootstrapTable("toggleView");
		}
	  
	  $('#deviceCheckRecordTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#deviceCheckRecordTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#deviceCheckRecordTable').bootstrapTable('getSelections').length!=1);
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
		  $('#deviceCheckRecordTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#deviceCheckRecordTable').bootstrapTable('refresh');
		});
    $('#createDate').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
    $('#beginCheckDate').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
    $('#endCheckDate').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
	});
		
  function getIdSelections() {
        return $.map($("#deviceCheckRecordTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该员工通行记录记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/check/deviceCheckRecord/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#deviceCheckRecordTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#deviceCheckRecordTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增设备点检记录', "${ctx}/check/deviceCheckRecord/form",'800px', '500px');
  }

  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑设备点检记录', "${ctx}/check/deviceCheckRecord/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看设备点检记录', "${ctx}/check/deviceCheckRecord/form?id=" + id, '800px', '500px');
 }

function addItem(id){
	if(id == undefined){
		id = getIdSelections();
	}
	// var url = "${ctx}/check/deviceCheckRitem/list?recordId=" + id;
	// window.open(url);
	jp.openSaveDialog('编辑设备点检配置项目明细', "${ctx}/check/deviceCheckRitem/list?recordId=" + id, '1200px', '800px');
}
function cancelRecord(id){
  	if(id==undefined){
  		id=getIdSelections();
	}
  	jp.confirm('确定要取消这条点检记录？',function(){
  		jp.openSaveDialog("请填写取消原因","${ctx}/check/deviceCheckRecord/cancel?Id=" + id,'500px','200px')
	})
}

</script>