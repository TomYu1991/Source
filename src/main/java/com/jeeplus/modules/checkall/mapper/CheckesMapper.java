/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.checkall.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.checkall.entity.Checkes;

/**
 * 点检表MAPPER接口
 * @author 张鲁蒙
 * @version 2019-01-07
 */
@MyBatisMapper
public interface CheckesMapper extends BaseMapper<Checkes> {
    /**
     * 获取设备管理的设备检查方法和点检设备描述
     * @param equip
     * @return
     */
    public Checkes getEquipment(String equip);
}