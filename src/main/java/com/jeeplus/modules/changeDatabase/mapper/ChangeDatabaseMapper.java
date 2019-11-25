/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.changeDatabase.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.changeDatabase.entity.ChangeDatabase;
import com.jeeplus.modules.weight.entity.Weight;

import java.util.List;

/**
 * 磅单管理MAPPER接口
 * @author jeeplus
 * @version 2018-12-25
 */
@MyBatisMapper
public interface ChangeDatabaseMapper extends BaseMapper<ChangeDatabaseMapper> {
	int cancel(ChangeDatabase w);

	ChangeDatabase queryNetStatus();

	void updateNetStatus(ChangeDatabase w);


}