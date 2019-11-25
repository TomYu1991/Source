package com.jeeplus.modules.monitor.task;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.check.entity.DeviceCheckRecord;
import com.jeeplus.modules.check.entity.DeviceCheckRitem;
import com.jeeplus.modules.check.service.DeviceCheckConfigService;
import com.jeeplus.modules.check.service.DeviceCheckRecordService;
import com.jeeplus.modules.check.service.DeviceCheckRitemService;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.consign_tmp.entity.ConsignTmp;
import com.jeeplus.modules.consign_tmp.service.ConsignTmpService;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.userpasscord.service.UserPassRecordService;
import com.jeeplus.modules.userpasscord.web.UserPassRecordController;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
import com.jeeplus.modules.warninginfo.service.WarningInfoService;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.service.WeightService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.*;

import static com.jeeplus.core.persistence.BaseEntity.IDTYPE_AUTO;
import static com.jeeplus.modules.interfaceTest.Controller.InterfaceTest.sendPost;

@Component
@Lazy(false)
public class Taskjob1 {

    @Scheduled(cron = "0 0 3 * * ?")
    public void task1() {
        System.out.println("更新同步数据的时间为：" + new Date());
        WeightService weightService = SpringContextHolder.getBean(WeightService.class);
        UserPassRecordService userPassRecordService = SpringContextHolder.getBean(UserPassRecordService.class);

        //定时更新未同步的数据
        List<Weight> we = weightService.notSynchronized();
        if (we.size() > 0) {
            for (Weight wt : we) {
                Weight w = new Weight();
                w.setWeighNo(wt.getWeighNo());
                Weight ws = weightService.queryInfoByWeighNo(w);
                InterfaceTest test = new InterfaceTest();
                test.queryInterFaceList(ws, null);
            }
        }
        userPassRecordService.deleteUserPassInfo();
        //定时更新员工信息
        UserPassRecordController uprc = new UserPassRecordController();
        uprc.saveUser();
        System.out.println("更新成功！" + new Date());
    }

    @Scheduled(cron = "0 0 19 * * ?")
    public void task2() {
        ConsignService consignService = SpringContextHolder.getBean(ConsignService.class);
        WarningInfoService warningInfoService = SpringContextHolder.getBean(WarningInfoService.class);

        List<Consign> cl = consignService.queryMaxEndTimeConsign();
        if (cl.size() > 0) {
            for (Consign consign : cl) {
                WarningInfo war = new WarningInfo();
                war.setVehicleNo(consign.getVehicleNo());
                war.setReason("过夜车辆");
                war.setState("1");
                war.setCreateDate(new Date());
                User u = new User();
                u.setLoginName("自动标记");
                war.setCreateBy(u);
                war.setDataType("0");
                war.setDelFlag("0");
                war.setId(IdGen.uuid());
                warningInfoService.insertInter(war);

            }
        }
        System.out.println("标记过夜车辆成功");
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void task3() {
        //定时更新员工信息
        UserPassRecordController uprc = new UserPassRecordController();
        uprc.saveUser();
        System.out.println("更新员工信息成功！" + new Date());
    }

    @Scheduled(cron = "0 0 15 * * ?")
    public void task4() {
        //定时更新员工信息
        UserPassRecordController uprc = new UserPassRecordController();
        uprc.saveUser();
        System.out.println("更新员工信息成功！" + new Date());
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void task5() {
        System.out.println("自动生成设备点检记录的时间为：" + new Date());
        DeviceCheckRecordService deviceCheckRecordService = SpringContextHolder.getBean(DeviceCheckRecordService.class);
        DeviceCheckRitemService deviceCheckRitemService = SpringContextHolder.getBean(DeviceCheckRitemService.class);

        DeviceCheckConfigService deviceCheckConfigService = SpringContextHolder.getBean(DeviceCheckConfigService.class);
        DeviceCheckRecord deviceCheckRecord = new DeviceCheckRecord();
        Map tempMap = new HashMap();
        tempMap.put("tasktime", "");
        List<Map> deviceCheckRecordList = deviceCheckRecordService.findTaskList(tempMap);
        Map deviceCheckMap = new HashMap();

        for (int i = 0; i < deviceCheckRecordList.size(); i++) {

            Map deviceCheckConfigTmp = deviceCheckRecordList.get(i);
            String DeviceCheckConfigId = (String) deviceCheckConfigTmp.get("id");
            String deviceCheckRecordTmpId = IdGen.uuid();
            Object itemId = deviceCheckConfigTmp.get("itemid");
            Object checkCycle = deviceCheckConfigTmp.get("checkCycle");
            Object checkDate = deviceCheckConfigTmp.get("checkDate");
            System.out.println("-------------------->" + checkDate);
            boolean needCreate = false;
            Date today = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            if (checkCycle != null && !checkCycle.equals("")) {
                if (checkCycle.equals("day")) {
                    needCreate = true;
                } else if (checkCycle.equals("week")) {

                    int weekday = c.get(Calendar.DAY_OF_WEEK);
                    if (weekday == 1) {
                        weekday = 7;
                    } else {
                        weekday--;
                    }

                    //weekday=1，当天是周日；weekday=2，当天是周一；...;weekday=7，当天是周六
                    //checkDate=1，当天是周1；weekday=2，当天是周2；...;weekday=7，当天是周天
                    if (checkDate != null && !checkDate.equals("")) {
                        String dates = (String) checkDate;
                        String[] days = dates.split(",");
                        for (int j = 0; j < days.length; j++) {
                            if (days[j].equals(String.valueOf(weekday))) {
                                needCreate = true;
                            }
                        }
                    }
                } else if (checkCycle.equals("month")) {
                    int dayMonth = c.get(Calendar.DAY_OF_MONTH);
                    if (checkDate != null && !checkDate.equals("")) {
                        String dates = (String) checkDate;
                        String[] days = dates.split(",");
                        for (int j = 0; j < days.length; j++) {
                            if (days[j].equals(String.valueOf(dayMonth))) {
                                needCreate = true;
                            }
                        }
                    }
                }
            }

            if (needCreate && itemId != null && !itemId.equals("")) {
                if (deviceCheckMap.get(DeviceCheckConfigId) != null) {
                    deviceCheckRecordTmpId = (String) deviceCheckMap.get(DeviceCheckConfigId);
                    //已保存
                } else {
                    deviceCheckMap.put(DeviceCheckConfigId, deviceCheckRecordTmpId);
                    //saveRecord
                    DeviceCheckRecord deviceCheckRecordTmp = new DeviceCheckRecord();
                    deviceCheckRecordTmp.setId(deviceCheckRecordTmpId);
                    deviceCheckRecordTmp.setConfigId(DeviceCheckConfigId);
                    deviceCheckRecordTmp.setWorkingGroup((String) deviceCheckConfigTmp.get("workingGroup"));
                    deviceCheckRecordTmp.setWorkingArea((String) deviceCheckConfigTmp.get("workingArea"));
                    deviceCheckRecordTmp.setGroupLeader((String) deviceCheckConfigTmp.get("groupLeader"));
                    deviceCheckRecordTmp.setGoupWorker((String) deviceCheckConfigTmp.get("goupWorker"));
                    deviceCheckRecordTmp.setCheckCycle((String) deviceCheckConfigTmp.get("checkCycle"));
                    deviceCheckRecordTmp.setCheckDate((String) deviceCheckConfigTmp.get("checkDate"));
                    deviceCheckRecordTmp.setRemarks((String) deviceCheckConfigTmp.get("remarks"));
                    deviceCheckRecordTmp.setIdType(IDTYPE_AUTO);
                    deviceCheckRecordService.inster(deviceCheckRecordTmp);
                }

                DeviceCheckRitem deviceCheckRitemTmp = new DeviceCheckRitem();
                deviceCheckRitemTmp.setId(IdGen.uuid());
                deviceCheckRitemTmp.setRecordId(deviceCheckRecordTmpId);
                deviceCheckRitemTmp.setDeviceName((String) deviceCheckConfigTmp.get("deviceName"));
                deviceCheckRitemTmp.setCheckItem((String) deviceCheckConfigTmp.get("checkItem"));
                deviceCheckRitemTmp.setCheckContent((String) deviceCheckConfigTmp.get("checkContent"));
                deviceCheckRitemTmp.setCheckReference((String) deviceCheckConfigTmp.get("checkReference"));
                deviceCheckRitemTmp.setCheckMethod((String) deviceCheckConfigTmp.get("checkMethod"));
                deviceCheckRitemTmp.setCheckResult((String) deviceCheckConfigTmp.get("checkResult"));
                deviceCheckRitemTmp.setDeviceState((String) deviceCheckConfigTmp.get("deviceState"));
                deviceCheckRitemTmp.setIdType(IDTYPE_AUTO);
                deviceCheckRitemService.inster(deviceCheckRitemTmp);
            }
        }
        System.out.println("更新成功！" + new Date());
    }



}
