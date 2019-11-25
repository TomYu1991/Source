/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.station.entity;

import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 人员管理Entity
 * @author 汤进国
 * @version 2019-01-02
 */
public class StationWorker extends DataEntity<StationWorker> {
	
	private static final long serialVersionUID = 1L;
	private WorkStation station;		// 工作站id 父类
	private User user;		// 人员id
	
	public StationWorker() {
		super();
	}

	public StationWorker(String id){
		super(id);
	}

	public StationWorker(WorkStation station){
		this.station = station;
	}

	public WorkStation getStation() {
		return station;
	}

	public void setStation(WorkStation station) {
		this.station = station;
	}
	
	@ExcelField(title="人员id", fieldType=User.class, value="user.name", align=2, sort=7)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}