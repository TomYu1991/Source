/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.synchroinfo.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.synchroinfo.entity.DataSynchroInfo;

/**
 * 数据异常信息MAPPER接口
 * @author 张鲁蒙
 * @version 2019-04-18
 */
@MyBatisMapper
public interface DataSynchroInfoMapper extends BaseMapper<DataSynchroInfo> {

    void saveRecord(DataSynchroInfo dataSynchroInfo);
}