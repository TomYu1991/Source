/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.passcheck.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.passcheck.mapper.PassCheckMapper;

/**
 * 出门条信息Service
 * @author 汤进国
 * @version 2019-01-17
 */
@Service
@Transactional(readOnly = true)
public class PassCheckService extends CrudService<PassCheckMapper, PassCheck> {

	@Autowired
	PassCheckMapper passCheckMapper;

	//根据车牌号和当前时间查询有效的出门条
	public  List<PassCheck> findPassByVehicleNo(PassCheck p){
		return passCheckMapper.findPassByVehicleNo(p);
	};

	public PassCheck get(String id) {
		return super.get(id);
	}
	
	public List<PassCheck> findList(PassCheck passCheck) {
		return super.findList(passCheck);
	}
	
	public Page<PassCheck> findPage(Page<PassCheck> page, PassCheck passCheck) {

		if(passCheck.getVehicleNo()!=null&&!"".equals(passCheck.getVehicleNo())) {
			String v = "%"+passCheck.getVehicleNo()+"%";
			passCheck.setVehicleNo(v);
		}
		if(passCheck.getTrnpAppNo()!=null&&!"".equals(passCheck.getTrnpAppNo())) {
			String v = "%"+passCheck.getTrnpAppNo()+"%";
			passCheck.setTrnpAppNo(v);
		}
		if("".equals(page.getOrderBy())||page.getOrderBy()==null){
			page.setOrderBy("a.valid_flag ASC, a.start_time DESC");
		}

		return super.findPage(page, passCheck);
	}
	
	@Transactional(readOnly = false)
	public void save(PassCheck passCheck) {
		super.save(passCheck);
	}
	
	@Transactional(readOnly = false)
	public void delete(PassCheck passCheck) {
		super.delete(passCheck);
	}


	@Transactional(readOnly = false)
	public void delByLogic(PassCheck p){
		passCheckMapper.delByLogic(p);
	}

	@Transactional(readOnly = false)
	public int insertPassCheckResult(PassCheck p){
          return passCheckMapper.insertPassCheckResult(p);
	}

	@Transactional(readOnly = false)
    public void updateDateType(PassCheck p){
		passCheckMapper.updateDateType(p);
	}

	public List<PassCheck> findTrnpAppNo(PassCheck p){
		return passCheckMapper.findTrnpAppNo(p);
	}

    @Transactional(readOnly = false)
	public void updateDeal(PassCheck p){
	    passCheckMapper.updateDeal(p);
    }
    //出门条
	public List<PassCheck> getVehiclePassCheck(PassCheck p){
		return passCheckMapper.getVehiclePassCheckList(p);
	}
	//出门条未出厂
	public List<PassCheck> getVehiclePassCheckU(PassCheck p){
		return passCheckMapper.getVehiclePassCheckUList(p);
	}
}