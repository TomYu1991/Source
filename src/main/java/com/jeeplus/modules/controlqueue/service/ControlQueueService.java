/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.controlqueue.service;

import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.mapper.ConsignMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.mapper.WeightMapper;
import com.jeeplus.modules.weightrecord.entity.UpdateWeightRecord;
import com.jeeplus.modules.weightrecord.entity.WeightRecord;
import com.jeeplus.modules.weightrecord.mapper.UpdateWeightRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.controlqueue.entity.ControlQueue;
import com.jeeplus.modules.controlqueue.mapper.ControlQueueMapper;

/**
 * 集控室排队Service
 * @author 汤进国
 * @version 2019-01-10
 */
@Service
@Transactional(readOnly = true)
public class ControlQueueService extends CrudService<ControlQueueMapper, ControlQueue> {

	@Autowired
	ControlQueueMapper controlQueueMapper;

	@Autowired
	ConsignMapper consignMapper;

	@Autowired
	WeightMapper weightMapper;
	@Autowired
	private UpdateWeightRecordMapper updateWeightRecordMapper;

	public ControlQueue get(String id) {
		return super.get(id);
	}
	
	public List<ControlQueue> findList(ControlQueue controlQueue) {
		return super.findList(controlQueue);
	}
	
	public Page<ControlQueue> findPage(Page<ControlQueue> page, ControlQueue controlQueue) {

		return super.findPage(page,controlQueue);
	}
	
	@Transactional(readOnly = false)
	public void save(ControlQueue controlQueue) {
		List<ControlQueue> cq = controlQueueMapper.getMinSeatNum();
		if(cq.size()>0){
			controlQueue.setSeatNum(cq.get(0).getSeatNum());
		}else{
			controlQueue.setSeatNum("4");
		}


		controlQueue.setId(IdGen.uuid());
		controlQueueMapper.insert(controlQueue);
	}
	
	@Transactional(readOnly = false)
	public void delete(ControlQueue controlQueue) {
		super.delete(controlQueue);
	}

	@Transactional(readOnly = false)
	public void deleteQueue(ControlQueue controlQueue, Consign consign, Weight weight) {
		UpdateWeightRecord uwr = new UpdateWeightRecord();
		uwr.setOperation("1");
		uwr.setId(IdGen.uuid());
		uwr.setUpdateBy(UserUtils.getUser());
		uwr.setUpdateDate(new Date());
		WeightRecord wr = new WeightRecord();
		wr.setWeighNo(weight.getWeighNo());
		uwr.setWeight(wr);
		updateWeightRecordMapper.insert(uwr);
		consignMapper.updateweightState(consign);
			//回退上传
			weightMapper.update(weight);
		controlQueue.setState("4");
		controlQueueMapper.updateState(controlQueue);
	}

	@Transactional(readOnly = false)
	public  void updateState(ControlQueue controlQueue,Weight weight){

		if(weight!=null){

			UpdateWeightRecord uwr = new UpdateWeightRecord();
			uwr.setOperation("4");
			if(controlQueue.getRemarks()!=null){
				if("1".equals(controlQueue.getRemarks())){
					uwr.setContent("过磅超时处理");
				}
				if("1".equals(controlQueue.getRemarks())){
					uwr.setContent("皮重异常处理");
				}
			}
			uwr.setId(IdGen.uuid());
			uwr.setUpdateBy(UserUtils.getUser());
			uwr.setUpdateDate(new Date());
			WeightRecord wr = new WeightRecord();
			wr.setWeighNo(weight.getWeighNo());
			uwr.setWeight(wr);
			updateWeightRecordMapper.insert(uwr);

		}
		controlQueueMapper.updateState(controlQueue);
	}

    @Transactional(readOnly = false)
    public void updateStateByWeighId(ControlQueue controlQueue){
        controlQueueMapper.updateStateByWeighId(controlQueue);
    }

	@Transactional(readOnly = false)
	public  void updateStateByWeight(ControlQueue controlQueue){
		controlQueueMapper.updateStateByWeight(controlQueue);
	}

	public ControlQueue checkSeatNum(ControlQueue controlQueue) {

		return controlQueueMapper.checkSeatNum(controlQueue);
	}

	public ControlQueue countQueue(ControlQueue c) {


		return controlQueueMapper.countQueue(c);
	}
	public ControlQueue countQueueAll(){
		return  controlQueueMapper.countQueueAll();
	}

	public ControlQueue queryInfoByWeigh(String weigh){

		return controlQueueMapper.queryInfoByWeigh(weigh);
	}
	public void removeQueueById(ControlQueue queue){
		controlQueueMapper.updateState(queue);
	}
}