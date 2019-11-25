/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.illegalinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.illegalinfo.entity.IllegalInfo;
import com.jeeplus.modules.illegalinfo.mapper.IllegalInfoMapper;

/**
 * 非法闯入信息记录Service
 * @author illegalinfo
 * @version 2019-04-06
 */
@Service
@Transactional(readOnly = true)
public class IllegalInfoService extends CrudService<IllegalInfoMapper, IllegalInfo> {

	@Autowired
	private IllegalInfoMapper illegalInfoMapper;

	public IllegalInfo get(String id) {
		return super.get(id);
	}
	
	public List<IllegalInfo> findList(IllegalInfo illegalInfo) {
		return super.findList(illegalInfo);
	}
	
	public Page<IllegalInfo> findPage(Page<IllegalInfo> page, IllegalInfo illegalInfo) {
		if(illegalInfo.getVehicleNo()!=null&&!"".equals(illegalInfo.getVehicleNo())) {
			String v = "%"+illegalInfo.getVehicleNo()+"%";
			illegalInfo.setVehicleNo(v);
		}
		if(illegalInfo.getConsigneUser()!=null&&!"".equals(illegalInfo.getConsigneUser())) {
			String v = "%"+illegalInfo.getConsigneUser()+"%";
			illegalInfo.setConsigneUser(v);
		}
		if(illegalInfo.getSupplierName()!=null&&!"".equals(illegalInfo.getSupplierName())) {
			String v = "%"+illegalInfo.getSupplierName()+"%";
			illegalInfo.setSupplierName(v);
		}
		if("".equals(page.getOrderBy())||page.getOrderBy()==null){
			page.setOrderBy("a.create_date desc");
		}
		return super.findPage(page, illegalInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(IllegalInfo illegalInfo) {
		super.save(illegalInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(IllegalInfo illegalInfo) {
		super.delete(illegalInfo);
	}

	@Transactional(readOnly = false)
	public void savePhoto(IllegalInfo illegalInfo) {

		illegalInfoMapper.savePhoto(illegalInfo);
	}

	public IllegalInfo queryLateInfoByVehicleNo(String v) {
		IllegalInfo illegalInfo = new IllegalInfo();
		illegalInfo.setVehicleNo(v);
		return illegalInfoMapper.queryLateInfoByVehicleNo(illegalInfo);
	}


}