<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {

	$('#consignTable').bootstrapTable({
		 
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
               url: "${ctx}/consign/consign/data",
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
                        jp.confirm('确认要删除该委托单/预约单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/consign/consign/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#consignTable').bootstrapTable('refresh');
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
		        field: 'type',
		        title: '类别代码',
		        sortable: true,
		        sortName: 'type',
		        formatter:function(value, row , index){
		        	   value = jp.getDictLabel(${fns:toJson(fns:getDictList('consign_type'))}, value, "-");
				   <c:choose>
					   <c:when test="${fns:hasPermission('consign:consign:edit')}">
					      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:when test="${fns:hasPermission('consign:consign:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		        }
		       
		    },{
                       field: 'field2',
                       title: '委托单类型',
                       sortable: true,
                       sortName: 'field2',
                       formatter:function(value, row , index){
                           console.log(row);
                           value = jp.getDictLabel(${fns:toJson(fns:getDictList('consign_sub_type'))}, value, "-");
                           return value;
                       }
                   }
			,{
		        field: 'consignId',
		        title: '委托/预约单号',
		        sortable: true,
		        sortName: 'consignId'
		       
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
		   ,{
			   field: 'prodCname',
			   title: '品名',
			   sortable: true,
			   sortName: 'prodCname'

		   }
			,{
		        field: 'vehicleNo',
		        title: '车牌号',
		        sortable: true,
		        sortName: 'vehicleNo'
		       
		    },{
                       field: 'status',
                       title: '状态',
                       sortable: true,
                       sortName: 'status',
                       formatter:function(value, row , index){
                           console.log(row);
                           value = jp.getDictLabel(${fns:toJson(fns:getDictList('consign_status'))}, value, "-");
                           return value;
                       }
                   },{
			   field: 'defaultFlag',
			   title: '是否锁皮',
			   sortable: true,
			   sortName: 'defaultFlag',
			   formatter:function(value, row , index){
				  console.log(row);
				   value = jp.getDictLabel(${fns:toJson(fns:getDictList('default_flag'))}, value, "-");
				   return value;
			   }
			   },{
                       field: 'ponderFlag',
                       title: '单次生效标记',
                       sortable: true,
                       sortName: 'ponderFlag',
                       formatter:function(value, row , index){
                           console.log(row);
                           value = jp.getDictLabel(${fns:toJson(fns:getDictList('ponder_flag'))}, value, "-");
                           return value;
                       }
                   },{
                       field: 'field1',
                       title: '确认收货标记',
                       sortable: true,
                       sortName: 'field1',
                       formatter:function(value, row , index){
                           console.log(row);
                           value = jp.getDictLabel(${fns:toJson(fns:getDictList('sure_flag'))}, value, "-");
                           return value;
                       }
                   }
			, {
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

					   <shiro:hasPermission name="consign:consign:view">
					   '<a href="#" class="view" title="详情" >[详情] </a>'
					   </shiro:hasPermission>
			   ].join('');
			   }
		   }
		 ]
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#consignTable').bootstrapTable("toggleView");
		}
	  
	  $('#consignTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
          $('#remove').prop('disabled', ! $('#consignTable').bootstrapTable('getSelections').length);
		  $('#view,#edit').prop('disabled', $('#consignTable').bootstrapTable('getSelections').length!=1);
          $('#deblocking').prop('disabled', $('#consignTable').bootstrapTable('getSelections').length!=1);
          $('#deleteImpWtH').prop('disabled', $('#consignTable').bootstrapTable('getSelections').length!=1);

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
					  jp.downloadFile('${ctx}/consign/consign/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/consign/consign/import', function (data) {
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
         jp.download('${ctx}/consign/consign/export',JSON.stringify($("#searchForm").serializeJSON()));

     });

		    
	  $("#search").click("click", function() {// 绑定查询按扭
          $("#searchFlag").val("1");
		  $('#consignTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#consignTable').bootstrapTable('refresh');
		});
		
		$('#startTime').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endTime').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#consignTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
function getVeSelections() {
    return $.map($("#consignTable").bootstrapTable('getSelections'), function (row) {
        return row.vehicleNo
    });
}
  function deleteAll(){

		jp.confirm('确认要删除该委托单/预约单记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/consign/consign/cancel?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#consignTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#consignTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增委托单/预约单', "${ctx}/consign/consign/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑委托单/预约单', "${ctx}/consign/consign/consignUpdateForm?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
     var a =  $('#consignTable').bootstrapTable('getData');
     console.log("页面数据条数"+a.length);
        jp.openViewDialog('查看委托单/预约单', "${ctx}/consign/consign/form?id=" + id, '800px', '500px');
 }

function consignIds() {
    return $.map($("#consignTable").bootstrapTable('getSelections'), function (row) {
        return row.consignId;
    });
}

function deblocking(){
    var consignId = consignIds();
    jp.get("${ctx}/consign/consign/deblocking?consignId="+consignId, function(data){

        if(data.success){
             jp.success(data.msg)
             refresh();
        }else{
            jp.error(data.msg)
        }
    });
}
function deleteImpWtH() {
    jp.confirm('确认要删除该委托单皮重历史吗？', function(){
        jp.loading();
        jp.get("${ctx}/consign/consign/deleteImpWtH?ids=" + getVeSelections(), function(data){
            if(data.success){
                $('#consignTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })
}

</script>