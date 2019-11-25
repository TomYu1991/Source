<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>首页</title>
	<meta name="decorator" content="ani"/>
	<style>
		#body-container {
			margin-left: 0px !important;
			/**padding: 10px;*/
			margin-top: 0px !important;
			overflow-x: hidden!important;
			transition: all 0.2s ease-in-out !important;
			height: 100% !important;
			background:#fbfcfd;
		}
		#body-container > img {
			position: absolute;
			top:50%;
			left:50%;
			transform: translate(-50%,-50%);
		}
	</style>
	<script src="${pageContext.request.contextPath}/webpage/include/device.js"></script>
	<script>
		var obj = {};
        $(function () {
            jp.get("${ctx}/checkAddr", function (data) {
                if(data.success){
                    //打开监控点车号识别
                    if(data.object==1){
                        commonchsb();
                    }
                    //打开监控点RFID
                    if(data.object==3){
                        commonbinding();
                        info();
                    }
                    //接受监控点报警信息
                    if(data.object==2){
                        info();
                    }
                }
            });
        })
        function commonchsb(){
            jp.get("${ctx}/illegalinfo/illegalInfo/queryByPosition?flag=1", function (data) {
                if(data.success){
                    var position = data.data;
                    for(var i= 0;i<position.length;i++){
                        console.log("初始值："+position[i].deviceNo);
                        var str = position[i].deviceNo;
                        orderOpen(str)
                        console.log("!!!!!!!!!!!!!!"+str)
                    }
                }
            })
        }
        function orderOpen(str){
            device.CarLicence.open({position:str,callback:function(data,ws){
                    var data = eval("("+data+")");
                    var value = data.Data;
                    console.log("打开识别车号："+str);
                    device.CarLicence.monitor({position:str,isclose:false, interval:500,count:0,
                        callback:function(data,ws){
                            var data = eval("("+data+")");
                            var value = data.Data;
                            console.log(value)
                            console.log("识别车号:"+str);
                            // value 识别内容
                            if(value.length>5){
                                value = value.substring(value.lastIndexOf("#") + 1, value.length+1);
                                jkdplateRecog(value,"",str);
                            }else{
                                if(obj[value]!=null){
                                    var time = obj[value];
                                    var o= Date.now()-time;
                                    if(o<15000){
                                        return;
                                    }else{
                                    }
                                }else {
                                    obj[value]=Date.now();
                                }
                            }
                        }
                    })
                }
            });
        }
        function commonbinding(){
            jp.get("${ctx}/illegalinfo/illegalInfo/queryByPosition?flag=2", function (data) {
                if(data.success){
                    console.log(data.data)
                    var position = data.data;
                    for(var i= 0;i<position.length;i++){
                        console.log(position[i].deviceNo);
                        var str = position[i].deviceNo;
                        openRFID(str);
                    }
                }
            })
        }
        function openRFID(str){
            device.RFID.open({
                position:str,
                //isclose:false,	//执行完毕后不关闭连接
                callback:function(data,ws){ //回调函数
                    //打开rfid
                    var dkrfiddata = eval("("+data+")");
                    //返回rfid状态
                    var dkrfid = dkrfiddata.Data;
                    if("2"==dkrfid || "1"==dkrfid){
                        console.log("打开的RFID设备号为："+str);
                        //获取数据
                        device.RFID.getdata({
                            position:str,
                            isclose:false,	//执行完毕后不关闭连接
                            count:0,	//获取数据次数，0 为一直获取
                            interval:100,	//获取数据时间间隔(1000毫秒)
                            data:"{istid:true}",
                            callback:function(sjdata,ws){
                                //获取rfid
                                var data = eval("("+sjdata+")");
                                //返回rfid数据
                                var rfid = data.Data;
                                if(typeof rfid != "undefined" && rfid != "" && rfid != "-1" && rfid != "1" && rfid != "2") {
                                    console.log("读到的RFID数据:" + rfid)
                                    jkdplateRecog("",rfid,str);
                                }
                            }
                        });
                    }else{
                        console.log("RFID:"+dkrfiddata.Describe);
                    }
                }
            });
        }
        function info(){
            device.Route.get({
                position:"Z01",
                data:"{key:'jiankongdianfasong'}",
                isclose:false,
                count:0,
                interval:100,
                callback:function(data){
                    var data_ = eval("("+data+")");
                    var value = data_.Data;
                    if(value.length>0&&value != -1){
                        device.Route.set({
                            position:"Z01",
                            data:"{key:'jiankongdianfasong',value:''}",
                            callback:function(data,ws){
                                console.log(value);
                                top.layer.alert(value);
                                jp.voice();
                            }
                        });
                    }
                }
            });
        }
        function jkdplateRecog(value,rfid,position) {
            if(value != "".trim() ||typeof (value) != "undefined"){
                if(localStorage.getItem(value)!=null){
                    var time = localStorage.getItem(value);
                    var times= Date.now()-time;
                    if(times<15000){
                        return;
                    }else{
                        localStorage.clear();
                    }
                }else {
                    localStorage.setItem(value, Date.now());
                    console.log("识别到的车牌号:" + value);
                    console.log("position:" + position);
                    jp.get("${ctx}/illegalinfo/illegalInfo/checkPower?vehicleNo=" + value + "&rfidNo=" + rfid + "&position=" + position, function (data) {
                        if (data.success) {
                        } else {
                            var ip = data.object;
                            zhuapaiill(value, rfid, ip);
                            var info = data.msg;
                            device.ip = "10.12.240.130";
                            device.Route.set({ position:"Z01",isclose:true,data:"{key:'jiankongdianfasong',value:'"+info+"'}" });
                            device.ip = "10.12.240.110"
                            device.Route.set({ position:"Z01",isclose:true,data:"{key:'jiankongdianfasong',value:'"+info+"'}" });
                            device.ip = "10.12.240.120"
                            device.Route.set({ position:"Z01",isclose:true,data:"{key:'jiankongdianfasong',value:'"+info+"'}" });
                            device.ip = "10.12.241.100"
                            device.Route.set({position: "Z01",isclose:true,data: "{key:'jiankongdianfasong',value:'" + info + "'}"});
                        }
                    })
                }
            }else {
                if (localStorage.getItem(rfid) != null) {
                    var time = localStorage.getItem(rfid);
                    var times = Date.now() - time;
                    if (times < 15000) {
                        return;
                    } else {
                        localStorage.clear();
                    }
                } else {
                    localStorage.setItem(rfid, Date.now());
                    console.log("设别到的RFID为:" + rfid);
                    console.log("position:" + position);
                    jp.get("${ctx}/illegalinfo/illegalInfo/checkPower?vehicleNo=" + value + "&rfidNo=" + rfid + "&position=" + position, function (data) {
                        if (data.success) {
                        } else {
                            var ip = data.object;
                            zhuapaiill(value, rfid, ip);
                            var info = data.msg;
                            device.ip = "10.12.241.100"
                            device.Route.set({ position:"Z01",isclose:true,data:"{key:'jiankongdianfasong',value:'"+info+"'}" });
                            device.ip = "10.12.240.110"
                            device.Route.set({ position:"Z01",isclose:true,data:"{key:'jiankongdianfasong',value:'"+info+"'}" });
                            device.ip = "10.12.240.120"
                            device.Route.set({ position:"Z01",isclose:true,data:"{key:'jiankongdianfasong',value:'"+info+"'}" });
                            device.ip = "10.12.240.130"
                            device.Route.set({position: "Z01",isclose:true,data: "{key:'jiankongdianfasong',value:'" + info + "'}"});
                        }
                    })
                }
            }
        }
        //抓拍
        function zhuapaiill(vehicleNo,rfid,ip){
            jp.get("${ctx}/videos/videos/queryVideoCamera?stationIp="+ip, function (data) {
                if (data.success) {
                    var v= data.data;
                    var spConfig = v.videoCamera;
                    console.log("监控点抓拍通道号："+spConfig);

                    var minecode = 'gateimage';
                    var value=0;
                    device.ip="10.12.241.70";
                    device.Video.sendmsg({
                        position:"C01",
                        data:"{sendmsg:'TakePic,"+minecode+","+spConfig+"'}",
                        callback:function(data,ws){
                            var data = eval("("+data+")");
                            value = data.Data;
                            console.log("图片~~~~~~~~~~~~~"+value);
                            saveweightpic(value,vehicleNo,rfid);
                        }
                    });
                }
            });
        }
        function saveweightpic(value,vehicleNo,rfid) {
            //获取抓拍图片
            //判断稳定后获取抓拍.
            console.log("@@@@@@@@@@@@@@@@@@@@@");
            var weight = $("#weight").val();

            var pic = value;
            var pic1 = pic.split("#")[1];

            var picz = "";

            if (pic1.split(",")[0] == "true") {
                picz = pic1.split(",")[2] + ",";
            } else {
                picz = ",";
            }

            var piczs = picz.split(",")
            var ct = piczs[0].toString();

            console.log("==========="+ct)
            jp.get("${ctx}/illegalinfo/illegalInfo/savePhoto?vehicleNo=" + vehicleNo +"&rfidNo="+rfid+ "&Photo=" + ct, function (data) {
            })
        }
	</script>
</head>
<body class="">
<div id="body-container" class="wrapper wrapper-content">

	<img src="${ctxStatic}/common/images/content.png" height="100%" />
	<%--<div class="conter-wrapper home-container">--%>
		<%--<div class="row home-row">--%>
			<%--<div class="col-md-4 col-lg-3">--%>
				<%--<div class="home-stats">--%>
					<%--<a href="#" class="stat hvr-wobble-horizontal">--%>
						<%--<div class=" stat-icon">--%>
							<%--<i class="fa fa-cloud-upload fa-4x text-info "></i>--%>
						<%--</div>--%>
						<%--<div class=" stat-label">--%>
							<%--<div class="label-header">--%>
								<%--88%--%>
							<%--</div>--%>
							<%--<div class="progress-sm progress ng-isolate-scope" value="progressValue" type="info">--%>
								<%--<div class="progress-bar progress-bar-info" role="progressbar"--%>
									 <%--aria-valuenow="88" aria-valuemin="0" aria-valuemax="100"  style="width: 88%;">--%>
								<%--</div>--%>
							<%--</div>--%>
							<%--<div class="clearfix stat-detail">--%>
								<%--<div class="label-body">--%>
									<%--<i class="fa fa-arrow-circle-o-right pull-right text-muted"></i>服务正常运行时间--%>
								<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</a>					<a href="#" class="stat hvr-wobble-horizontal">--%>
					<%--<div class=" stat-icon">--%>
						<%--<i class="fa fa-heartbeat fa-4x text-success "></i>--%>
					<%--</div>--%>
					<%--<div class=" stat-label">--%>
						<%--<div class="label-header">--%>
							<%--94%--%>
						<%--</div>--%>
						<%--<div class="progress-sm progress ng-isolate-scope" value="progressValue" type="info">--%>
							<%--<div class="progress-bar progress-bar-success" role="progressbar"--%>
								 <%--aria-valuenow="94" aria-valuemin="0" aria-valuemax="100"  style="width: 94%;">--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="clearfix stat-detail">--%>
							<%--<div class="label-body">--%>
								<%--<i class="fa fa-arrow-circle-o-right pull-right text-muted"></i>积极反馈--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</div>--%>
				<%--</a>					<a href="#" class="stat hvr-wobble-horizontal">--%>
					<%--<div class=" stat-icon">--%>
						<%--<i class="fa fa-flag fa-4x text-danger "></i>--%>
					<%--</div>--%>
					<%--<div class=" stat-label">--%>
						<%--<div class="label-header">--%>
							<%--88%--%>
						<%--</div>--%>
						<%--<div class="progress-sm progress ng-isolate-scope" value="progressValue" type="info">--%>
							<%--<div class="progress-bar progress-bar-danger" role="progressbar"--%>
								 <%--aria-valuenow="88" aria-valuemin="0" aria-valuemax="100"  style="width: 88%;">--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="clearfix stat-detail">--%>
							<%--<div class="label-body">--%>
								<%--<i class="fa fa-arrow-circle-o-right pull-right text-muted"></i>机器负载--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</div>--%>
				<%--</a>--%>
				<%--</div>--%>
			<%--</div>--%>
			<%--<div class="col-md-4 col-lg-6">--%>
				<%--<div class="home-charts-middle">--%>
					<%--<div class="chart-container">--%>
						<%--<div class="chart-comment clearfix">--%>
							<%--<div class="text-primary pull-left">--%>
								<%--<span class="comment-header">55%</span><br />--%>
								<%--<span class="comment-comment">搜素引擎</span>--%>
							<%--</div>--%>
							<%--<div class="text-success pull-left m-l">--%>
								<%--<span class="comment-header">25%</span><br />--%>
								<%--<span class="comment-comment">自主访问</span>--%>
							<%--</div>--%>
							<%--<div class="text-warning pull-left m-l">--%>
								<%--<span class="comment-header">20%</span><br />--%>
								<%--<span class="comment-comment">友情链接</span>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div id="lineChart" style="height:250px"></div>--%>
					<%--</div>--%>
				<%--</div>--%>
			<%--</div>--%>
			<%--<div class="col-md-4 col-lg-3">--%>
				<%--<div class="home-charts-right">--%>
					<%----%>
					<%--<div class="bottom-right-chart">--%>
						<%--<div class="chart-container box clearfix">--%>
							<%--<div class="row">--%>
								<%--<div class="col-sm-3 text-left">--%>
									<%--<div class="padder">--%>
										<%--<span class="heading">本周访问人数 : </span><br />--%>
										<%--<big class="text-primary">22068</big>--%>
									<%--</div>--%>
								<%--</div>--%>
								<%--<div class="col-sm-6">--%>
									<%--<div id="pie"  style="height: 298px;padding-top: 8px;max-height: 298px;position: relative;"></div>--%>
								<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</div>--%>
				<%--</div>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="row home-row">--%>
			<%--<div class="col-lg-8 col-md-6">--%>
				<%--<div class="map-container box padder">--%>
					<%--<!-- <div id="world-map" style="width: 100%; height: 320px"></div> -->--%>
					<%--<div class="top-right-chart row">--%>
								<%----%>
								<%--<div class="col-sm-12">--%>
									<%--<span class="heading">销售业绩 </span><br />--%>
									<%--<div id="cbar" style="height: 298px; padding-top:7px;"></div>--%>
								<%--</div>--%>
					<%--</div>--%>
				<%--</div>--%>
			<%--</div>--%>
			<%--<div class="col-lg-4 col-md-6">--%>
				<%--<div class="todo-container panel panel-danger">--%>
					<%--<div class="panel-heading">--%>
						<%--<div class="todo-header text-center">--%>
							<%--<h4><i class="fa fa-tasks"></i>&nbsp;待办任务</h4>--%>
						<%--</div>--%>
					<%--</div>--%>
					<%--<div class="panel-body bg-danger">--%>
						<%--<div class="todo-body">--%>
							<%--<div class="todo-list-wrap">--%>
								<%--<ul class="todo-list">--%>
									<%--<li class="">--%>
										<%--<label class="checkbox1" for="option1">--%>
											<%--<input id="option1" type="checkbox" class="">--%>
											<%--<span></span>--%>
										<%--</label>--%>
										<%--<span class="done-false">9:00开晨会安排工作</span>--%>
									<%--</li>--%>
									<%--<li class="">--%>
										<%--<label class="checkbox1" for="option3">--%>
											<%--<input id="option3" type="checkbox" class="">--%>
											<%--<span></span>--%>
										<%--</label>--%>
										<%--<span class="done-false">9:00~12:00客户需求分析</span>--%>
									<%--</li>--%>
									<%--<li class="">--%>
										<%--<label class="checkbox1" for="option4">--%>
											<%--<input id="option4" type="checkbox" class="">--%>
											<%--<span></span>--%>
										<%--</label>--%>
										<%--<span class="done-false">12:00和客户电话会议</span>--%>
									<%--</li>--%>
									<%--<li class="">--%>
										<%--<label class="checkbox1" for="option5">--%>
											<%--<input id="option5" type="checkbox" class="">--%>
											<%--<span></span>--%>
										<%--</label>--%>
										<%--<span class="done-false">2:00参加技术论坛</span>--%>
									<%--</li>--%>
									<%--<li class="">--%>
										<%--<label class="checkbox1" for="option2">--%>
											<%--<input id="option2" type="checkbox" class="">--%>
											<%--<span></span>--%>
										<%--</label>--%>
										<%--<span class="done-false">5:00晚会总结进度</span>--%>
									<%--</li>--%>
								<%--</ul>--%>
							<%--</div>--%>
							<%--<form class="form-horizontal todo-from-bottom">--%>
								<%--<div class="row">--%>
									<%--<div class="col-sm-12">--%>
										<%--<div class="input-group">--%>
											<%--<input type="text" class="form-control" placeholder="">--%>
											<%--<span class="input-group-btn">--%>
										<%--<button class="btn btn-default" type="submit">增加</button>--%>
									<%--</span>--%>
										<%--</div>--%>
									<%--</div>--%>
								<%--</div>--%>
							<%--</form>--%>
						<%--</div>--%>
					<%--</div>--%>
				<%--</div>--%>
			<%--</div>--%>
		<%--</div>--%>
	<%--</div>--%>
</div>

<script>
    $(function(){
        $('#calendar2').fullCalendar({
            eventClick: function(calEvent, jsEvent, view) {
                alert('Event: ' + calEvent.title);
                alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
                alert('View: ' + view.name);
            }
        });

        $('#rtlswitch').click(function() {
            console.log('hello');
            $('body').toggleClass('rtl');

            var hasClass = $('body').hasClass('rtl');

            $.get('/api/set-rtl?rtl='+ (hasClass ? 'rtl': ''));

        });
        $('.theme-picker').click(function() {
            changeTheme($(this).attr('data-theme'));
        });
        $('#showMenu').click(function() {
            $('body').toggleClass('push-right');
        });

    });
    function changeTheme(the) {
        $("#current-theme").remove();
        $('<link>')
            .appendTo('head')
            .attr('id','current-theme')
            .attr({type : 'text/css', rel : 'stylesheet'})
            .attr('href', '/css/app-'+the+'.css');
    }
</script>

<script>
    $(function(){
        setTimeout(function() {
            var chart = c3.generate({
                bindto: '#lineChart',
                data: {
                    columns: [
                        ['搜索引擎', 30, 200, 100, 400, 150, 250],
                        ['自主访问', 50, 120, 210, 140, 115, 425],
                        ['友情链接', 40, 150, 98, 300, 175, 100]
                    ]
                },
                color: {
                    pattern: ['#3CA2E0','#5CB85C','#F1B35B']
                },
                axis: {
                    x: {
                        show: false
                    },
                    y: {
                        show: false
                    },
                }
            });
        }, 275);
        setTimeout(function() {
            var chart2 = c3.generate({
                bindto: '#cbar',
                data: {
                    columns: [
                        [10,40,20,90,35,70,10,50,20,80,60,10,20,40,70]
                    ],
                    type:'bar'
                },
                bar: {
                    width: {
                        ratio: 0.5 // this makes bar width 50% of length between ticks
                    }
                },
                color: {
                    pattern: ['#DB5B57']
                },
                labels: true,
                legend: {
                    show: 0
                },
                axis: {
                    x: {
                        show: false
                    },
                    y: {
                        show: false
                    },
                }
            });

        }, 275);
        setTimeout(function() {
            var chart = c3.generate({
                bindto: '#pie',
                data: {
                    // iris data from R
                    columns: [
                        ['data1', 11],
                        ['data2', 23],
                        ['data3', 66]
                    ],
                    type : 'pie',
                },
                color: {
                    pattern: ['#5CB85C','#F0AD4E','#3CA2E0']
                },
                legend: {
                    show: 0
                },
            });

        }, 275);
    });
</script>

</body>
</html>