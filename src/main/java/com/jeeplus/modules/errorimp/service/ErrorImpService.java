/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.errorimp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.errorimp.entity.ErrorImp;
import com.jeeplus.modules.errorimp.mapper.ErrorImpMapper;

/**
 * 异常皮重信息Service
 * @author 汤进国
 * @version 2019-05-05
 */
@Service
@Transactional(readOnly = true)
public class ErrorImpService extends CrudService<ErrorImpMapper, ErrorImp> {

	public ErrorImp get(String id) {
		return super.get(id);
	}
	
	public List<ErrorImp> findList(ErrorImp errorImp) {
		return super.findList(errorImp);
	}
	
	public Page<ErrorImp> findPage(Page<ErrorImp> page, ErrorImp errorImp) {
		return super.findPage(page, errorImp);
	}
	
	@Transactional(readOnly = false)
	public void save(ErrorImp errorImp) {
		super.save(errorImp);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErrorImp errorImp) {
		super.delete(errorImp);
	}
	
}