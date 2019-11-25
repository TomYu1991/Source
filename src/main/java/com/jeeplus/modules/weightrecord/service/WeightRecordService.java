/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightrecord.service;

import java.util.List;

import com.jeeplus.common.utils.IdGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.weightrecord.entity.WeightRecord;
import com.jeeplus.modules.weightrecord.mapper.WeightRecordMapper;
import com.jeeplus.modules.weightrecord.entity.InitWeight;
import com.jeeplus.modules.weightrecord.mapper.InitWeightMapper;
import com.jeeplus.modules.weightrecord.entity.PrintRecord;
import com.jeeplus.modules.weightrecord.mapper.PrintRecordMapper;
import com.jeeplus.modules.weightrecord.entity.UpdateWeightRecord;
import com.jeeplus.modules.weightrecord.mapper.UpdateWeightRecordMapper;

/**
 * 磅单信息记录Service
 * @author 汤进国
 * @version 2019-04-18
 */
@Service
@Transactional(readOnly = true)
public class WeightRecordService extends CrudService<WeightRecordMapper, WeightRecord> {

	@Autowired
	private InitWeightMapper initWeightMapper;
	@Autowired
	private PrintRecordMapper printRecordMapper;
	@Autowired
	private UpdateWeightRecordMapper updateWeightRecordMapper;
	
	public WeightRecord get(String id) {
		WeightRecord weightRecord = super.get(id);
		weightRecord.setInitWeightList(initWeightMapper.findList(new InitWeight(weightRecord)));
		weightRecord.setPrintRecordList(printRecordMapper.findList(new PrintRecord(weightRecord)));
		weightRecord.setUpdateWeightRecordList(updateWeightRecordMapper.findList(new UpdateWeightRecord(weightRecord)));
		return weightRecord;
	}
	
	public List<WeightRecord> findList(WeightRecord weightRecord) {
		return super.findList(weightRecord);
	}
	
	public Page<WeightRecord> findPage(Page<WeightRecord> page, WeightRecord weightRecord) {
		if("".equals(page.getOrderBy())||page.getOrderBy()==null){
			page.setOrderBy("a.weigh_no desc");
		}
		return super.findPage(page, weightRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(WeightRecord weightRecord) {

		super.save(weightRecord);
		for (InitWeight initWeight : weightRecord.getInitWeightList()){
			if (initWeight.getId() == null){
				continue;
			}
			if (InitWeight.DEL_FLAG_NORMAL.equals(initWeight.getDelFlag())){
				if (StringUtils.isBlank(initWeight.getId())){
					initWeight.setWeight(weightRecord);
					initWeight.preInsert();
					initWeightMapper.insert(initWeight);
				}else{
					initWeight.preUpdate();
					initWeightMapper.update(initWeight);
				}
			}else{
				initWeightMapper.delete(initWeight);
			}
		}
		for (PrintRecord printRecord : weightRecord.getPrintRecordList()){
			if (printRecord.getId() == null){
				continue;
			}
			if (PrintRecord.DEL_FLAG_NORMAL.equals(printRecord.getDelFlag())){
				if (StringUtils.isBlank(printRecord.getId())){
					printRecord.setWeight(weightRecord);
					printRecord.preInsert();
					printRecordMapper.insert(printRecord);
				}else{
					printRecord.preUpdate();
					printRecordMapper.update(printRecord);
				}
			}else{
				printRecordMapper.delete(printRecord);
			}
		}
		for (UpdateWeightRecord updateWeightRecord : weightRecord.getUpdateWeightRecordList()){
			if (updateWeightRecord.getId() == null){
				continue;
			}
			if (UpdateWeightRecord.DEL_FLAG_NORMAL.equals(updateWeightRecord.getDelFlag())){
				if (StringUtils.isBlank(updateWeightRecord.getId())){
					updateWeightRecord.setWeight(weightRecord);
					updateWeightRecord.preInsert();
					updateWeightRecordMapper.insert(updateWeightRecord);
				}else{
					updateWeightRecord.preUpdate();
					updateWeightRecordMapper.update(updateWeightRecord);
				}
			}else{
				updateWeightRecordMapper.delete(updateWeightRecord);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(WeightRecord weightRecord) {
		super.delete(weightRecord);
		initWeightMapper.delete(new InitWeight(weightRecord));
		printRecordMapper.delete(new PrintRecord(weightRecord));
		updateWeightRecordMapper.delete(new UpdateWeightRecord(weightRecord));
	}

}