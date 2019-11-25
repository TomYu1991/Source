/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.vehicleaccessrecord.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.mapper.VehicleAccessRecordMapper;

/**
 * 车辆进出记录Service
 * @author 汤进国
 * @version 2019-01-18
 */
@Service
@Transactional(readOnly = true)
public class VehicleAccessRecordService extends CrudService<VehicleAccessRecordMapper, VehicleAccessRecord> {

	@Autowired
	VehicleAccessRecordMapper vehicleAccessRecordMapper;

	public VehicleAccessRecord get(String id) {
		return super.get(id);
	}
	
	public List<VehicleAccessRecord> findList(VehicleAccessRecord vehicleAccessRecord) {
		return super.findList(vehicleAccessRecord);
	}
	
	public Page<VehicleAccessRecord> findPage(Page<VehicleAccessRecord> page, VehicleAccessRecord vehicleAccessRecord) {

		if(vehicleAccessRecord.getVehicleNo()!=null&&!"".equals(vehicleAccessRecord.getVehicleNo())) {
			String v = "%"+vehicleAccessRecord.getVehicleNo()+"%";
			vehicleAccessRecord.setVehicleNo(v);
		}
		if("".equals(page.getOrderBy())||page.getOrderBy()==null){
			page.setOrderBy("a.create_date desc");
		}
		return super.findPage(page, vehicleAccessRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(VehicleAccessRecord vehicleAccessRecord) {
		super.save(vehicleAccessRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(VehicleAccessRecord vehicleAccessRecord) {
		super.delete(vehicleAccessRecord);
	}

	//添加车辆出厂时间
	@Transactional(readOnly = false)
	public void updateOutTime(VehicleAccessRecord vehicleAccessRecord) {

		vehicleAccessRecordMapper.updateOutTime(vehicleAccessRecord);
	}
	//查询当前车辆最近一条记录
	public VehicleAccessRecord queryLatelyRecord(VehicleAccessRecord vehicleAccessRecord) {
		return vehicleAccessRecordMapper.queryLatelyRecord(vehicleAccessRecord);
	}

	//查询当前排队车辆数量
	public VehicleAccessRecord queryVehicleNum() {

		return vehicleAccessRecordMapper.queryVehicleNum();
	}

	//修改车辆状态
	@Transactional(readOnly = false)
	public void updateState(VehicleAccessRecord vehicleAccessRecord) {

		vehicleAccessRecordMapper.updateState(vehicleAccessRecord);
	}
	//保存车辆违章信息
	@Transactional(readOnly = false)
	public void savePeccancyInfo(VehicleAccessRecord vehicleAccessRecord) {

		vehicleAccessRecordMapper.savePeccancyInfo(vehicleAccessRecord);
	}

	public List<VehicleAccessRecord> queryRecord(VehicleAccessRecord v){
		return vehicleAccessRecordMapper.queryRecord(v);
	}
	@Transactional(readOnly = false)
	public void updatePic(VehicleAccessRecord v){
		vehicleAccessRecordMapper.updatePic(v);
	}

	@Transactional(readOnly = false)
	public void updateOpenInfo(VehicleAccessRecord v){
		vehicleAccessRecordMapper.updateOpenInfo(v);
	}

	//进门车辆
	public int getVehicleInCount(VehicleAccessRecord v){
		return vehicleAccessRecordMapper.getInCount(v);
	}
	//出门车辆
	public int getVehicleOutCount(VehicleAccessRecord v){
		return vehicleAccessRecordMapper.getOutCount(v);
	}
	//预约车辆
	public int getConsignVehicle(VehicleAccessRecord r){
		return vehicleAccessRecordMapper.getConsignVehicleCount(r);
	}
	//预约入厂车辆
	public List<VehicleAccessRecord> getIntoFactoryVehicleList(VehicleAccessRecord record){
		return vehicleAccessRecordMapper.getIntoFactoryVehicle(record);
	}
	//进门手动放行
	public List<VehicleAccessRecord> getVehicleManual(VehicleAccessRecord record){
	    return vehicleAccessRecordMapper.getVehicleManualList(record);
    }
}