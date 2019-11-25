/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.passchecksub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.passchecksub.entity.PassCheckSub;
import com.jeeplus.modules.passchecksub.mapper.PassCheckSubMapper;

/**
 * 出门条明细Service
 * @author 汤进国
 * @version 2019-01-17
 */
@Service
@Transactional(readOnly = true)
public class PassCheckSubService extends CrudService<PassCheckSubMapper, PassCheckSub> {

	@Autowired
	PassCheckSubMapper passCheckSubMapper;

	public PassCheckSub get(String id) {
		return super.get(id);
	}
	
	public List<PassCheckSub> findList(PassCheckSub passCheckSub) {
		return super.findList(passCheckSub);
	}
	
	public Page<PassCheckSub> findPage(Page<PassCheckSub> page, PassCheckSub passCheckSub) {

		if("".equals(page.getOrderBy())||page.getOrderBy()==null){
			page.setOrderBy("a.prod_cname desc");
		}
		return super.findPage(page, passCheckSub);
	}
	
	@Transactional(readOnly = false)
	public void save(PassCheckSub passCheckSub) {
		super.save(passCheckSub);
	}
	
	@Transactional(readOnly = false)
	public void delete(PassCheckSub passCheckSub) {
		super.delete(passCheckSub);
	}


	public List findPassCheckSubList(String trnpAppNo){
		return passCheckSubMapper.findPassCheckSubList(trnpAppNo);
	}
	public List findCheckSubList(String trnpAppNo){
		return passCheckSubMapper.findCheckSubList(trnpAppNo);
	}

}