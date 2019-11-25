<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备点检子项配置</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="deviceCheckItemList.js" %>
	<script type="text/javascript">
		var gdhVoice=function() {
			var audio = document.createElement("audio");
			audio.src = ctxStatic+"/common/voice/40509.mp3";
			audio.play();
		};
		$(function(){
			$("#yy").click(function () {
				gdhVoice();
			});
		})
	</script>
</head>
<body class="bg-white">
<a href="#" id="yy">语音提示</a>

</body>
</html>