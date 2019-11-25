/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.service;

import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.check.entity.DeviceCheckConfig;
import com.jeeplus.modules.check.mapper.DeviceCheckConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author
 * @version 2019-03-16
 */
@Service
@Transactional(readOnly = true)
public class DeviceCheckConfigService extends CrudService<DeviceCheckConfigMapper, DeviceCheckConfig> {

	@Autowired
    DeviceCheckConfigMapper deviceCheckConfigMapper;

	public DeviceCheckConfig get(String id) {
		return super.get(id);
	}
	
	public List<DeviceCheckConfig> findList(DeviceCheckConfig deviceCheckConfig) {
		return super.findList(deviceCheckConfig);
	}

	public DeviceCheckConfig findConfigById(String id){
		return deviceCheckConfigMapper.findConfigById(id);
	}
//	public Page<DeviceCheckConfig> findPage(Page<DeviceCheckConfig> page, DeviceCheckConfig DeviceCheckConfig) {
//		if(DeviceCheckConfig.getCardSerial()!=null&&!"".equals(DeviceCheckConfig.getCardSerial())){
//			String v = "%"+DeviceCheckConfig.getCardSerial()+"%";
//			DeviceCheckConfig.setCardSerial(v);
//		}
//		if(DeviceCheckConfig.getName()!=null&&!"".equals(DeviceCheckConfig.getName())){
//			String v = "%"+DeviceCheckConfig.getName()+"%";
//			DeviceCheckConfig.setName(v);
//		}
//		if(DeviceCheckConfig.getPersonnelId()!=null&&!"".equals(DeviceCheckConfig.getPersonnelId())){
//			String v = "%"+DeviceCheckConfig.getPersonnelId()+"%";
//			DeviceCheckConfig.setPersonnelId(v);
//		}
//		return super.findPage(page, DeviceCheckConfig);
//	}





}