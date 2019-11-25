<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var huoquip;
$(document).ready(function() {
	$('#swipeCardTable').bootstrapTable({
		 
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
               url: "${ctx}/swipecard/swipeCard/data",
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
                        jp.confirm('确认要删除该刷卡人员权限记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/swipecard/swipeCard/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#swipeCardTable').bootstrapTable('refresh');
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
		       
		    },{
               field: 'workName',
               title: '姓名',
               sortable: true,
               sortName: 'workName'
               }
               ,{
               field: 'jobNumber',
               title: '工号',
               sortable: true,
               sortName: 'jobNumber'

               }
			,{
               field: 'officeId',
               title: '所属班组',
               sortable: true,
               sortName: 'officeId',
                       formatter: function (value, row, index) {
                           return jp.getDictLabel(${fns:toJson(fns:getDictList('swipe_class'))}, value, "-");
                       }
           }
			,{
		        field: 'telephone',
		        title: '联系方式',
		        sortable: true,
		        sortName: 'telephone'
		       
		    }
		   ,{
			   field: 'createDate',
			   title: '创建时间',
			   sortable: true,
			   sortName: 'createDate'

		   },{
               field: 'icnumber',
               title: '卡号',
               sortable: true,
               sortName: 'icnumber'
           }
		     ]
		
		});

    if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#swipeCardTable').bootstrapTable("toggleView");
		}
	  
	  $('#swipeCardTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#swipeCardTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#swipeCardTable').bootstrapTable('getSelections').length!=1);
            $('#icnumber').prop('disabled', $('#swipeCardTable').bootstrapTable('getSelections').length!=1);
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
					 jp.downloadFile('${ctx}/swipecard/swipeCard/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/swipecard/swipeCard/import', function (data) {
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
			jp.downloadFile('${ctx}/swipecard/swipeCard/export');
	  });

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#swipeCardTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#swipeCardTable').bootstrapTable('refresh');
		});

    $('#startTime').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });
    $('#endTime').datetimepicker({
        format: "YYYY-MM-DD HH:mm:ss"
    });

});

		
  function getIdSelections() {
        return $.map($("#swipeCardTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

function deleteAll(){

    jp.confirm('确认要删除该刷卡人员权限记录吗？', function(){
        jp.loading();
        jp.get("${ctx}/swipecard/swipeCard/deleteAll?ids=" + getIdSelections(), function(data){
            if(data.success){
                $('#swipeCardTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })
}
  function refresh(){
  	$('#swipeCardTable').bootstrapTable('refresh');
  }
  function add(){
      jp.openSaveDialog('安保人员管理信息',"${ctx}/swipecard/swipeCard/form/add", '800px', '500px');
	}


function edit(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
    jp.openSaveDialog('编辑刷卡人员权限', "${ctx}/swipecard/swipeCard/form/edit?id=" + id, '800px', '500px');
}

function view(id){//没有权限时，不显示确定按钮
    if(id == undefined){
        id = getIdSelections();
    }
    jp.openViewDialog('查看刷卡人员权限', "${ctx}/swipecard/swipeCard/form?id=" + id, '800px', '500px');
}
  var icStr = "221"

//打开读卡器
function openCardbind(){

    jp.get("${ctx}/swipecard/swipeCard/queryWorkStation",function (data) {
        if (data.success) {
            jp.alert("请放置卡片")
            // 开启刷卡器
            device.MWRF35.open({
                position:icStr,
                //isclose:false,	//执行完毕后不关闭连接
                callback:function(data,ws){ //回调函数
                    //打开ic
                    var icdata = eval("("+data+")");
                    //返回ic状态
                    var fhiczt = icdata.Data;
                    console.log(fhiczt);
                    if(fhiczt<0){
                        alert("发生错误，状态码为："+fhiczt);
                    }else{
                        readCard(icStr);
                    }
                }
            });
        } else{
                jp.alert("请在1号岗进门处绑定IC卡")

        }
    });
}
//读取卡号
function readCard() {
    //获取卡号
    device.MWRF35.getid({
        position:icStr,
        isclose:false,	//执行完毕后不关闭连接
        count:0,		//获取数据次数，0 为一直获取
        data:'{docardmodel:0,convert:1}', //convert:1全部是正的卡号;docardmodel:0只获取一次卡号，需要拿卡才可获取卡号
        interval:1000,	//获取数据时间间隔(50毫秒)8秒
        callback:function(data,ws){
            //获取ic
            var data=eval("("+data+")");
            ickh = 	data.Data;
            console.log(ickh);
            if("-2"==ickh||"-3"==ickh){
                alert("错误,状态码为："+ickh)
            }else if("-1"==ickh){

            }else{
                var id = getIdSelections();

                if(typeof ickh != "undefined" && ickh != "" && ickh != -1 && ickh != null){
                	var icnumber = ickh;
                    jp.get("${ctx}/swipecard/swipeCard/repeat?id="+ id+"&icnumber="+ickh,function (data) {
                        if (data.success) {
                            console.log(ickh)
                            updateIC(id,icnumber);
                        } else{
                            if(data.object == "1"){
                                console.log(ickh)
                                jp.error(data.msg);
                            }else{
                                jp.confirm('确定要覆盖之前的记录吗？', function(){
                                    updateIC(id,icnumber);
                                });
                            }

                        }
                    });
                }
            }
        }
    });
}
function updateIC(id,ickh){
    jp.get("${ctx}/swipecard/swipeCard/updateIC?id="+ id+"&icnumber="+ickh,function (data) {
        if (data.success) {
            jp.success(data.msg);
            device.MWRF35.close({position:"201",callback:function(data,ws){
            	console.log("读卡已关闭")
				}});
            refresh();
        }else{
            //jp.error(data.msg);
		}
    });
}
</script>