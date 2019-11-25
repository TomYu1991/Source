/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightmonitor.service;


import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.mapper.ConsignMapper;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.controlqueue.entity.ControlQueue;
import com.jeeplus.modules.controlqueue.mapper.ControlQueueMapper;
import com.jeeplus.modules.controlqueue.service.ControlQueueService;
import com.jeeplus.modules.gatelog.entity.GateLog;
import com.jeeplus.modules.print.entity.Print;
import com.jeeplus.modules.station.entity.StationDevice;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.mapper.VehicleAccessRecordMapper;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.mapper.WeightMapper;
import com.jeeplus.modules.weightmonitor.entity.WeightMonitor;
import com.jeeplus.modules.weightmonitor.mapper.WeightMonitorMapper;
import com.jeeplus.modules.weightrecord.entity.InitWeight;
import com.jeeplus.modules.weightrecord.entity.PrintRecord;
import com.jeeplus.modules.weightrecord.entity.UpdateWeightRecord;
import com.jeeplus.modules.weightrecord.entity.WeightRecord;
import com.jeeplus.modules.weightrecord.mapper.InitWeightMapper;
import com.jeeplus.modules.weightrecord.mapper.PrintRecordMapper;
import com.jeeplus.modules.weightrecord.mapper.UpdateWeightRecordMapper;
import com.jeeplus.modules.weightrecord.mapper.WeightRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 磅单管理Service
 * @author jeeplus
 * @version 2018-12-25
 */
@Service
@Transactional(readOnly = true)
public class WeightMonitorService extends CrudService<WeightMonitorMapper,WeightMonitor> {
	@Autowired
	private WeightMonitorMapper weightMonitorMapper;

	public List<WeightMonitor> getList(WeightMonitor monitor){
		List<WeightMonitor> list = weightMonitorMapper.findListByStationAndTime(monitor);
//		long startTime = monitor.getBegintaretime().getTime();
//		long endTime = monitor.getEndtaretime().getTime();
//		long[] result=range(startTime,endTime,1000);
//		List<WeightMonitor> newList = new ArrayList<>();
//		for(int i=0;i<result.length;i++){
//			for (WeightMonitor m:list
//				 ) {
//				if(m.getUpdateDate().getTime()!=result[i]){
//					WeightMonitor weightMonitor=new WeightMonitor();
//					weightMonitor.setUpdateDate(new Date(result[i]));
//					weightMonitor.setWeightValue(0.0);
//					newList.add(weightMonitor);
//				}
//			}
//		}
		return list;
	}

	private long[] range(long start,long end,int step){
		int sz = (int) ((end-start)/step+1);
		long[] result = new long[sz];
		for(int i=0;i<sz;i++){
			result[i]=start+(i*step);
		}
		return result;
	}
}