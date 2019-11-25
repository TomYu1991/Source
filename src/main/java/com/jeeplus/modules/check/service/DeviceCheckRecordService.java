/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.check.entity.DeviceCheckRecord;
import com.jeeplus.modules.check.mapper.DeviceCheckRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author
 * @version 2019-03-16
 */
@Service
@Transactional(readOnly = true)
public class DeviceCheckRecordService extends CrudService<DeviceCheckRecordMapper, DeviceCheckRecord> {

    @Autowired
    private DeviceCheckRecordMapper mapper;


    public List<LinkedHashMap<String, Object>> findTaskList(Map<String,Object> params) {

        return mapper.findTaskList(params);
    }

    public void finishRecord(DeviceCheckRecord deviceCheckRecord){
        mapper.finish(deviceCheckRecord);
    }

//    public Page<T> findPage(Page<T> page, T entity) {
//        dataRuleFilter(entity);
//        entity.setPage(page);
//        page.setList(this.mapper.findList(entity));
//        return page;
//    }


    public Page<DeviceCheckRecord> findFinishedPage(Page<DeviceCheckRecord> page, DeviceCheckRecord deviceCheckRecord) {
        dataRuleFilter(deviceCheckRecord);
        deviceCheckRecord.setPage(page);
        page.setList(mapper.findFinishedList(deviceCheckRecord));
        return page;
    }

    public List<DeviceCheckRecord> findFinishedRecord(DeviceCheckRecord deviceCheckRecord){
        return mapper.findFinishedList(deviceCheckRecord);
    }

    public List<DeviceCheckRecord> getReportList(DeviceCheckRecord deviceCheckRecord){
        return mapper.getReportList(deviceCheckRecord);
    }

    public void updateCancel(DeviceCheckRecord deviceCheckRecord){
        mapper.updateCancel(deviceCheckRecord);
    }

    public void updateEdit(DeviceCheckRecord deviceCheckRecord){
        mapper.updateEdit(deviceCheckRecord);
    }
    public void cancel(DeviceCheckRecord deviceCheckRecord){
        mapper.cancel(deviceCheckRecord);
    }
}