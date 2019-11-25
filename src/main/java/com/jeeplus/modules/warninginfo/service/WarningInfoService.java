/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warninginfo.service;

import java.util.List;

import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
import com.jeeplus.modules.warninginfo.mapper.WarningInfoMapper;

/**
 * 报警信息Service
 * @author 张鲁蒙
 * @version 2019-03-05
 */
@Service
@Transactional(readOnly = true)
public class WarningInfoService extends CrudService<WarningInfoMapper, WarningInfo> {

	@Autowired
	private WarningInfoMapper warningInfoMapper;

	public WarningInfo get(String id) {
		return super.get(id);
	}
	
	public List<WarningInfo> findList(WarningInfo warningInfo) {
		return super.findList(warningInfo);
	}
	
	public Page<WarningInfo> findPage(Page<WarningInfo> page, WarningInfo warningInfo) {
		if(warningInfo.getVehicleNo()!=null&&!"".equals(warningInfo.getVehicleNo())) {
			String v = "%"+warningInfo.getVehicleNo()+"%";
			warningInfo.setVehicleNo(v);
		}
		if("".equals(page.getOrderBy())||page.getOrderBy()==null){
			page.setOrderBy("a.create_date desc");
		}
		return super.findPage(page, warningInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WarningInfo warningInfo) {
		super.save(warningInfo);
	}

	@Transactional(readOnly = false)
	public void insertInter(WarningInfo warningInfo) {
		warningInfoMapper.insertInter(warningInfo);
		InterfaceTest ift = new InterfaceTest();
		ift.InterWarn(warningInfo);
	}

	@Transactional(readOnly = false)
	public void delete(WarningInfo warningInfo) {
		super.delete(warningInfo);
	}

	public  List<WarningInfo> findInfoByVehicleNo(String vehicleNo){
		WarningInfo warningInfo = new WarningInfo();
		warningInfo.setVehicleNo(vehicleNo);
		return  warningInfoMapper.findInfoByVehicleNo(warningInfo);
	}
	//保存接口返回
	@Transactional(readOnly = false)
	public void updateDateType(WarningInfo w){
		warningInfoMapper.updateDateType(w);
	};
	//已批过夜车辆
	public List<WarningInfo> getApproveVehicle(WarningInfo warningInfo){
		return warningInfoMapper.getApproveVehicleList(warningInfo);
	}
	//未批过夜车辆
	public List<WarningInfo> getUnapproveVehicle(WarningInfo warningInfo){
		return warningInfoMapper.getUnapproveVehicleList(warningInfo);
	}
	//违章车辆
	public List<WarningInfo> getWarningInfoVehicles(WarningInfo warningInfo){
		return warningInfoMapper.getWarningInfoVehicleList(warningInfo);
	}
}