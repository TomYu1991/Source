/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.synchroinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.synchroinfo.entity.DataSynchroInfo;
import com.jeeplus.modules.synchroinfo.mapper.DataSynchroInfoMapper;

/**
 * 数据异常信息Service
 * @author 张鲁蒙
 * @version 2019-04-18
 */
@Service
@Transactional(readOnly = true)
public class DataSynchroInfoService extends CrudService<DataSynchroInfoMapper, DataSynchroInfo> {

	@Autowired
	private DataSynchroInfoMapper dataSynchroInfoMapper;

	public DataSynchroInfo get(String id) {
		return super.get(id);
	}
	
	public List<DataSynchroInfo> findList(DataSynchroInfo dataSynchroInfo) {
		return super.findList(dataSynchroInfo);
	}
	
	public Page<DataSynchroInfo> findPage(Page<DataSynchroInfo> page, DataSynchroInfo dataSynchroInfo) {
		if(dataSynchroInfo.getCode()!=null&&!"".equals(dataSynchroInfo.getCode())){
			String v = "%"+dataSynchroInfo.getCode()+"%";
			dataSynchroInfo.setCode(v);
		}
		if(dataSynchroInfo.getRemarks()!=null&&!"".equals(dataSynchroInfo.getRemarks())){
			String v = "%"+dataSynchroInfo.getRemarks()+"%";
			dataSynchroInfo.setRemarks(v);
		}
		if(dataSynchroInfo.getVehicleNo()!=null&&!"".equals(dataSynchroInfo.getVehicleNo())){
			String v = "%"+dataSynchroInfo.getVehicleNo()+"%";
			dataSynchroInfo.setVehicleNo(v);
		}
		return super.findPage(page, dataSynchroInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(DataSynchroInfo dataSynchroInfo) {
		super.save(dataSynchroInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(DataSynchroInfo dataSynchroInfo) {
		super.delete(dataSynchroInfo);
	}

	@Transactional(readOnly = false)
	public void saveRecord(DataSynchroInfo dataSynchroInfo) {
		dataSynchroInfoMapper.saveRecord(dataSynchroInfo);
	}
}