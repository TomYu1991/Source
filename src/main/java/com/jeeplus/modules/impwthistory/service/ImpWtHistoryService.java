/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.impwthistory.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.impwthistory.entity.ImpWtHistory;
import com.jeeplus.modules.impwthistory.mapper.ImpWtHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 皮重历史Service
 * @author 张鲁蒙
 * @version 2019-03-05
 */
@Service
@Transactional(readOnly = true)
public class ImpWtHistoryService extends CrudService<ImpWtHistoryMapper, ImpWtHistory> {

	@Autowired
	private ImpWtHistoryMapper impWtHistoryMapper;

	public ImpWtHistory get(String id) {
		return super.get(id);
	}
	
	public List<ImpWtHistory> findList(ImpWtHistory impWtHistory) {
		return super.findList(impWtHistory);
	}
	
	public Page<ImpWtHistory> findPage(Page<ImpWtHistory> page, ImpWtHistory impWtHistory) {
		return super.findPage(page, impWtHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(ImpWtHistory impWtHistory) {
		super.save(impWtHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(ImpWtHistory impWtHistory) {
		super.delete(impWtHistory);
	}

	public ImpWtHistory queryImpWtHistory(String v){
		ImpWtHistory i = new ImpWtHistory();
		i.setConsignId(v);
		return impWtHistoryMapper.queryImpWtHistory(i);
	}
	public void deleteImpWtHistory(String v){
		ImpWtHistory i = new ImpWtHistory();
		i.setConsignId(v);
		impWtHistoryMapper.deleteImpWtHistory(i);
	}
	public void updatePic(ImpWtHistory i){

		impWtHistoryMapper.updatePic(i);
	}
	public void updateTime(ImpWtHistory i){

		impWtHistoryMapper.updateTime(i);
	}

	@Transactional(readOnly = false)
	public void deleteByVehicleNo(String vehicleNo){
		ImpWtHistory i = new ImpWtHistory();
		i.setVehicleNo(vehicleNo);
		impWtHistoryMapper.deleteByVehicleNo(i);
	}

	public List<ImpWtHistory> queryImpWtHistoryByConsignId(String consignId){
		ImpWtHistory i = new ImpWtHistory();
		i.setConsignId(consignId);
		return impWtHistoryMapper.queryImpWtHistoryByConsignId(i);
	}

	public ImpWtHistory queryImpWtHistoryAvg(String ve){
		ImpWtHistory i = new ImpWtHistory();
		i.setVehicleNo(ve);
		return impWtHistoryMapper.queryImpWtHistoryAvg(i);
	}

}