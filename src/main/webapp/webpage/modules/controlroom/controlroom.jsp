<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>集控室管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>
    <script src="${ctxStatic}/plugin/bootstrapTree/bootstrap-treeview.js" type="text/javascript"></script>
    <link href="${ctxStatic}/plugin/bootstrapTree/bootstrap-treeview.css" rel="stylesheet" type="text/css"/>
    <%@include file="controlroom.js" %>
    <script src="${pageContext.request.contextPath}/webpage/include/deviceControl.js"></script>
    <script src="${pageContext.request.contextPath}/webpage/include/device.js"></script>
    <script src="${pageContext.request.contextPath}/webpage/include/highcharts.js"></script>
    <script src="${pageContext.request.contextPath}/webpage/include/exporting.js"></script>
    <script src="${pageContext.request.contextPath}/webpage/include/webVideoCtrl.js"></script>
    <script src="${pageContext.request.contextPath}/webpage/include/video.js"></script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">集控室管理</h3>
        </div>
        <div class="panel-body">
                <div class="col-sm-4 col-md-2" style="width: 15%;">
                    <div style="padding-bottom: 160px">
                        <form:form id="inputF" modelAttribute="controlroom" class="form-horizontal">
                            <table align="left">
                                <tr>
                                    <td style="font-size: 20px;padding-top: 10px">
                                        <div id="seatName"  style="font-size: 30px;color:red">${seatName}</div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="font-size: 20px;padding-top: 10px">坐席状态</td>
                                </tr>
                                <tr style="padding-top: 50px">
                                    <td style="padding-top: 10px" >
                                        <form:select path="status" id="seatState" onchange="updateSeatState()"  class="form-control m-b">
                                            <form:options items="${fns:getDictList('seat_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding-top: 40px">
                                        <input id ="dqpd" type="button" class="btn btn-warning" style="font-size: 18px;border-radius: 8px;width: 120px" onclick="queuelist()">
                                        <form:input path="queueNum" id="queueNum"/>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                    <div style="padding-top: 140px">
                            <input type="text" id = "weight" value="0"  style="font-size: 30px;color:red;border-radius: 8px;width: 120px">
                            </span><span style="font-size: 30px">kg</span>
                    </div>
                    <div style="padding-top: 40px">
                        <input type="text" id = "fxvehicleNo" disabled="true" style="font-size: 30px;color:red;border-radius: 8px;width: 160px">
                    </div>
                   </div>
                <div class="col-sm-8 col-md-10" >
                    <div class="b" >
                        <select class="btn btn-default" id="station" style="margin-top: 20px" onchange="sure()">
                            <option value="" label="请选择工作站"></option>
                            <c:forEach items="${list}" var="l">
                                <option value="${l.id}" label="${l.workStation}"></option>
                            </c:forEach>
                        </select>
                        </select>
                        <shiro:hasPermission name="controlroom:controlroom:dybd">
                        <button id="dybd" style="margin-top: 20px" class="btn btn-danger" onclick="dybd()">打印磅单</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:jkkqhw">
                        <button id="kqhw" style="margin-top: 20px" class="btn btn-success" onclick="common('003')">开启红外</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:jkgbhw">
                        <button id="gbhw" style="margin-top: 20px" class="btn btn-success" onclick="common('004')">关闭红外</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:openRed">
                            <button id="openRed" style="margin-top: 20px" class="btn btn-success" onclick="openRed()">开启红灯</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:openGreen">
                            <button id="openGreen" style="margin-top: 20px" class="btn btn-success" onclick="openGreen()">开启绿灯</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:ybql">
                            <button id="ybql" style="margin-top: 20px" class="btn btn-success" onclick="ybql()">仪表清零</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:spjz">
                        <button id="spjz" style="margin-top: 20px" class="btn btn-primary" onclick="spjz()">视频加载</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:sxjjym">
                        <button id="sxjjym" style="margin-top: 20px" class="btn btn-primary" onclick="common('001')">刷新检斤页面</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:fsyy">
                        <button id="fsyy" style="margin-top: 20px" class="btn btn-info" onclick="sendyy()">发送语音</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:fsled">
                        <button id="fsled" style="margin-top: 20px" class="btn btn-info" onclick="sendled()">发送LED</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:xgcph">
                        <button id="xgcph" style="margin-top: 20px" class="btn btn-info" onclick="common(document.getElementById('vehicleNo_txt').value)">修改车牌号</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="controlroom:controlroom:scanning">
                            <button id="scanning" style="margin-top: 20px" class="btn btn-info" onclick="common('002')">重新扫描二维码</button>
                        </shiro:hasPermission>
                    </div>
                    <hr>
                    <div  style="float: left;width:50%;height: 100%">
                    <table class="table table-bordered table-condensed"  style="width:100%;height:100%">
                        <tr >
                            <td style="font-size:16px;width: 15%">车号:</td>
                            <td style="font-size:16px;width: 35%">
                                <sys:autoComplete id="vehicleNo" name="vehicleNo" txtName="vehicleNo" sqlid="searchVehicleNoVarietys"  cssClass="form-control required" value="${vehicleNo}" labelValue="${vehicleNo}" />
                            </td>
                            <td style="font-size:16px;width: 15%">高炉号:</td>
                            <td style="font-size:16px;width: 35%">
                                <input readonly id="blastFurnaceNo" type="text" style="width:100%" >
                            </td>
                        </tr>
                        <tr>
                            <td style="font-size:16px;width: 15%">毛重:</td>
                            <td style="font-size:16px;width: 35%">
                                <input readonly id="matGrossWt" type="text" style="width:100%" >
                            </td>
                            <td style="font-size:16px;width: 15%">历史皮重:</td>
                            <td style="font-size:16px;width: 35%">
                                <input readonly id="oldImpWt" type="text" style="width:100%" >
                            </td>
                        </tr>
                        <tr>
                            <td style="font-size:16px;width: 15%">皮重:</td>
                            <td style="font-size:16px;width: 35%">
                                <input readonly id="impWt" type="text" style="width:100%" >
                            </td>
                            <td style="font-size:16px;width: 15%">收货单位:</td>
                            <td style="font-size:16px;width: 35%">
                                <input readonly id="consigneUser" type="text" style="width:100%" >
                            </td>
                        </tr>
                        <tr>
                            <td style="font-size:16px;width: 15%">净重:</td>
                            <td style="font-size:16px;width: 35%">
                                <input readonly id="matWt" type="text" style="width:100%" >
                            </td>
                            <td style="font-size:16px;width: 15%">物料名称:</td>
                            <td style="font-size:16px;width: 35%">
                                <input readonly id="prodCname" type="text" style="width:100%" >
                            </td>
                        </tr>
                        <tr>
                            <td style="font-size:16px;width: 15%">语音提示:</td>
                            <td style="font-size:16px;width: 35%">
                                <input id="yyts" type="text" style="width:100%" list="yylist">
                                <datalist id="yylist">
                                    <option>现场未确认收货，请返回现场                    </option>
                                    <option>你的车卸货时间已超过6小时，请返回现场签明原因  </option>
                                    <option>你的车无委托，请下磅                          </option>
                                    <option>请看墙上操作流程                              </option>
                                    <option>请把票据放在右下角指定处                      </option>
                                    <option>禁止倒车上下磅                                </option>
                                    <option>车上人员，请下磅                              </option>
                                    <option>点击物资名称后，再点击请求过磅                 </option>
                                    <option>你已超时，请下磅                              </option>
                                    <option>请重新过磅                                    </option>
                                    <option>称重已完成，立即下磅                           </option>
                                    <option>票据没签字，请返回现场                         </option>
                                </datalist>
                            </td>
                            <td  style="font-size:16px;width: 15%">LED提示:</td>
                            <td  style="font-size:16px;width: 35%">
                                <input id="ledts" type="text" style="width:100%" list="ledlist">
                                <datalist id="ledlist">
                                    <option>请核对车号及过磅信息</option>
                                </datalist>
                            </td>
                        </tr>
                        <tr style="height: 50%">
                            <td style="font-size:16px;width: 15%">票据截图:</td>
                            <td colspan="3">
                                <img id="picture1" src="" height="400px" onclick="jp.showPic(pic);"/>;
                            </td>
                        </tr>
                        <tr>
                        </tr>
                    </table>
                    </div>
                    <div id="video" style="float:right;width:48%;height:100%">
                        <!-- 视频  -->
                        <div id="divPlugin" class="plugin" ></div>
                    </div>
                </div>
            </div>
    </div>
</div>
<div>
    <tr>
        <td>
            <input id="info" type="hidden" onclick="sxjjym()">
            <input id="weighNo" type="hidden" value="" type="text" style="width:100%">
        </td>
    </tr>
</div>

</body>
</html>