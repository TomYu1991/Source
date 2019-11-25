<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#weightRecordTable').bootstrapTable({
		 
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
    	       //显示详情按钮
    	       detailView: true,
    	       	//显示详细内容函数
	           detailFormatter: "detailFormatter",
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
               url: "${ctx}/weightrecord/weightRecord/data",
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
                        jp.confirm('确认要删除该磅单信息记录记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/weightrecord/weightRecord/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#weightRecordTable').bootstrapTable('refresh');
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
		        field: 'weighNo',
		        title: '磅单号',
		        sortable: true,
		        sortName: 'weighNo'
		       
		    }
			,{
		        field: 'consignId',
		        title: '委托单号',
		        sortable: true,
		        sortName: 'consignId'
		       
		    }
			,{
		        field: 'vehicleNo',
		        title: '车牌号/铁水罐号',
		        sortable: true,
		        sortName: 'vehicleNo'
		       
		    }
			,{
		        field: 'prodCname',
		        title: '品名',
		        sortable: true,
		        sortName: 'prodCname'
		       
		    }
			,{
		        field: 'sgSign',
		        title: '钢级',
		        sortable: true,
		        sortName: 'sgSign'
		       
		    }
			,{
		        field: 'matWt',
		        title: '净重',
		        sortable: true,
		        sortName: 'matWt'
		       
		    }
			,{
		        field: 'matGrossWt',
		        title: '毛重',
		        sortable: true,
		        sortName: 'matGrossWt'
		       
		    }
			,{
		        field: 'impWt',
		        title: '皮重',
		        sortable: true,
		        sortName: 'impWt'
		       
		    }
			,{
		        field: 'status',
		        title: '状态',
		        sortable: true,
		        sortName: 'status',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('weigh_status'))}, value, "-");
		        }
		       
		    },{
                       field: 'defaultFlag',
                       title: '锁皮标记',
                       sortable: true,
                       sortName: 'defaultFlag',
                       formatter:function(value, row , index){
                           return jp.getDictLabel(${fns:toJson(fns:getDictList('default_flag'))}, value, "-");
                       }

                   }
			,{
		        field: 'consigneUser',
		        title: '收货方',
		        sortable: true,
		        sortName: 'consigneUser'
		       
		    }
			,{
		        field: 'supplierName',
		        title: '发货方',
		        sortable: true,
		        sortName: 'supplierName'
		       
		    }
			,{
		        field: 'fistStation',
		        title: '一次过磅工作站',
		        sortable: true,
		        sortName: 'fistStation',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('workststion_ip'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'secondStation',
		        title: '二次过磅工作站',
		        sortable: true,
		        sortName: 'secondStation',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('workststion_ip'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'grosstime',
		        title: '一次过磅时间',
		        sortable: true,
		        sortName: 'grosstime'
		       
		    }
			,{
		        field: 'taretime',
		        title: '二次过磅时间',
		        sortable: true,
		        sortName: 'taretime'
		       
		    }

		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#weightRecordTable').bootstrapTable("toggleView");
		}
	  
	  $('#weightRecordTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#weightRecordTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#weightRecordTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/weightrecord/weightRecord/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/weightrecord/weightRecord/import', function (data) {
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
			jp.downloadFile('${ctx}/weightrecord/weightRecord/export');
	  });
		    
	  $("#search").click("click", function() {// 绑定查询按扭
          $("#searchFlag").val("1");
		  $('#weightRecordTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#weightRecordTable').bootstrapTable('refresh');
		});

    $('#begintaretime').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
    $('#endtaretime').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
	});
		
  function getIdSelections() {
        return $.map($("#weightRecordTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该磅单信息记录记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/weightrecord/weightRecord/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#weightRecordTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

     //刷新列表
  function refresh(){
  	$('#weightRecordTable').bootstrapTable('refresh');
  }
  function add(){
		jp.go("${ctx}/weightrecord/weightRecord/form/add");
	}

  function edit(id){
	  if(id == undefined){
		  id = getIdSelections();
	  }
	  jp.go("${ctx}/weightrecord/weightRecord/form/edit?id=" + id);
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
         jp.go("${ctx}/weightrecord/weightRecord/form/view?id=" + id);
 }

  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#weightRecordChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/weightrecord/weightRecord/detail?id="+row.id, function(weightRecord){
    	var weightRecordChild1RowIdx = 0, weightRecordChild1Tpl = $("#weightRecordChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  weightRecord.initWeightList;

		  if(data1!=null){
              for (var i=0; i<data1.length; i++){
                  data1[i].dict = {};
                  data1[i].dict.defaultFlag = jp.getDictLabel(${fns:toJson(fns:getDictList('default_flag'))}, data1[i].defaultFlag, "-");
                  data1[i].dict.stationIp = jp.getDictLabel(${fns:toJson(fns:getDictList('workststion_ip'))}, data1[i].stationIp, "-");
                  addRow('#weightRecordChild-'+row.id+'-1-List', weightRecordChild1RowIdx, weightRecordChild1Tpl, data1[i]);
                  weightRecordChild1RowIdx = weightRecordChild1RowIdx + 1;
              }
		  }

				
    	var weightRecordChild2RowIdx = 0, weightRecordChild2Tpl = $("#weightRecordChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data2 =  weightRecord.printRecordList;
		if(data2!=null){
            for (var i=0; i<data2.length; i++){
                data2[i].dict = {};
                addRow('#weightRecordChild-'+row.id+'-2-List', weightRecordChild2RowIdx, weightRecordChild2Tpl, data2[i]);
                weightRecordChild2RowIdx = weightRecordChild2RowIdx + 1;
            }
		}

				
    	var weightRecordChild3RowIdx = 0, weightRecordChild3Tpl = $("#weightRecordChild3Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data3 =  weightRecord.updateWeightRecordList;
		  if(data3!=null){
              for (var i=0; i<data3.length; i++){
                  data3[i].dict = {};
                  data3[i].dict.operation = jp.getDictLabel(${fns:toJson(fns:getDictList('weight_operation'))}, data3[i].operation, "-");
                  addRow('#weightRecordChild-'+row.id+'-3-List', weightRecordChild3RowIdx, weightRecordChild3Tpl, data3[i]);
                  weightRecordChild3RowIdx = weightRecordChild3RowIdx + 1;
              }
		  }

				
      	  			
      })
     
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
			
</script>
<script type="text/template" id="weightRecordChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">磅单初始数据</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">打印记录</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-3" aria-expanded="true">磅单修改记录</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>过磅工作站</th>
   						 		<th>过磅状态</th>
								<th>车牌号/铁水罐号</th>
    							<th>重量(kg)</th>
								<th>品名</th>
								<th>称重时间</th>


							</tr>
						</thead>
						<tbody id="weightRecordChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-2" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>操作</th>
								<th>打印位置</th>
    							<th>打印人</th>
    							<th>打印时间</th>
								<th>车牌号/铁水罐号</th>
							</tr>
						</thead>
						<tbody id="weightRecordChild-{{idx}}-2-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-3" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>操作人</th>
								<th>操作时间</th>
								<th>操作</th>
								<th>操作内容</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="weightRecordChild-{{idx}}-3-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="weightRecordChild1Tpl">//<!--
				<tr>
    				<td>
    					{{row.dict.stationIp}}
					</td>
					<td>
						{{row.remarks}}
					</td>
					<td>
						{{row.vehicleNo}}
					</td>
					<td>
					{{row.weightWt}}
					</td>
					<td>
						{{row.prodCname}}
					</td>
					<td>
						{{row.weightTime}}
					</td>

				</tr>//-->
	</script>
	<script type="text/template" id="weightRecordChild2Tpl">//<!--
				<tr>
					<td>
						{{row.operation}}
					</td>
					<td>
						{{row.stationIp}}
					</td>
					<td>
					{{row.createBy.loginName}}
					</td>
					<td>
					{{row.createDate}}
					</td>
					<td>
						{{row.vehicleNo}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="weightRecordChild3Tpl">//<!--
				<tr>
					<td>
						{{row.updateBy.loginName}}
					</td>
					<td>
						{{row.updateDate}}
					</td>
					<td>
						{{row.dict.operation}}
					</td>
					<td>
						{{row.content}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>
