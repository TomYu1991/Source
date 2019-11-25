/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.service;

import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.check.entity.DeviceCheckItem;
import com.jeeplus.modules.check.mapper.DeviceCheckItemMapper;
import com.jeeplus.modules.check.mapper.DeviceCheckRitemMapper;
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
public class DeviceCheckItemService extends CrudService<DeviceCheckItemMapper, DeviceCheckItem> {

	@Autowired
    DeviceCheckRitemMapper deviceCheckRitemMapper;

	public DeviceCheckItem get(String id) {
		return super.get(id);
	}
	
	public List<DeviceCheckItem> findList(DeviceCheckItem deviceCheckItem) {
		return super.findList(deviceCheckItem);
	}
//	public Page<DeviceCheckItem> findPage(Page<DeviceCheckItem> page, DeviceCheckItem DeviceCheckItem) {
//		if(DeviceCheckItem.getCardSerial()!=null&&!"".equals(DeviceCheckItem.getCardSerial())){
//			String v = "%"+DeviceCheckItem.getCardSerial()+"%";
//			DeviceCheckItem.setCardSerial(v);
//		}
//		if(DeviceCheckItem.getName()!=null&&!"".equals(DeviceCheckItem.getName())){
//			String v = "%"+DeviceCheckItem.getName()+"%";
//			DeviceCheckItem.setName(v);
//		}
//		if(DeviceCheckItem.getPersonnelId()!=null&&!"".equals(DeviceCheckItem.getPersonnelId())){
//			String v = "%"+DeviceCheckItem.getPersonnelId()+"%";
//			DeviceCheckItem.setPersonnelId(v);
//		}
//		return super.findPage(page, DeviceCheckItem);
//	}




}