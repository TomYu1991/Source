/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightrecord.entity;

import com.jeeplus.modules.weightrecord.entity.WeightRecord;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 修改磅单记录表Entity
 * @author 汤进国
 * @version 2019-04-18
 */
public class UpdateWeightRecord extends DataEntity<UpdateWeightRecord> {
	
	private static final long serialVersionUID = 1L;
	private String operation;		// 操作
	private String content;		// 操作内容
	private WeightRecord weight;		// 磅单号 父类
	
	public UpdateWeightRecord() {
		super();
	}

	public UpdateWeightRecord(String id){
		super(id);
	}

	public UpdateWeightRecord(WeightRecord weight){
		this.weight = weight;
	}

	@ExcelField(title="操作", dictType="weight_operation", align=2, sort=6)
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	@ExcelField(title="操作内容", align=2, sort=7)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public WeightRecord getWeight() {
		return weight;
	}

	public void setWeight(WeightRecord weight) {
		this.weight = weight;
	}
	
}