/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.consign_tmp.service;

import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.consign_tmp.entity.ConsignTmp;
import com.jeeplus.modules.consign_tmp.mapper.ConsignTmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 委托单/预约单管理Service
 *
 * @author 汤进国
 * @version 2019-01-17
 */
@Service
@Transactional(readOnly = true)
public class ConsignTmpService extends CrudService<ConsignTmpMapper, ConsignTmp> {

    @Autowired
    ConsignTmpMapper consignTmpmapper;


    public List<ConsignTmp> findListByOptflag() {
        return consignTmpmapper.findListByOptflag();
    }

    public void updateConsignTmp(ConsignTmp tmp){
        consignTmpmapper.updateConsignTmp(tmp);
    }
}