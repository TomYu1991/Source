<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#vehicleAccessRecordTable').bootstrapTable({
		 
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
               url: "${ctx}/vehicleaccessrecord/vehicleAccessRecord/data",
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
                        jp.confirm('确认要删除该车辆进出记录记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/vehicleaccessrecord/vehicleAccessRecord/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#vehicleAccessRecordTable').bootstrapTable('refresh');
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
		        field: 'vehicleNo',
		        title: '车牌号',
		        sortable: true,
		        sortName: 'vehicleNo'
		        ,formatter:function(value, row , index){
		        	value = jp.unescapeHTML(value);
				   <c:choose>
					  <c:when test="${fns:hasPermission('vehicleaccessrecord:vehicleAccessRecord:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		         }
		       
		    }
			,{
		        field: 'transContactPerson',
		        title: '姓名',
		        sortable: true,
		        sortName: 'transContactPerson'
		       
		    }
			,{
		        field: 'transContactPersonTel',
		        title: '联系方式',
		        sortable: true,
		        sortName: 'transContactPersonTel'
		       
		    }
			,{
		        field: 'intoTime',
		        title: '进厂时间',
		        sortable: true,
		        sortName: 'intoTime'
		       
		    }
			,{
		        field: 'outTime',
		        title: '出厂时间',
		        sortable: true,
		        sortName: 'outTime'
		       
		    },{
                       field: 'rfidNo',
                       title: '通行类型',
                       sortable: true,
                       sortName: 'rfidNo',
                       formatter:function(value, row , index){
                           value = jp.getDictLabel(${fns:toJson(fns:getDictList('open_type'))}, value, "-");
                           return value;
                       }
                   },{
                       field: 'idcard',
                       title: '放行人工号',
                       sortable: true,
                       sortName: 'idcard'
                   }
			,{
		        field: 'remarks',
		        title: '通行岗亭',
		        sortable: true,
		        sortName: 'remarks'
		    },
		   {
			   field: 'consignType',
			   title: '委托类型',
			   sortable: true,
			   sortName: 'consignType',
               formatter:function(value, row , index){
                    value = jp.getDictLabel(${fns:toJson(fns:getDictList('consign_type'))}, value, "-");
                   return value;
               }
		   },
		   {
			   field: 'takePhotos',
			   title: '出门抓拍图片',
			   sortable: true,
			   sortName: 'takePhotos',
               formatter:function(value, row , index){

                   if(value ==''){
                       return '<img height="40px" src="${ctxStatic}/common/images/flat-avatar.png">';
                   }else{
                       return '<img onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                   }
               }
		   }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#vehicleAccessRecordTable').bootstrapTable("toggleView");
		}
	  
	  $('#vehicleAccessRecordTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#peccancy').prop('disabled', ! $('#vehicleAccessRecordTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#vehicleAccessRecordTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/vehicleaccessrecord/vehicleAccessRecord/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/vehicleaccessrecord/vehicleAccessRecord/import', function (data) {
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
			jp.download('${ctx}/vehicleaccessrecord/vehicleAccessRecord/export',JSON.stringify($("#searchForm").serializeJSON()));
	  });

		    
	  $("#search").click("click", function() {// 绑定查询按扭
          $("#searchFlag").val("1");
		  $('#vehicleAccessRecordTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#vehicleAccessRecordTable').bootstrapTable('refresh');
		});
		
		$('#intoTime').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#outTime').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginintoTime').datetimepicker({
			format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endintoTime').datetimepicker({
			format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginoutTime').datetimepicker({
			format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endoutTime').datetimepicker({
			format: "YYYY-MM-DD HH:mm:ss"
		});
	});
		
  function getIdSelections() {
        return $.map($("#vehicleAccessRecordTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

function getVehicleSelections() {
    return $.map($("#vehicleAccessRecordTable").bootstrapTable('getSelections'), function (row) {
        return row.vehicleNo
    });
}
  function deleteAll(){

		jp.confirm('确认要删除该车辆进出记录记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/vehicleaccessrecord/vehicleAccessRecord/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#vehicleAccessRecordTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#vehicleAccessRecordTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增车辆进出记录', "${ctx}/vehicleaccessrecord/vehicleAccessRecord/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑车辆进出记录', "${ctx}/vehicleaccessrecord/vehicleAccessRecord/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看车辆进出记录', "${ctx}/vehicleaccessrecord/vehicleAccessRecord/form?id=" + id, '800px', '500px');
 }
//获取车辆状态
function getState() {
    return $.map($("#vehicleAccessRecordTable").bootstrapTable('getSelections'), function (row) {
        return row.state
    });
}

function peccancy(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getVehicleSelections();
    }

    jp.openSaveDialog('填写违章信息', "${ctx}/vehicleaccessrecord/vehicleAccessRecord/peccancyForm?vehicleNo=" + id, '800px', '500px')

}
function record(){//没有权限时，不显示确定按钮
    jp.openSaveDialog('',"${ctx}/warninginfo/warningInfo/list", '1000px', '600px');
}


</script>