<%@ page contentType="text/html;charset=UTF-8" %>
<script>

$(document).ready(function() {

	$('#controlQueueTable').bootstrapTable({
		  //请求方法
               method: 'post',
               //类型json
               dataType: "json",
               contentType: "application/x-www-form-urlencoded",
              /* //显示检索按钮
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
    	       showPaginationSwitch: true,*/
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
               url: "${ctx}/controlqueue/controlQueue/data",
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
                        jp.confirm('确认要删除该排队信息记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/controlqueue/controlQueue/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#controlQueueTable').bootstrapTable('refresh');
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
		        field: 'stationName',
		        title: '工作站',
		        sortable: true,
		        sortName: 'stationName'

		    },{
    	        field: 'vehicleNo',
                title: '车牌号',
                sortable: true,
                sortName: 'vehicleNo'
            },{
               field: 'weightId',
               title: '磅单号',
               sortable: true,
               sortName: 'weightId'
           }, {
               field: 'billPic',
               title: '票据截图',
               formatter:function(value, row , index){

                   if(value ==''){
                       return '<img height="40px" src="${ctxStatic}/common/images/flat-avatar.png">';
                   }else{
                       return '<img onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                   }
               }
                   },{
               	field: 'content',
				title: '内容',
				sortable: true,
				sortName: 'content'
			},{
		        field: 'stateName',
		        title: '排队状态',
		        sortable: true,
		        sortName: 'stateName'

		    },
           //         {
           //     field: 'seatNum',
           //     title: '坐席编号',
           //     sortable: true,
           //     sortName: 'seatNum'
           //
           // }  ,
                   {
               field: 'operate',
               title: '操作',
               align: 'center',
               events: {
                   'click .handle': function (e, value, row, index) {
                       handle(row.id);
                   },
                   'click .delete': function (e, value, row, index) {
                       deleteAll(row.id);
                   },
                   'click .remove': function (e, value, row, index) {
                       remove(row.id);
                   },
               },
               formatter: function operateFormatter(value, row, index) {
                   return [

                       <shiro:hasPermission name="controlqueue:controlQueue:handle">
                       '<a href="#" class="handle" title="处理"><b style="font-size:18px;">[处理]</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>',
                       '<a href="#" class="delete" title="回退">[回退] </a>',
                       '<a href="#" class="remove" title="删除">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[删除] </a>'
                        </shiro:hasPermission>

               ].join('');
               }
           }

		     ]

		});


	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


		  $('#controlQueueTable').bootstrapTable("toggleView");
		}

	});

  function getIdSelections() {
        return $.map($("#controlQueueTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
function deleteAll(id){

    jp.confirm('确认要回退该条记录吗？', function(){
        jp.get("${ctx}/controlqueue/controlQueue/deleteAll?ids=" +id, function(data){

            if(data.success){

                $('#controlQueueTable').bootstrapTable('refresh');
                jp.success(data.msg);
                device.ip = data.object;
                device.Route.set({
                    position:"Z01",
                    isclose:true,
                    data:"{key:'xiugaichehao',value:'005'}",
                    callback:function(data){
                    }
                });
                device.Route.set({
                    position:"Z01",
                    isclose:true,
                    data:"{key:'xiugaichehao',value:''}",
                    callback:function(data){
                    }
                });
              }else {
                jp.error(data.msg);
            }
        });

    })
}
  //修改排队状态，
  function handle(id) {

      if(id == undefined){
          id = getIdSelections();
      }
      jp.get("${ctx}/controlqueue/controlQueue/handle?id=" +id, function(data){

          if(data.success){

          	  var w= data.data;
              var a = top.getActiveTab()[0].contentWindow;
              var d = new Date();
              localStorage.setItem("weight",JSON.stringify(w));
               a.window.location.reload();
              jp.close();
              a.$("#video").css('display','block');
          }else{
              jp.success(data.msg)

              $('#controlQueueTable').bootstrapTable('refresh');
          }
      })
  }

  //删除阻塞的已处理排队信息
    function remove(id) {
        if(id == undefined){
            id = getIdSelections();
        }
        jp.confirm('确认要删除该排队信息吗？', function(){
            jp.loading();
            jp.get("${ctx}/controlqueue/controlQueue/remove?id=" + id, function(data){
                if(data.success){
                    $('#controlQueueTable').bootstrapTable('refresh');
                    jp.success(data.msg);
                }else{
                    jp.error(data.msg);
                }
            })

        })

    }

</script>