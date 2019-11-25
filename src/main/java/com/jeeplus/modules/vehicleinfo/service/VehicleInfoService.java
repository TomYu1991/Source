/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.vehicleinfo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import com.jeeplus.modules.vehicleinfo.mapper.VehicleInfoMapper;

/**
 * 车辆信息表Service
 * @author 汤进国
 * @version 2019-01-17
 */
@Service
@Transactional(readOnly = true)
public class VehicleInfoService extends CrudService<VehicleInfoMapper, VehicleInfo> {

	@Autowired
	VehicleInfoMapper vehicleInfoMapper;

	public VehicleInfo get(String id) {
		return super.get(id);
	}
	
	public List<VehicleInfo> findList(VehicleInfo vehicleInfo) {
		return super.findList(vehicleInfo);
	}
	
	public Page<VehicleInfo> findPage(Page<VehicleInfo> page, VehicleInfo vehicleInfo) {
		if(vehicleInfo.getVehicleNo()!=null&&!"".equals(vehicleInfo.getVehicleNo())) {
			String v = "%"+vehicleInfo.getVehicleNo()+"%";
			vehicleInfo.setVehicleNo(v);
		}
		if(vehicleInfo.getGroupCode()!=null&&!"".equals(vehicleInfo.getGroupCode())) {
			String v = "%"+vehicleInfo.getGroupCode()+"%";
			vehicleInfo.setGroupCode(v);
		}
		if("".equals(page.getOrderBy())||page.getOrderBy()==null){
			page.setOrderBy("a.vehicle_no desc");
		}
		return super.findPage(page, vehicleInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(VehicleInfo vehicleInfo) {
		super.save(vehicleInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(VehicleInfo vehicleInfo) {
		super.delete(vehicleInfo);
	}

	//接口方法
	@Transactional(readOnly = false)
	public int insertVehicleInfo(VehicleInfo vehicleInfo) {

		return vehicleInfoMapper.insertVehicleInfo(vehicleInfo);
	}

	@Transactional(readOnly = false)
	public int deleteVehicleInfo(VehicleInfo vehicleInfo) {
		return vehicleInfoMapper.deleteVehicleInfo(vehicleInfo);
	}

	@Transactional(readOnly = false)
	public int cancelVehicleInfo(VehicleInfo vehicleInfo) {
		return vehicleInfoMapper.cancelVehicleInfo(vehicleInfo);
	}

	//根据车牌号查询车辆信息表车辆信息
	public List<VehicleInfo> checkByVehicleNo(String vehicleNo) {
		VehicleInfo vehicleInfo = new VehicleInfo();
		vehicleInfo.setVehicleNo(vehicleNo);
		return vehicleInfoMapper.checkByVehicleNo(vehicleInfo);
	}
	//查询

	public List<VehicleInfo> getVehicleNoByRfid(String rfidNo){
		VehicleInfo v= new VehicleInfo();
		v.setRfidNo(rfidNo);
		return vehicleInfoMapper.getVehicleNoByRfid(v);
	}

	public List<VehicleInfo> getVehicleNoBySrfid(String rfidNo){
		VehicleInfo v= new VehicleInfo();
		v.setSrfidNo(rfidNo);
		return vehicleInfoMapper.getVehicleNoBySrfid(v);
	}



	//查询该车辆是否在有效期之内
	public VehicleInfo queryValidity(String vehicleNo){
         return vehicleInfoMapper.queryValidity(vehicleNo);
	}
    //查询出厂车辆的放行码
    public VehicleInfo getVehiclePassCode(String vehicleNo) {
            return vehicleInfoMapper.getVehiclePassCode(vehicleNo);
    }

    @Transactional(readOnly = false)
    public void updaterfid(VehicleInfo vehicleInfo){
		vehicleInfoMapper.updaterfid(vehicleInfo);
	}

	//更新轨道衡RFID卡号
	@Transactional(readOnly = false)
	public void updaterRailfid(VehicleInfo vehicleInfo) {
		vehicleInfoMapper.updaterRailfid(vehicleInfo);
	}
}