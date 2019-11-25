/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.service;

import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.check.entity.DeviceCheckRitem;
import com.jeeplus.modules.check.mapper.DeviceCheckRitemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author
 * @version 2019-01-02
 */
@Service
@Transactional(readOnly = true)
public class DeviceCheckRitemService extends CrudService<DeviceCheckRitemMapper, DeviceCheckRitem> {


	
}