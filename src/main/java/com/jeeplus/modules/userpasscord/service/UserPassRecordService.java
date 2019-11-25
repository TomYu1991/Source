/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.userpasscord.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.userpasscord.mapper.UserPassRecordMapper;

/**
 * 员工通行记录Service
 * @author 汤进国
 * @version 2019-03-16
 */
@Service
@Transactional(readOnly = true)
public class UserPassRecordService extends CrudService<UserPassRecordMapper, UserPassRecord> {

	@Autowired
	UserPassRecordMapper userPassRecordMapper;

	public UserPassRecord get(String id) {
		return super.get(id);
	}
	
	public List<UserPassRecord> findList(UserPassRecord userPassRecord) {
		return super.findList(userPassRecord);
	}
	
	public Page<UserPassRecord> findPage(Page<UserPassRecord> page, UserPassRecord userPassRecord) {
		if(userPassRecord.getCardSerial()!=null&&!"".equals(userPassRecord.getCardSerial())){
			String v = "%"+userPassRecord.getCardSerial()+"%";
			userPassRecord.setCardSerial(v);
		}
		if(userPassRecord.getName()!=null&&!"".equals(userPassRecord.getName())){
			String v = "%"+userPassRecord.getName()+"%";
			userPassRecord.setName(v);
		}
		if(userPassRecord.getPersonnelId()!=null&&!"".equals(userPassRecord.getPersonnelId())){
			String v = "%"+userPassRecord.getPersonnelId()+"%";
			userPassRecord.setPersonnelId(v);
		}
		return super.findPage(page, userPassRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(UserPassRecord userPassRecord) {
		super.save(userPassRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserPassRecord userPassRecord) {
		super.delete(userPassRecord);
	}


	public List<UserPassRecord> findInfoByCard(String cardNum){
		UserPassRecord userPassRecord = new UserPassRecord();
		userPassRecord.setCardSerial(cardNum);
		return userPassRecordMapper.findInfoByCard(userPassRecord);
	}
	@Transactional(readOnly = false)
	public void saveUserPassInfo(UserPassRecord userPassRecord) {
		userPassRecordMapper.saveUserPassInfo(userPassRecord);
	}

	@Transactional(readOnly = false)
	public void deleteUserPassInfo() {
		userPassRecordMapper.deleteUserPassInfo();
	}

	//进门人员
	public int getUserInCount(UserPassRecord record){
		return userPassRecordMapper.getUserInCounts(record);
	}
	//出门人员
	public int getUserOutCount(UserPassRecord record){

		return userPassRecordMapper.getUserOutCounts(record);
	}
	//预约人员
	public int getConsignUserCount(UserPassRecord record){
		return userPassRecordMapper.getConsignUser(record);
	}
	//预约入厂人员
	public int getIntoFactoryUserList(UserPassRecord record){
		return userPassRecordMapper.getIntoUserList(record);
	}
}