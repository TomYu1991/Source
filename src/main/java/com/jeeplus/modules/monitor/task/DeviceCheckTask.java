package com.jeeplus.modules.monitor.task;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.check.entity.DeviceCheckRecord;
import com.jeeplus.modules.check.entity.DeviceCheckRitem;
import com.jeeplus.modules.check.service.DeviceCheckConfigService;
import com.jeeplus.modules.check.service.DeviceCheckRecordService;
import com.jeeplus.modules.check.service.DeviceCheckRitemService;
import com.jeeplus.modules.monitor.entity.Task;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.jeeplus.core.persistence.BaseEntity.IDTYPE_AUTO;

/**
 * 定时同步数据并获取人员信息
 */

@DisallowConcurrentExecution
@Component
@Lazy(false)
public class DeviceCheckTask extends Task {
	Logger logger = Logger.getLogger(DeviceCheckTask.class);
	@Override
	public void run() {

		System.out.println("自动生成设备点检记录的时间为："+new Date());
		DeviceCheckRecordService deviceCheckRecordService = SpringContextHolder.getBean(DeviceCheckRecordService.class);
		DeviceCheckRitemService deviceCheckRitemService = SpringContextHolder.getBean(DeviceCheckRitemService.class);

		DeviceCheckConfigService deviceCheckConfigService = SpringContextHolder.getBean(DeviceCheckConfigService.class);
		DeviceCheckRecord deviceCheckRecord = new DeviceCheckRecord();
		Map tempMap = new HashMap();
		tempMap.put("tasktime","");
		List<Map>  deviceCheckRecordList = deviceCheckRecordService.findTaskList(tempMap);
		Map deviceCheckMap = new HashMap();


		for(int i = 0; i < deviceCheckRecordList.size(); i++){

			Map deviceCheckConfigTmp = deviceCheckRecordList.get(i);
			String DeviceCheckConfigId = (String)deviceCheckConfigTmp.get("id");
			String deviceCheckRecordTmpId = IdGen.uuid();
			Object itemId = deviceCheckConfigTmp.get("itemid");
			Object checkCycle = deviceCheckConfigTmp.get("checkCycle");
			Object checkDate = deviceCheckConfigTmp.get("checkDate");
			System.out.println("-------------------->"+checkDate);
			boolean needCreate = false;
			Date today = new Date();
			Calendar c=Calendar.getInstance();
			c.setTime(today);
			if(checkCycle !=null && !checkCycle.equals("")){
				if(checkCycle.equals("day")){
					needCreate = true;
				}else if(checkCycle.equals("week")){

					int weekday=c.get(Calendar.DAY_OF_WEEK);
					if(weekday == 1){
						weekday = 7;
					}else{
						weekday--;
					}

					//weekday=1，当天是周日；weekday=2，当天是周一；...;weekday=7，当天是周六
					//checkDate=1，当天是周1；weekday=2，当天是周2；...;weekday=7，当天是周天
					if(checkDate != null && !checkDate.equals("") ){
						String dates = (String)checkDate;
						String[] days = dates.split(",");
						for(int j=0;j<days.length;j++){
							if(days[j].equals(String.valueOf(weekday))){
								needCreate = true;
							}
						}
					}
				}else if(checkCycle.equals("month")){
					int dayMonth=c.get(Calendar.DAY_OF_MONTH);
					if(checkDate != null && !checkDate.equals("")){
						String dates = (String)checkDate;
						String[] days = dates.split(",");
						for(int j=0;j<days.length;j++){
							if(days[j].equals(String.valueOf(dayMonth))){
								needCreate = true;
							}
						}
					}
				}
			}

			if(needCreate && itemId != null && !itemId.equals("")){
				if(deviceCheckMap.get(DeviceCheckConfigId) != null){
					deviceCheckRecordTmpId = (String)deviceCheckMap.get(DeviceCheckConfigId);
					//已保存
				}else{
					deviceCheckMap.put(DeviceCheckConfigId,deviceCheckRecordTmpId);
					//saveRecord
					DeviceCheckRecord deviceCheckRecordTmp  = new DeviceCheckRecord();
					deviceCheckRecordTmp.setId(deviceCheckRecordTmpId);
					deviceCheckRecordTmp.setConfigId(DeviceCheckConfigId);
					deviceCheckRecordTmp.setWorkingGroup((String)deviceCheckConfigTmp.get("workingGroup"));
					deviceCheckRecordTmp.setWorkingArea((String)deviceCheckConfigTmp.get("workingArea"));
					deviceCheckRecordTmp.setGroupLeader((String)deviceCheckConfigTmp.get("groupLeader"));
					deviceCheckRecordTmp.setGoupWorker((String)deviceCheckConfigTmp.get("goupWorker"));
					deviceCheckRecordTmp.setCheckCycle((String)deviceCheckConfigTmp.get("checkCycle"));
					deviceCheckRecordTmp.setCheckDate((String)deviceCheckConfigTmp.get("checkDate"));
					deviceCheckRecordTmp.setRemarks((String)deviceCheckConfigTmp.get("remarks"));
					deviceCheckRecordTmp.setIdType(IDTYPE_AUTO);
					deviceCheckRecordService.inster(deviceCheckRecordTmp);
				}


				DeviceCheckRitem deviceCheckRitemTmp  = new DeviceCheckRitem();
				deviceCheckRitemTmp.setId(IdGen.uuid());
				deviceCheckRitemTmp.setRecordId(deviceCheckRecordTmpId);
				deviceCheckRitemTmp.setDeviceName((String)deviceCheckConfigTmp.get("deviceName"));
				deviceCheckRitemTmp.setCheckItem((String)deviceCheckConfigTmp.get("checkItem"));
				deviceCheckRitemTmp.setCheckContent((String)deviceCheckConfigTmp.get("checkContent"));
				deviceCheckRitemTmp.setCheckReference((String)deviceCheckConfigTmp.get("checkReference"));
				deviceCheckRitemTmp.setCheckMethod((String)deviceCheckConfigTmp.get("checkMethod"));
				deviceCheckRitemTmp.setCheckResult((String)deviceCheckConfigTmp.get("checkResult"));
				deviceCheckRitemTmp.setDeviceState((String)deviceCheckConfigTmp.get("deviceState"));
				deviceCheckRitemTmp.setIdType(IDTYPE_AUTO);
				deviceCheckRitemService.inster(deviceCheckRitemTmp);
			}


		}



		System.out.println("更新成功！"+new Date());
	}

}

