/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.print.service;

import java.util.List;

import com.jeeplus.modules.weight.entity.Weight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.print.entity.Print;
import com.jeeplus.modules.print.mapper.PrintMapper;

/**
 * 打印Service
 * @author 打印
 * @version 2019-03-20
 */
@Service
@Transactional(readOnly = true)
public class PrintService extends CrudService<PrintMapper, Print> {
    @Autowired
	private PrintMapper printMapper;

	public Print get(String id) {
		return super.get(id);
	}
	
	public List<Print> findList(Print print) {
		return super.findList(print);
	}
	
	public Page<Print> findPage(Page<Print> page, Print print) {
		return super.findPage(page, print);
	}
	
	@Transactional(readOnly = false)
	public void save(Print print) {
		super.save(print);
	}
	
	@Transactional(readOnly = false)
	public void delete(Print print) {
		super.delete(print);
	}

	public List<Print> querybill(){
		return printMapper.querybill();
	}

	public List<Print> queryweight(){
		return printMapper.querybill();
	}
}