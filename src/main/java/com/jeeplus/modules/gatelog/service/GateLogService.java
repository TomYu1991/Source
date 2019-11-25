/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.gatelog.service;

import java.util.List;

import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.gatelog.entity.GateLog;
import com.jeeplus.modules.gatelog.mapper.GateLogMapper;
import org.springframework.util.Base64Utils;

/**
 * 门岗日志Service
 * @author jeeplus
 * @version 2018-12-22
 */
@Service
@Transactional(readOnly = true)
public class GateLogService extends CrudService<GateLogMapper, GateLog> {

	@Autowired
	private GateLogMapper gateLogMapper;

	public GateLog get(String id) {
		return super.get(id);
	}
	
	public List<GateLog> findList(GateLog gateLog) {
		return super.findList(gateLog);
	}
	
	public Page<GateLog> findPage(Page<GateLog> page, GateLog gateLog) {
		if(gateLog.getVehicleNo()!=null&&!"".equals(gateLog.getVehicleNo())){
			String v = "%"+gateLog.getVehicleNo()+"%";
			gateLog.setVehicleNo(v);
		}
		return super.findPage(page, gateLog);
	}
	
	@Transactional(readOnly = false)
	public void save(GateLog gateLog) {
		if(gateLog.getU()!=null){
			gateLog.setOfficeId(UserUtils.get(gateLog.getU().getId()).getOffice().getName());
			gateLog.setCompanyId(UserUtils.get(gateLog.getU().getId()).getCompany().getName());
		}
		super.save(gateLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(GateLog gateLog) {
		super.delete(gateLog);
	}

	//当前排队车辆数量
	public  GateLog countCarByVehicleNo(GateLog g){
		GateLog l = gateLogMapper.countCarByVehicleNo(g);
		return l;
	}
	//最大排队数量
	public  GateLog queryMaxCarNum(){
		GateLog l = gateLogMapper.queryMaxCarNum();
		return l;
	}

	public List<GateLog> queryGateLogbd(String weighNo){
		return  gateLogMapper.queryGateLogbd(weighNo);
	}
}