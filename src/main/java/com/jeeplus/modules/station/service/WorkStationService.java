/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.station.service;

import java.util.List;

import com.jeeplus.modules.devicemanage.entity.DeviceManage;
import com.jeeplus.modules.devicemanage.service.DeviceManageService;
import com.jeeplus.modules.swipecard.entity.SwipeCard;
import com.jeeplus.modules.swipecard.service.SwipeCardService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.mapper.WorkStationMapper;
import com.jeeplus.modules.station.entity.StationDevice;
import com.jeeplus.modules.station.mapper.StationDeviceMapper;
import com.jeeplus.modules.station.entity.StationWorker;
import com.jeeplus.modules.station.mapper.StationWorkerMapper;

/**
 * 工作站管理Service
 * @author 汤进国
 * @version 2019-01-02
 */
@Service
@Transactional(readOnly = true)
public class WorkStationService extends CrudService<WorkStationMapper, WorkStation> {

	@Autowired
	private StationDeviceMapper stationDeviceMapper;
	@Autowired
	private StationWorkerMapper stationWorkerMapper;
	@Autowired
	private WorkStationMapper workStationMapper;
	@Autowired
	private DeviceManageService deviceManageService;
    @Autowired
	private SwipeCardService swipeCardService;

	public WorkStation get(String id) {
		WorkStation workStation = super.get(id);
		workStation.setStationWorkerList(stationWorkerMapper.findList(new StationWorker(workStation)));
		workStation.setStationDeviceList(stationDeviceMapper.findList(new StationDevice(workStation)));
		return workStation;
	}
	
	public List<WorkStation> findList(WorkStation workStation) {
		return super.findList(workStation);
	}
	
	public Page<WorkStation> findPage(Page<WorkStation> page, WorkStation workStation) {
		return super.findPage(page, workStation);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkStation workStation) {
		super.save(workStation);
		for (StationDevice stationDevice : workStation.getStationDeviceList()){
			if (stationDevice.getId() == null){
				continue;
			}
			if (StationDevice.DEL_FLAG_NORMAL.equals(stationDevice.getDelFlag())){
				if (StringUtils.isBlank(stationDevice.getId())){
					stationDevice.setStation(workStation);
					stationDevice.preInsert();
					stationDeviceMapper.insert(stationDevice);
					DeviceManage device = new DeviceManage();
					device.setDeviceName(stationDevice.getDeviceName());
					device.setDeviceDeviceNo(stationDevice.getDeviceNum());
					device.setDeviceFlag("0");
					device.setDeviceType(stationDevice.getDeviceType());
					device.setStation(stationDevice.getStation());
					deviceManageService.save(device);
				}else{
					stationDevice.preUpdate();
					stationDeviceMapper.update(stationDevice);
					DeviceManage device = new DeviceManage();
					device.setDeviceName(stationDevice.getDeviceName());
					device.setDeviceDeviceNo(stationDevice.getDeviceNum());
					device.setDeviceType(stationDevice.getDeviceType());
					device.setStation(stationDevice.getStation());
					deviceManageService.update(device);
				}
			}else{
				stationDeviceMapper.delete(stationDevice);
			}
		}
		for (StationWorker stationWorker : workStation.getStationWorkerList()){
			if (stationWorker.getId() == null){
				continue;
			}
			if (StationWorker.DEL_FLAG_NORMAL.equals(stationWorker.getDelFlag())){
				if (StringUtils.isBlank(stationWorker.getId())){
					stationWorker.setStation(workStation);
					stationWorker.preInsert();
					stationWorkerMapper.insert(stationWorker);

					SwipeCard sc = new SwipeCard();
					sc.setUser(stationWorker.getUser());
					sc.setStation(stationWorker.getStation());
					sc.setWorkerId(stationWorker.getId());
					sc.preInsert();
					swipeCardService.inster(sc);
				}else{
					stationWorker.preUpdate();
					stationWorkerMapper.update(stationWorker);

                    SwipeCard sc = new SwipeCard();
                    sc.setUser(stationWorker.getUser());
                    sc.setStation(stationWorker.getStation());
                    sc.setWorkerId(stationWorker.getId());
                    swipeCardService.update(sc);
				}
			}else{
				stationWorkerMapper.delete(stationWorker);
				SwipeCard sc = new SwipeCard();
				sc.setWorkerId(stationWorker.getId());
				swipeCardService.delete(sc);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(WorkStation workStation) {
		super.delete(workStation);
		stationDeviceMapper.delete(new StationDevice(workStation));
		stationWorkerMapper.delete(new StationWorker(workStation));

		String w = workStation.getId();
		swipeCardService.deleteOne(w);
	}


	//根据ip查询工作站名称
	public WorkStation queryWorkNameByStationIp(String stationIp){
		WorkStation workStation = new WorkStation();
		workStation.setStationIp(stationIp);
		return workStationMapper.queryWorkNameByStationIp(workStation);
	}
	//根据ip查询工作站
	public WorkStation queryByStationIp(String stationIp){
		WorkStation workStation = new WorkStation();
		workStation.setStationIp(stationIp);
		return workStationMapper.queryByStationIp(workStation);
	}
	public List<WorkStation> queryDeviceNoSBCH(){
      return workStationMapper.queryDeviceNoSBCH();
	}

	public List<WorkStation> queryDeviceNoRFID(){
	  return workStationMapper.queryDeviceNoRFID();
	}

	public WorkStation queryByDeviceNo(String deviceNo){
		return workStationMapper.queryByDeviceNo(deviceNo);
	}

    public List<WorkStation> queryByStationName(String name){
        WorkStation wk =new WorkStation();
		if(name!=null&&!"".equals(name)){
			String v = "%"+name+"%";
			wk.setWorkStation(v);
		}
	    return workStationMapper.queryByStationName(wk);
    }

}