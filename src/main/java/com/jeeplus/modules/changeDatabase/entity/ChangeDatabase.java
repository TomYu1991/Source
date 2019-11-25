/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.changeDatabase.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;

/**
 * 磅单管理Entity
 * @author jeeplus
 * @version 2018-12-25
 */
public class ChangeDatabase extends DataEntity<ChangeDatabase> {
	

	private String netStatus;  //网络状态


	public String getNetStatus() {
		return netStatus;
	}

	public void setNetStatus(String netStatus) {
		this.netStatus = netStatus;
	}


	
}