<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#checkesTable').bootstrapTable({
		 
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
               url: "${ctx}/checkall/checkes/data",
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
                        jp.confirm('确认要删除该点检表记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/checkall/checkes/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#checkesTable').bootstrapTable('refresh');
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
		        field: 'checkNumber',
		        title: '点检标准号',
		        sortable: true,
		        sortName: 'checkNumber'
		        ,formatter:function(value, row , index){
		        	value = jp.unescapeHTML(value);
				   <c:choose>
					   <c:when test="${fns:hasPermission('checkall:checkes:edit')}">
					      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:when test="${fns:hasPermission('checkall:checkes:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		         }
		       
		    }
			,{
		        field: 'equipment.equipment',
		        title: '设备名称',
		        sortable: true,
		        sortName: 'equipment.equipment'
		       
		    }
			,{
		        field: 'checkMethod',
		        title: '设备检查方法',
		        sortable: true,
		        sortName: 'checkMethod'
		       
		    }
			,{
		        field: 'maintenExplanation',
		        title: '点检设备描述',
		        sortable: true,
		        sortName: 'maintenExplanation'
		       
		    }
			,{
		        field: 'checkInspector.name',
		        title: '点检人',
		        sortable: true,
		        sortName: 'checkInspector.name'
		       
		    }
			,{
		        field: 'checkDate',
		        title: '点检日期',
		        sortable: true,
		        sortName: 'checkDate'
		       
		    }
			,{
		        field: 'checkContect',
		        title: '点检内容',
		        sortable: true,
		        sortName: 'checkContect'
		       
		    }
			,{
		        field: 'checkResult',
		        title: '点检结果',
		        sortable: true,
		        sortName: 'checkResult',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('dic_check_result'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'checkState',
		        title: '点检状态',
		        sortable: true,
		        sortName: 'checkState'
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注',
		        sortable: true,
		        sortName: 'remarks'
		       
		    }
           ,{
               field: 'checkUsage.name',
               title: '审核人',
               sortable: true,
               sortName: 'checkUsage.name'

           }
			,{
		        field: 'checkSuggestion',
		        title: '审核意见',
		        sortable: true,
		        sortName: 'checkSuggestion'
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#checkesTable').bootstrapTable("toggleView");
		}
	  
	  $('#checkesTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#checkesTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#checkesTable').bootstrapTable('getSelections').length!=1);
            $('#inspect').prop('disabled', $('#checkesTable').bootstrapTable('getSelections').length!=1);
            $('#checkes').prop('disabled', $('#checkesTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/checkall/checkes/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/checkall/checkes/import', function (data) {
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
			jp.downloadFile('${ctx}/checkall/checkes/export');
	  });

		    
	  $("#search").click("click", function() {// 绑定查询按扭
          $("#searchFlag").val("1");
		  $('#checkesTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#checkesTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#checkesTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

    function getcheckState() {
    return $.map($("#checkesTable").bootstrapTable('getSelections'), function (row) {
    return row.checkState;
});
}
  function deleteAll(){

		jp.confirm('确认要删除该点检表记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/checkall/checkes/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#checkesTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#checkesTable').bootstrapTable('refresh')
  }

   function add(){

	  jp.openSaveDialog('新增点检表计划', "${ctx}/checkall/checkes/form",'1000px', '700px')
  }


  
   function edit(id){
       if(id == undefined){
	      id = getIdSelections();
   }
   var checkState = getcheckState();
        if( "已审核"==checkState || "已检查"==checkState){
        jp.openViewDialog('查看点检表', "${ctx}/checkall/checkes/auditing?id=" + id, '1000px', '700px')
    }else{
	   jp.openSaveDialog('修改点检表', "${ctx}/checkall/checkes/form?id=" + id, '1000px', '700px')
   }
   }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看点检表', "${ctx}/checkall/checkes/auditing?id=" + id, '1000px', '700px')
 }

    function checkss(id){//没有权限时，不显示确定按钮
    if(id == undefined){
    id = getIdSelections();
 }
    var checkState = getcheckState();

    jp.openSaveDialog('审核点检表', "${ctx}/checkall/checkes/auditing?id=" + id, '1000px', '700px')

 }
    function inspect(id){
    if(id == undefined){
    id = getIdSelections();
 }
    var checkState = getcheckState();
    if( "已审核"==checkState){
    jp.openViewDialog('查看点检表', "${ctx}/checkall/checkes/auditing?id=" + id, '1000px', '700px')
}else{
    jp.openSaveDialog('编辑点检表', "${ctx}/checkall/checkes/checck?id=" + id, '1000px', '700px')
 }
 }


</script>