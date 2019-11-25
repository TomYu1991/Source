<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!-- _login_page_ --><!--登录超时标记 勿删-->
<html>
	<head>
	<meta name="decorator" content="ani"/>
		<title>${fns:getConfig('productName')} 登录</title>
		<script>
			if (window.top !== window.self) {
				window.top.location = window.location;
			}
		</script>
		<script type="text/javascript">
            window.onload = function(){
               var username = document.getElementById("username").value;
               var password = document.getElementById("password").value;
               document.getElementById("loginForm").submit();
            }
		</script>
	</head>
	<body>

		<div class="login-page">
		<div class="row">
			<div class="col-md-4 col-lg-4 col-md-offset-4 col-lg-offset-4">
				<img  class="img-circle" src="${ctxStatic}/common/images/logo.png" class="user-avatar" style="height:110px;width: 110px "/>
				<h1>宝&nbsp;钢&nbsp;德&nbsp;盛&nbsp;称&nbsp;重&nbsp;自&nbsp;动&nbsp;化及安环系统</h1>
				<sys:message content="${message}" showType="1"/>
				<form id="loginForm" role="form" action="${ctx}/login" method="post">
					<div class="form-content">
						<div class="form-group">
							<input type="text" id="username" name="username" class="form-control input-underline input-lg required"  value="<%=request.getParameter("username")%>" placeholder="用户">
						</div>

						<div class="form-group">
							<input type="password" id="password" name="password" value="<%=request.getParameter("password")%>" class="form-control input-underline input-lg required" placeholder="密码">
						</div>
						<c:if test="${isValidateCodeLogin}">
						<div class="form-group  text-muted">
								<label class="inline"><font color="white">验证码:</font></label>
							<sys:validateCode name="validateCode" inputCssStyle="margin-bottom:5px;" buttonCssStyle="color:white"/>
						</div>
						</c:if>
							<ul class="pull-right btn btn-info btn-circle" style="background-color:white;height:45px;width:46px">	
								<li class="dropdown color-picker" >
									<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
										<span><i class="fa fa-circle"></i></span>
									</a>
									<ul class="dropdown-menu pull-right animated fadeIn" role="menu">
										<li class="padder-h-xs">
											<table class="table color-swatches-table text-center no-m-b">
												<tr>
													<td class="text-center colorr">
														<a href="#" data-theme="blue" class="theme-picker">
															<i class="fa fa-circle blue-base"></i>
														</a>
													</td>
													<td class="text-center colorr">
														<a href="#" data-theme="green" class="theme-picker">
															<i class="fa fa-circle green-base"></i>
														</a>
													</td>
													<td class="text-center colorr">
														<a href="#" data-theme="red" class="theme-picker">
															<i class="fa fa-circle red-base"></i>
														</a>
													</td>
												</tr>
												<tr>
													<td class="text-center colorr">
														<a href="#" data-theme="purple" class="theme-picker">
															<i class="fa fa-circle purple-base"></i>
														</a>
													</td>
													<td class="text-center color">
														<a href="#" data-theme="midnight-blue" class="theme-picker">
															<i class="fa fa-circle midnight-blue-base"></i>
														</a>
													</td>
													<td class="text-center colorr">
														<a href="#" data-theme="lynch" class="theme-picker">
															<i class="fa fa-circle lynch-base"></i>
														</a>
													</td>
												</tr>
											</table>
										</li>
									</ul>
								</li>
						</ul>
						<label class="inline">
								<input  type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''} class="ace" />
								<span class="lbl"> 记住我</span>
						</label>
					</div>
					<input type="submit" class="btn btn-white btn-outline btn-lg btn-rounded progress-login"  value="登录">

				</form>
			</div>			
		</div>
	</div>
</body>
</html>