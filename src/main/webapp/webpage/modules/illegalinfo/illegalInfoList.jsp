<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>监控点闯入列表</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="illegalInfoList.js" %>

</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">监控点闯入列表</h3>
	</div>
	<div class="panel-body">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="illegalInfo" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="监控点位置：">监控点位置：</label>
				 <form:select path="monitorPosition"  class="form-control m-b">
					 <form:option value="" label=""/>
					 <form:options items="${fns:getDictList('illegal')}" itemLabel="label" itemValue="label" htmlEscape="false"/>
				 </form:select>
			 </div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="车牌号：">车牌号：</label>
				<form:input path="vehicleNo" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>

				<div class="col-xs-12 col-sm-6 col-md-4">
					<div class="form-group">
						<label class="label-item single-overflow pull-left" title="通行时间：">&nbsp;通行时间：</label>
						<div class="col-xs-12">
							<div class="col-xs-12 col-sm-5">
								<div class='input-group date' id='begiPassTime' style="left: -10px;" >
									<input type='text'  name="begiPassTime" class="form-control"  />
									<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
								</div>
							</div>
							<div class="col-xs-12 col-sm-1">
								~
							</div>
							<div class="col-xs-12 col-sm-5">
								<div class='input-group date' id='endPassTime' style="left: -10px;" >
									<input type='text'  name="endPassTime" class="form-control" />
									<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
								</div>
							</div>
						</div>
					</div>
				</div>

			<form:hidden path="searchFlag" htmlEscape="false" class=" form-control" id="searchFlag" />
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>	
	</form:form>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="illegalinfo:illegalInfo:add">
				<button id="add" class="btn btn-primary" onclick="add()">
					<i class="glyphicon glyphicon-plus"></i> 新建
				</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="illegalinfo:illegalInfo:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="illegalinfo:illegalInfo:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="illegalinfo:illegalInfo:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="illegalinfo:illegalInfo:export">
	        		<button id="export" class="btn btn-warning">
					<i class="fa fa-file-excel-o"></i> 导出
				</button>
			 </shiro:hasPermission>
	                 <shiro:hasPermission name="illegalinfo:illegalInfo:view">
				<button id="view" class="btn btn-default" disabled onclick="view()">
					<i class="fa fa-search-plus"></i> 查看
				</button>
			</shiro:hasPermission>
		    <%--<input id = "post1" type="text" >
			<button id="post" class="btn btn-success" onclick="post()">
				<i class="glyphicon glyphicon-edit"></i> 发送
			</button>--%>
		    </div>
		
	<!-- 表格 -->
	<table id="illegalInfoTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="illegalinfo:illegalInfo:view">
        <li data-item="view"><a>查看</a></li>
        </shiro:hasPermission>
    	<shiro:hasPermission name="illegalinfo:illegalInfo:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="illegalinfo:illegalInfo:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
	<script >
        //滚动条
        $(function(){
            var divel =  $("<div style='display:inline-block;position:fixed;bottom:0;left:0;width:100%;height:20px;background:#fff;overflow:auto;' ><div style='display:inline-block;height:30px;'></div></div>").appendTo("body");

            $(".fixed-table-body").css("overflow-x","hidden");
            $(divel).find(">div").width( $(".fixed-table-body>table" ).width());
            $(divel).scroll(function(){
                $(".fixed-table-body").scrollLeft($(this).scrollLeft());
            });
            $(".fixed-table-body").resize(function(){
                $(divel).find(">div").width( $(".fixed-table-body>table" ).width());
            });
        })
	</script>
</body>
</html>