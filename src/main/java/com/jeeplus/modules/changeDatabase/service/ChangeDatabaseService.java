/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.changeDatabase.service;

import java.util.List;

import com.jeeplus.modules.changeDatabase.entity.ChangeDatabase;
import com.jeeplus.modules.changeDatabase.mapper.ChangeDatabaseMapper;
import com.jeeplus.modules.tools.utils.MultiDBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.mapper.WeightMapper;

/**
 * 磅单管理Service
 * @author jeeplus
 * @version 2018-12-25
 */
@Service
@Transactional(readOnly = true)
public class ChangeDatabaseService {

	@Autowired
	private ChangeDatabaseMapper changeDatabaseMapper;


	//查询网络状态
	public ChangeDatabase queryNetStatus(){
		return changeDatabaseMapper.queryNetStatus();
	};
	//修改网络状态
	@Transactional(readOnly = false)
	public void updateNetStatus(ChangeDatabase w){
		changeDatabaseMapper.updateNetStatus((ChangeDatabase) w);
	};


}