/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.controlseat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.controlseat.entity.ControlSeat;
import com.jeeplus.modules.controlseat.mapper.ControlSeatMapper;

/**
 * 集控室坐席表Service
 * @author 汤进国
 * @version 2019-01-14
 */
@Service
@Transactional(readOnly = true)
public class ControlSeatService extends CrudService<ControlSeatMapper, ControlSeat> {

	@Autowired
	ControlSeatMapper controlSeatMapper;

	public ControlSeat get(String id) {
		return super.get(id);
	}
	
	public List<ControlSeat> findList(ControlSeat controlSeat) {
		return super.findList(controlSeat);
	}
	
	public Page<ControlSeat> findPage(Page<ControlSeat> page, ControlSeat controlSeat) {
		return super.findPage(page, controlSeat);
	}
	
	@Transactional(readOnly = false)
	public void save(ControlSeat controlSeat) {
		super.save(controlSeat);
	}
	
	@Transactional(readOnly = false)
	public void delete(ControlSeat controlSeat) {
		super.delete(controlSeat);
	}

	//修改坐席状态
	@Transactional(readOnly = false)
	public void updateSeatState(ControlSeat controlSeat) {

		controlSeatMapper.updateSeatState(controlSeat);
	}

	//根据IP查询坐席编号
	public ControlSeat findInfoByIp(ControlSeat controlSeat) {

		return controlSeatMapper.findInfoByIp(controlSeat);
	}


}