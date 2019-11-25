<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>安环报表</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="accessReportList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">安环报表</h3>
	</div>
	<div class="panel-body">
	
	<!-- 搜索 -->
	<div>
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="queryResult" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="报表类别：">报表类别：</label>
				<form:select path="type"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('report_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<div class="form-group">
						<label class="label-item single-overflow pull-left" title="日期：">&nbsp;日期：</label>
						<div class="col-xs-12">
							<div class="col-xs-12 col-sm-5">
								<div class='input-group date' id='date' style="left: -10px;" >
									<input type='text'  name="date" class="form-control"  />
									<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
								</div>
							</div>
						</div>
					</div>
				</div>


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

	<!-- 表格 -->
	<table id="reportTable"   data-toolbar="#toolbar" data-show-export="true">

	</table>


	</div>
	</div>
	</div>
	<script >

        //滚动条
        $(function(){
            var divel =  $("<div style='display:inline-block;position:fixed;bottom:0;left:0;width:100%;height:20px;background:#fff;overflow:auto;' ><div style='display:inline-block;height:30px;'></div></div>").appendTo("body");

            $(".fixed-table-body").css("overflow-x","hidden");
            $(divel).find(">div").width( $(".fixed-table-body>table" ).width() + $(".fixed-table-body").width());
            $(divel).scroll(function(){
                $(".fixed-table-body").scrollLeft($(this).scrollLeft());
            });
            $(".fixed-table-body").resize(function(){
                $(divel).find(">div").width( $(".fixed-table-body>table" ).width() + $(".fixed-table-body").width());
            });
        })


	</script>
</body>
</html>