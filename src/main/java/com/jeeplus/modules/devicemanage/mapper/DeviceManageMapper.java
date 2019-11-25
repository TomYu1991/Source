/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.devicemanage.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.devicemanage.entity.DeviceManage;

/**
 * 设备状态管理MAPPER接口
 * @author 汤进国
 * @version 2019-01-02
 */
@MyBatisMapper
public interface DeviceManageMapper extends BaseMapper<DeviceManage> {

    /**
     * 开启按钮
     * @param
     * @return
     */
    //  int open(DeviceManage deviceManage);
    int open(DeviceManage deviceManage);

    /**
     * 屏蔽按钮
     * @param
     * @return
     */
    int guan(DeviceManage deviceManage);
}