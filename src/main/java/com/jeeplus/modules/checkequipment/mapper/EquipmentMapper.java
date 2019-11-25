/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.checkequipment.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.checkequipment.entity.Equipment;
import com.jeeplus.modules.station.entity.WorkStation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 点检设备表MAPPER接口
 * @author 张鲁蒙
 * @version 2019-01-04
 */
@MyBatisMapper
public interface EquipmentMapper extends BaseMapper<Equipment> {

    List findEquipmentUnique(@Param ("equipment")String equipment, @Param("station")String station);
}