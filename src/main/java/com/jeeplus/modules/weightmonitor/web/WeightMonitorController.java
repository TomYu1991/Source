/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightmonitor.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.vehicleinfo.service.VehicleInfoService;
import com.jeeplus.modules.weightmonitor.entity.WeightMonitor;
import com.jeeplus.modules.weightmonitor.service.WeightMonitorService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 过磅重量监控Controller
 *
 * @author jeeplus
 * @version 2018-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/weightmonitor/weightmonitor")
public class WeightMonitorController extends BaseController {

    @Autowired
    private WeightMonitorService weightMonitorService;
    //工作站
    @Autowired
    private WorkStationService workStationService;
    //车辆信息
    @Autowired
    private VehicleInfoService vehicleInfoService;

    /**
     * 过磅重量监控页面
     */
    @RequiresPermissions("weightmonitor:weightmonitor:list")
    @RequestMapping(value = {"list", ""})
    public String list(WeightMonitor monitor, Model model) {
        model.addAttribute("monitor", monitor);
        return "modules/weight/weightMonitor";
    }


    //保存过磅重量
    @ResponseBody
    @RequestMapping(value = "saveWeightData")
    public AjaxJson saveWeightData(HttpServletRequest request, WeightMonitor monitor) {
        AjaxJson json = new AjaxJson();
        String ip = request.getRemoteAddr();
        WorkStation ws = workStationService.queryByStationIp(ip);
        if (ws != null) {
            String workStation = ws.getStationIp();
            monitor.setWorkStation(workStation);
            weightMonitorService.inster(monitor);
            json.setSuccess(true);
            json.setMsg("数据插入成功！");
        } else {
            json.setSuccess(false);
            json.setMsg("数据插入失败！");
        }
        return json;
    }

    //查询过磅重量
    @ResponseBody
    @RequestMapping(value = "queryWeightData")
    public AjaxJson queryWeightData(WeightMonitor monitor) {
        AjaxJson json = new AjaxJson();
        Date current = new Date();
        if (monitor.getEndtaretime() == null) {
            monitor.setEndtaretime(current);
        }
        List<WeightMonitor> list = weightMonitorService.getList(monitor);
        List respList = new ArrayList();
        if (list != null && list.size() != 0) {
            for (WeightMonitor m : list) {
                Map map = new HashMap();
                if (m.getVehicleNo() == null) {
                    map.put("name", "未识别到车牌号");
                } else {
                    map.put("name", m.getVehicleNo());
                }
                List tempList = new ArrayList();
                tempList.add(m.getUpdateDate());
                tempList.add(m.getWeightValue());
                map.put("value", tempList);
                respList.add(map);
            }
            json.setSuccess(true);
            json.setData(respList);
        } else {
            json.setSuccess(false);
            json.setMsg("暂时没有过磅数据");
        }
        return json;
    }
}
