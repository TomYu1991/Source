<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车辆进出记录管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="vehicleAccessRecordList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">车辆进出记录列表</h3>
	</div>
	<div class="panel-body">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="vehicleAccessRecord" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="车牌号：">车牌号：</label>
				<form:input path="vehicleNo" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>


			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="委托单类型：">委托单类型：</label>
				 <form:select path="consignType"  class="form-control m-b">
					 <form:option value="" label=""/>
					 <form:options items="${fns:getDictList('consign_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				 </form:select>
			</div>

				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="放行类型：">放行类型：</label>
					<form:select path="rfidNo"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('open_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="通行岗亭：">通行岗亭：</label>
				<form:select path="remarks"  class="form-control m-b">
					<option value="" label="请选择通行门岗"></option>
					<c:forEach items="${list}" var="l">
						<option value="${l.workStation}" label="${l.workStation}"></option>
					</c:forEach>
				</form:select>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<div class="form-group">
						<label class="label-item single-overflow pull-left" title="进厂时间：">&nbsp;进厂时间：</label>
						<div class="col-xs-12">
							<div class="col-xs-12 col-sm-5">
								<div class='input-group date' id='beginintoTime' style="left: -10px;" >
									<input type='text'  name="beginintoTime" class="form-control"  />
									<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
								</div>
							</div>
							<div class="col-xs-12 col-sm-1">
								~
							</div>
							<div class="col-xs-12 col-sm-5">
								<div class='input-group date' id='endintoTime' style="left: -10px;" >
									<input type='text'  name="endintoTime" class="form-control" />
									<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="col-xs-12 col-sm-6 col-md-4">
					<div class="form-group">
						<label class="label-item single-overflow pull-left" title="出厂时间：">&nbsp;出厂时间：</label>
						<div class="col-xs-12">
							<div class="col-xs-12 col-sm-5">
								<div class='input-group date' id='beginoutTime' style="left: -10px;" >
									<input type='text'  name="beginoutTime" class="form-control"  />
									<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
								</div>
							</div>
							<div class="col-xs-12 col-sm-1">
								~
							</div>
							<div class="col-xs-12 col-sm-5">
								<div class='input-group date' id='endoutTime' style="left: -10px;" >
									<input type='text'  name="endoutTime" class="form-control" />
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
		   <shiro:hasPermission name="vehicleaccessrecord:vehicleAccessRecord:peccancy">
				<button id="peccancy" class="btn btn-danger" disabled onclick="peccancy()">
					<i class="glyphicon glyphicon-remove"></i> 标记违章
				</button>
		   </shiro:hasPermission>
		   <shiro:hasPermission name="vehicleaccessrecord:vehicleAccessRecord:export">
				<button id="export" class="btn btn-warning">
					<i class="fa fa-file-excel-o"></i> 导出
				</button>
		   </shiro:hasPermission>
		<button id="record" class="btn btn-warning" onclick="record()">
			<i class="fa fa-file-excel-o"></i> 违章记录
		</button>
		    </div>
		
	<!-- 表格 -->
	<table id="vehicleAccessRecordTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="vehicleaccessrecord:vehicleAccessRecord:view">
        <li data-item="view"><a>查看</a></li>
        </shiro:hasPermission>
    	<shiro:hasPermission name="vehicleaccessrecord:vehicleAccessRecord:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="vehicleaccessrecord:vehicleAccessRecord:del">
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