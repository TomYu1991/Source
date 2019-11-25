/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.check.entity.DeviceCheckItem;
import com.jeeplus.modules.check.entity.DeviceCheckRecord;

/**
 * 员工通行记录MAPPER接口
 * @author 汤进国
 * @version 2019-03-16
 */
@MyBatisMapper
public interface DeviceCheckItemMapper extends BaseMapper<DeviceCheckItem> {

    DeviceCheckItem getConfigItemById(String id);


}