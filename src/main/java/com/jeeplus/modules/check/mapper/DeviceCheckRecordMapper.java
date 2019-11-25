/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.check.entity.DeviceCheckConfig;
import com.jeeplus.modules.check.entity.DeviceCheckRecord;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author
 * @version 2019-01-02
 */
@MyBatisMapper
public interface DeviceCheckRecordMapper extends BaseMapper<DeviceCheckRecord> {
    public List<LinkedHashMap<String, Object>> findTaskList(Map<String, Object> params);
    public void finish(DeviceCheckRecord deviceCheckRecord);
    public List<DeviceCheckRecord> findFinishedList(DeviceCheckRecord deviceCheckRecord);
    public List<DeviceCheckRecord> getReportList(DeviceCheckRecord deviceCheckRecord);
    public void updateCancel(DeviceCheckRecord deviceCheckRecord);
    public void updateEdit(DeviceCheckRecord deviceCheckRecord);
    public void cancel(DeviceCheckRecord deviceCheckRecord);
}