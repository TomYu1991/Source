<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>磅单管理管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="weightList.js" %>
	<script src="${pageContext.request.contextPath}/webpage/include/deviceControl.js"></script>
	<script src="${pageContext.request.contextPath}/webpage/include/device.js"></script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">磅单管理列表</h3>
	</div>
	<div class="panel-body">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="weight" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="委托单号：">委托单号：</label>
				<form:input path="consignId" htmlEscape="false"   class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="磅单号：">磅单号：</label>
				<form:input path="weighNo" htmlEscape="false"   class=" form-control"/>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="品名：">品名：</label>
					<form:input path="prodCname" htmlEscape="false"   class=" form-control"/>
				</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="车牌号：">车牌号：</label>
				<form:input path="vehicleNo" htmlEscape="false"   class=" form-control"/>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="收货方：">收货方：</label>
					<form:input path="consigneUser" htmlEscape="false" maxlength="20"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="发货方：">发货方：</label>
					<form:input path="supplierName" htmlEscape="false" maxlength="20"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="称重类型：">称重类型：</label>
					<form:select path="weightType"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('weight_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="状态：">磅单状态：</label>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('weigh_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="锁车标记：">锁皮标记：</label>
					<form:select path="defaultFlag"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('default_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="过磅状态：">过磅状态：</label>
					<form:select path="weightFlag"  class="form-control m-b">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('weight_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="一次过磅工作站：">一次过磅工作站：</label>
				<form:select path="fistStation"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('workststion_ip')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="二次过磅工作站：">二次过磅工作站：</label>
				<form:select path="secondStation"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('workststion_ip')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="同步状态：">同步状态：</label>
				<form:select path="dataType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('syn_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<div class="form-group">
						<label class="label-item single-overflow pull-left" title="过磅时间：">&nbsp;过磅时间：</label>
						<div class="col-xs-12">
							<div class="col-xs-12 col-sm-5">
								<div class='input-group date' id='begintaretime' style="left: -10px;" >
									<input type='text'  name="begintaretime" class="form-control"  />
									<span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
								</div>
							</div>
							<div class="col-xs-12 col-sm-1">
								~
							</div>
							<div class="col-xs-12 col-sm-5">
								<div class='input-group date' id='endtaretime' style="left: -10px;" >
									<input type='text'  name="endtaretime" class="form-control" />
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
		<shiro:hasPermission name="weight:weight:cancel">
			<button id="cancel" class="btn btn-danger" disabled onclick="cancel()">
				<i class="glyphicon glyphicon-remove"></i> 作废
			</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="weight:weight:backquit">
			<button id="back" class="btn btn-danger" disabled onclick="backQuit()">
				<i ></i> 磅单回退
			</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="weight:weight:exchange">
			<button id="change" class="btn btn-success" disabled onclick="change()">
				 毛皮互换
			</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="weight:weight:edit">
			<button id="edit" class="btn btn-success" disabled onclick="edit()">
				<i class="glyphicon glyphicon-edit"></i> 修改
			</button>
			<button id="mergeWeight" class="btn btn-primary" onclick="mergeWeight()">
				合并数据
			</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="weight:weight:makeup">
		<button id="bdbill" class="btn btn-danger" disabled onclick="bdbill()">
			<i ></i> 补打票据
		</button>
		<button id="bdweight" class="btn btn-danger" disabled onclick="bdweight()">
			<i ></i> 补打磅单
		</button>
		<select class="btn btn-default" id="selectprint" style="width: 150px;height:40px;display: inline-block" onchange="selectprint()">
			<option value="" label="请选择打印机"></option>
			<c:forEach items="${fns:getDictList('dyj_deviceip')}" var="dict">
				<option value="${dict.value}">${dict.label}</option>
			</c:forEach>
		</select>
		</shiro:hasPermission>
			<shiro:hasPermission name="weight:weight:export">
				<button id="export" class="btn btn-warning">
					<i class="fa fa-file-excel-o"></i> 导出
				</button>
			 </shiro:hasPermission>
		<shiro:hasPermission name="weight:weight:synchronization">
			<button id="synData" class="btn btn-primary" onclick="synData()">
				同步数据
			</button>

			<button id="weigh" class="btn btn-primary" onclick="weightConsign()">
				车辆过磅
			</button>
			<button id="weigh" class="btn btn-primary" onclick="gdbweightConsign()">
				铁水罐过磅
			</button>
		</shiro:hasPermission>
		    </div>
		
	<!-- 表格 -->
	<table id="weightTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="weight:weight:view">
        <li data-item="view"><a>查看</a></li>
        </shiro:hasPermission>
    	<shiro:hasPermission name="weight:weight:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="weight:weight:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>
	</div>
	</div>
	</div>

    <script >
		/*//滚动条
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
        })*/
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