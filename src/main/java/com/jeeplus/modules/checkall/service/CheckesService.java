/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.checkall.service;

import java.util.List;

import com.jeeplus.modules.checkall.entity.Checkes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.checkall.mapper.CheckesMapper;

/**
 * 点检表Service
 * @author 张鲁蒙
 * @version 2019-01-07
 */
@Service
@Transactional(readOnly = true)
public class CheckesService extends CrudService<CheckesMapper, Checkes> {

	@Autowired
	private CheckesMapper checkesMapper;

	public Checkes get(String id) {
		return super.get(id);
	}
	
	public List<Checkes> findList(Checkes checkes) {
		return super.findList(checkes);
	}
	
	public Page<Checkes> findPage(Page<Checkes> page, Checkes checkes) {
		return super.findPage(page, checkes);
	}
	
	@Transactional(readOnly = false)
	public void save(Checkes checkes) {
		super.save(checkes);
	}
	
	@Transactional(readOnly = false)
	public void delete(Checkes checkes) {
		super.delete(checkes);
	}

	public Checkes getEquipment(String equip) {
		Checkes check = checkesMapper.getEquipment(equip);
		return check;
	}
}