/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.checkequipment.service;

import java.util.List;

import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.weight.mapper.WeightMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.checkequipment.entity.Equipment;
import com.jeeplus.modules.checkequipment.mapper.EquipmentMapper;

/**
 * 点检设备表Service
 * @author 张鲁蒙
 * @version 2019-01-04
 */
@Service
@Transactional(readOnly = true)
public class EquipmentService extends CrudService<EquipmentMapper, Equipment> {
    @Autowired
	private EquipmentMapper equipmentMapper;

	public Equipment get(String id) {
		return super.get(id);
	}
	
	public List<Equipment> findList(Equipment equipment) {
		return super.findList(equipment);
	}
	
	public Page<Equipment> findPage(Page<Equipment> page, Equipment equipment) {
		return super.findPage(page, equipment);
	}
	
	@Transactional(readOnly = false)
	public void save(Equipment equipment) {
		super.save(equipment);
	}
	
	@Transactional(readOnly = false)
	public void delete(Equipment equipment) {
		super.delete(equipment);
	}

	public List findEquipmentUnique(String equipment,String station){
		return equipmentMapper.findEquipmentUnique(equipment,station);
	}
}