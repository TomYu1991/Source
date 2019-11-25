<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>车辆管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="carKind1TreeList.js" %>
	<%@include file="car1List.js" %>
	<script type="text/javascript">

        var nZoom=1;
        window.onbeforeunload = onunload_handler; //默认获取卸载事件


        function DelOcx()	//卸载控件

        {
            onunload_handler();
        }

        function OpenV1()	//打开视频1
        {
            DOcxtest1.OpenDevice(1);
            //DOcxtest1.SetFbl(2048,1536);
        }

        function OpenV2()	//打开视频2
        {

            DOcxtest1.OpenDevice(2);
        }

        function OpenV3()	//打开视频3
        {
            //DOcxtest1.SetMjpg(3,1);
            DOcxtest1.OpenDevice(3);
        }

        function bb()	//拍照存本地
        {

            DOcxtest1.SetQuality(60);
            DOcxtest1.GetJpg("D:\\tp\\1234.jpg",0);//拍一张图按照指定文件名存指定路径数值0表示图片不旋转
            var s = DOcxtest1.ImageToBase64("D:\\tp\\1234.jpg");  //把这张图生成base64编码base64
            //IE8及以下BASE64在网页中预览图片受字节限制无法预览完全，建议上传服务器后再载入IE中预览。
            if(CheckIEVersion() <= 8)
            {
                var strFileName = "D:\\tp\\1234.jpg"
                ShowPreview(strFileName);
            }
            else
            {
                s = "data:image/jpg;base64," + s;
                var pic = document.getElementById("Base64IMG");
                pic.src= null;
                pic.src= s;
            }
            alert(s.length);
            //getStr();

        }

        function bbase64()	//拍照不存本地，内存直接获取base64编码
        {
            DOcxtest1.SetQuality(60);
            var s =DOcxtest1.GetJpgBase64(0);     //拍一张图获取base64码  0正常方向
            //IE8及以下BASE64在网页中预览图片受字节限制无法预览完全，建议上传服务器后再载入IE中预览。
            if(CheckIEVersion() <= 8)
            {
                s = "data:image/jpg;base64," + s;
                var pic = document.getElementById("Base64IMG");
                pic.src= null;
                pic.src= s;
            }
            else
            {
                s = "data:image/jpg;base64," + s;
                var pic = document.getElementById("Base64IMG");
                pic.src= null;
                pic.src= s;
            }
            alert(s);
        }



        function CheckIEVersion()	//检查浏览器版本
        {
            var iDx = 6;
            var b = document.createElement('b');
            //return b.getElementsByTagName('i').length === 1
            for(; iDx < 12; iDx++)
            {
                b.innerHTML = '<!--[if IE ' + iDx + ']><i></i><![endif]-->';

                if(b.getElementsByTagName('i').length === 1)
                {
                    //alert("Now is IE" + iDx.toString());
                    break;
                }
            }
            return iDx;
        }




        function ShowPreview(strFileName)
        {
            var pic = document.getElementById("Base64IMG");
            var ext = strFileName.substring(strFileName.lastIndexOf(".")+1).toLowerCase();
            pic.style.filter = null;
            pic.src = null;
            pic.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src=\"" + strFileName + "\")";
            var s = "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
            pic.src = s;

        }

        function onunload_handler()
        {
            var active_object_id='DOcxtest1'; //activex的控件id
            var activex_obj=document.getElementById(active_object_id);
            var parent_element=activex_obj.parentElement; //找到控件的父元素

            //删除activex父元素的所有子元素
            while (parent_element.children.length>0)
            {
                //var warning="删除控件";
                //alert(warning);
                parent_element.removeChild(parent_element.children[0]);
            }
        }


        function SetResolution()
        {

            DOcxtest1.SetFbl(1600,1200);//如果单独设置某一个摄像头分辨率请填写摄像头序号，如：gpy.SetFbl(1,1600, 1200)；其中1表示摄像头1、2表示辅助镜头2、3表示辅助镜头3.

        }
        function SetOrientation()	//设置动态视频方向
        {
            DOcxtest1.SetTurn(2);
        }

        function SetQuality()		//设置JPG图片质量减少存储大小，建议数值为60
        {
            DOcxtest1.SetQuality(50);
        }

        function SetAutoCutOn()	//开启自动裁边
        {
            DOcxtest1.SetAutoCut(1);
        }

        function SetAutoCutOff()	//关闭自动裁边
        {
            DOcxtest1.SetAutoCut(0);
        }

        function ZoomOut()	//设置缩放视频
        {
            DOcxtest1.SetZoom(3);	//缩放1级的倍数  范围从1到10    如 3 为 0.3
            if(nZoom<10)
            {
                nZoom++;
            }
            DOcxtest1.VideoZoom(nZoom);
        }

        function ZoomIn()
        {
            if(nZoom>0)
            {
                nZoom--;
            }
            DOcxtest1.VideoZoom(nZoom);
        }

        function TestBase64()
        {

            var s = DOcxtest1.TestEnBase64("123456789");

            alert(s);
        }

        function printimage()		//打印图片
        {
            DOcxtest1.PrintImage("d:\\tp\\1234.jpg");
        }

        function GetInitRet()		//初始化设备得到返回值
        {
            var a = DOcxtest1.GetInitRet();
            switch(a)
            {
                case 2:
                    alert("视频未打开或设备连接中断");
                    break;

                case 1:
                    alert("成功");
                    break;

                case 0:
                    alert("不是标准设备");
                    break;
            }
        }

        function getStr(){
            var str = "";
            var tempStrbak="";
            DOcxtest1.resetPictureStr();//将DOcxtest1.pictureStr指向第一段BASE64段
            while(true){
                var tempStr = DOcxtest1.pictureStr;//读取当前返回的BASE64段
                if(tempStr == null || tempStr == ''){
                    break;
                } else {
                    str += tempStr;
                }
                tempStrbak = tempStr;
                DOcxtest1.getPictureStrNext();//将DOcxtest1.pictureStr更新为下一个BASE64段
            }
            document.getElementById('rtnStr').innerHTML = str;
            //alert(str);
            //alert(tempStrbak);
        }

        function GetPid()	//获取当前设备PID
        {
            var a = DOcxtest1.GetPid();
            alert(a);
        }
        function GetFbl()	//获取当前设备所有分辨率
        {
            var a = DOcxtest1.GetFbl();
            alert(a);
        }

        function SetBox()	//设置固定坐标值裁边
        {
            DOcxtest1.SetBox(100,100,600,600);

        }
        function SetBox2()	//取消固定坐标值裁边
        {
            DOcxtest1.SetBox(0,0,0,0);

        }
        function CloseVideo()	//关闭视频
        {
            DOcxtest1.CloseDevice(1);
        }
        function ShowErrorInfo()//开启调试信息
        {
            DOcxtest1.ShowErrorInfo(1);
        }
        function ShowResetInfo()//获取当前手动裁边坐标及长高
        {
            var str = DOcxtest1.GetPresetStr();
            alert(str);
        }
        function SetPDFPath()//设置PDF临时文件夹目录
        {
            DOcxtest1.SetTempDir("d:\\tp\\temp");
        }
        function BeginPDF()	//开始进入PDF状态
        {
            SetPDFPath();	//设置临时文件目录
            DOcxtest1.StartPDF();
            document.getElementById("IMG1").src= null;
            document.getElementById("IMG2").src= null;
            document.getElementById("IMG3").src= null;
        }
        function GetPDF()	//拍照PDF图片
        {

            document.getElementById("IMG1").src= null;
            document.getElementById("IMG1").src= document.getElementById("IMG2").src;

            document.getElementById("IMG2").src= null;
            document.getElementById("IMG2").src= document.getElementById("IMG3").src;
            DOcxtest1.SetQuality(50);
            var s =DOcxtest1.GetPicPDF(0);     //拍一张图  0正常方向
            s = "file://" + s;

            document.getElementById("IMG3").src= null;
            document.getElementById("IMG3").src= s;
        }
        function DelPDF1()	//删除图片
        {
            DOcxtest1.DelPicPDF(3); 	//删除倒数第三张

            //缩略图重新移位显示
            document.getElementById("IMG1").src= null;
        }
        function DelPDF2()
        {
            DOcxtest1.DelPicPDF(2);  //删除倒数第二张

            //缩略图重新移位显示

            document.getElementById("IMG2").src= null;
            document.getElementById("IMG2").src= document.getElementById("IMG1").src;
            document.getElementById("IMG1").src= null;
        }
        function DelPDF3()
        {
            DOcxtest1.DelPicPDF(1);  //删除倒数第一张

            //缩略图重新移位显示
            document.getElementById("IMG3").src= null;
            document.getElementById("IMG3").src= document.getElementById("IMG2").src;

            document.getElementById("IMG2").src= null;
            document.getElementById("IMG2").src= document.getElementById("IMG1").src;

            document.getElementById("IMG1").src= null;
        }

        function EndPDF()	//合并PDF
        {
            DOcxtest1.StopPDF("D:\\tp\\1234.pdf");
        }

        function SetFtp()
        {
            DOcxtest1.SetupFtp("10.59.119.85",21,"zou","123456");
        }
        function EnableFtp()
        {
            DOcxtest1.EnableFtp(1);
        }

        function SetFtpPath()
        {
            DOcxtest1.SetFtpPath("\\image\\");
        }


        function FormatDlg()	//视频格式窗体调用接口
        {
            DOcxtest1.FormatDlg();
        }
        function PropertyDlg()	//视频属性窗体调用接口
        {
            DOcxtest1.PropertyDlg();
        }
        function bbStartClean()	//开启自动去底色适应打印纯白底色
        {
            DOcxtest1.SetCleanParam(300,2,8);
        }
        function bbCloseClean()	//关闭自动去底色功能恢复正常截图
        {
            DOcxtest1.SetCleanParam(-1,-1,-1);
        }


	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">车辆列表</h3>
	</div>
	<div class="panel-body">
		<div class="row">
				<div class="col-sm-4 col-md-3" >
					<div class="form-group">
						<div class="row">
							<div class="col-sm-10" >
								<div class="input-search">
									<button type="submit" class="input-search-btn">
										<i class="fa fa-search" aria-hidden="true"></i></button>
									<input   id="search_q" type="text" class="form-control input-sm" name="" placeholder="查找...">

								</div>
							</div>
							<div class="col-sm-2" >
								<button  class="btn btn-default btn-sm"  onclick="jp.openSaveDialog('新建车系', '${ctx}/test/treetable/dialog/carKind1/form','800px', '500px')">
									<i class="fa fa-plus"></i>
								</button>
							</div>
						</div>
					</div>
					<div id="carKind1jsTree" style="overflow-x:auto; border:0px;"></div>
				</div>
				<div  class="col-sm-8 col-md-9">
	

	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="test:treetable:dialog:car1:add">
				<button id="add" class="btn btn-primary" onclick="OpenV1()">
					<i class="glyphicon glyphicon-plus"></i> 新建
				</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="test:treetable:dialog:car1:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="test:treetable:dialog:car1:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="test:treetable:dialog:car1:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="test:treetable:dialog:car1:export">
	        		<button id="export" class="btn btn-warning">
					<i class="fa fa-file-excel-o"></i> 导出
				</button>
			 </shiro:hasPermission>
	                 <shiro:hasPermission name="test:treetable:dialog:car1:view">
				<button id="view" class="btn btn-default" disabled onclick="view()">
					<i class="fa fa-search-plus"></i> 查看
				</button>
			</shiro:hasPermission>
		    </div>
	</div>
	</div>
	</div>
	<%--<TABLE id=Table3 border=1 cellSpacing=1 cellPadding=1 width=1024 height=221>--%>
		<%--<TR>--%>
			<%--<TD width=84>打开视频</TD>--%>
			<%--<TD>--%>
				<%--<INPUT id=button15 type=button value=打开视频1 name=button1 onclick="OpenV1();" style="Z-INDEX: 0">--%>

			<%--<TD width="300" rowspan="5"><IMG ID="Base64IMG" height="225" width="300" src=""></IMG></TD>--%>
		<%--</TR>--%>

		<%--<TR>--%>
			<%--<TD width=84 height=79>视频操作</TD>--%>
			<%--<TD height=79>--%>

				<%--<INPUT id=button12 type=button value=取分辨率 name=button16 onclick=GetFbl();   style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=取PID name=button15  onclick="GetPid();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button3 type=button value=设置分辨率 name=button3 onclick="SetResolution();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button4 type=button value=设置视频方向 name=button4 onclick="SetOrientation();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=设置质量 name=button6  onclick="SetQuality();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=自动裁边开 name=button8  onclick="SetAutoCutOn();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=自动裁边关 name=button9  onclick="SetAutoCutOff();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=视频放大 name=button7  onclick="ZoomOut();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=视频缩小 name=button7  onclick="ZoomIn();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=预设框选 name=button17  onclick="SetBox();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=当前预设 name=button19  onclick="ShowResetInfo();"style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=取消预设 name=button18  onclick="SetBox2();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=视频格式 name=button19  onclick="FormatDlg();"style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=视频属性 name=button18  onclick="PropertyDlg();" style="Z-INDEX: 0">--%>
			<%--</TD></TR>--%>

		<%--<TR>--%>
			<%--<TD width=84>截图</TD>--%>
			<%--<TD>--%>
				<%--<INPUT id=button1 type=button value=截图 name=button1 onclick="bb();" style="Z-INDEX: 0">--%>
				<%--&lt;%&ndash;<INPUT id=button1 type=button value=内存截图[Base64] name=button19 onclick="bbase64();" style="Z-INDEX: 0">&ndash;%&gt;--%>
				<%--&lt;%&ndash;<INPUT id=button6 type=button value=Base64测试 name=button7  onclick="TestBase64();" style="Z-INDEX: 0">&ndash;%&gt;--%>
				<%--&lt;%&ndash;<INPUT id=button1 type=button value=开启自动去底色 name=button19 onclick="bbStartClean();" style="Z-INDEX: 0">&ndash;%&gt;--%>
				<%--&lt;%&ndash;<INPUT id=button6 type=button value=关闭自动去底色 name=button7  onclick="bbCloseClean();" style="Z-INDEX: 0">&ndash;%&gt;--%>

		<%--<TR>--%>
			<%--<TD width=84>PDF 功能</TD>--%>
			<%--<TD>--%>
				<%--<INPUT style="Z-INDEX: 0" id=Button13 onclick=SetPDFPath(); value=设置PDF临时目录 type=button name=button1>--%>
				<%--<INPUT style="Z-INDEX: 0" id=Button9 onclick=BeginPDF(); value=开始制作PDF type=button name=button1>--%>
				<%--<INPUT style="Z-INDEX: 0" id=Button10 onclick=GetPDF(); value=PDF拍照 type=button name=button1>--%>
				<%--<INPUT style="Z-INDEX: 0" id=Button11 onclick=EndPDF(); value=完成制作 type=button name=button1>--%>
			<%--</TD>--%>
		<%--</TR>--%>
		<%--<TR>--%>
			<%--<TD width=84>其它操作</TD>--%>
			<%--<TD>--%>
				<%--<!--<INPUT id=button1 type=button value=ftp设置 name=button1 onclick="SetFtp();" style="Z-INDEX: 0">--%>
                <%--<INPUT id=button1 type=button value=设置ftp目录 name=button10 onclick="SetFtpPath();" style="Z-INDEX: 0">--%>
                <%--<INPUT id=button1 type=button value=开启ftp上传 name=button11 onclick="EnableFtp();" style="Z-INDEX: 0">--%>
                <%--<INPUT id=button2 type=button value=设置保存路径 name=button2  onclick="SetPath();" style="Z-INDEX: 0"></TD></TR>>-->--%>
				<%--<INPUT id=button6 type=button value=打印 name=button13  onclick="printimage();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=启用错误提示 name=button18  onclick="ShowErrorInfo();" style="Z-INDEX: 0">--%>
				<%--<INPUT id=button6 type=button value=删除ocx name=button19  onclick="DelOcx();" style="Z-INDEX: 0"></TD></TR></TABLE></P>--%>

	<TABLE id=Table1 border=1 cellSpacing=1 cellPadding=1 width=1024 height=300>
		<TR>
			<TD width=765>
				<OBJECT Width=640 height=481 id="DOcxtest1" classid=clsid:95ECFA19-1D62-4F14-BB83-DF2503E8888F
						bcbocx.CAB"><PARAM NAME="_Version" VALUE="65536"><PARAM NAME="_ExtentX" VALUE="2646"><PARAM NAME="_ExtentY" VALUE="1323"><PARAM NAME="_StockProps" VALUE="0">
				</OBJECT>
			</TD>
			<TD>
				<TABLE id=Table2 border=0 cellSpacing=1 cellPadding=1 width=300
					   height=361>
					<TR>
						<TD height=218><IMG style="Z-INDEX: 0" id=IMG3
											src="" width=300 height=221></TD></TR>

	<P>
	</P>
	<P></IMG>&nbsp;&nbsp;&nbsp;</IMG>&nbsp;&nbsp;</IMG>&nbsp;
	</P>

	<tr id="rtnStr"></tr>
</body>
</html>