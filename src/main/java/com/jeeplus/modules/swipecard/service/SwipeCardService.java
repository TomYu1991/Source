/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.swipecard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.swipecard.entity.SwipeCard;
import com.jeeplus.modules.swipecard.mapper.SwipeCardMapper;

/**
 * 刷卡人员权限Service
 * @author zhanglumeng 
 * @version 2019-02-13
 */
@Service
@Transactional(readOnly = true)
public class SwipeCardService extends CrudService<SwipeCardMapper, SwipeCard> {
    @Autowired
	private  SwipeCardMapper swipeCardMapper;
	public SwipeCard get(String id) {
		return super.get(id);
	}
	
	public List<SwipeCard> findList(SwipeCard swipeCard) {
		return super.findList(swipeCard);
	}
	
	public Page<SwipeCard> findPage(Page<SwipeCard> page, SwipeCard swipeCard) {
		return super.findPage(page, swipeCard);
	}
	
	@Transactional(readOnly = false)
	public void save(SwipeCard swipeCard) {
		super.save(swipeCard);
	}
	
	@Transactional(readOnly = false)
	public void delete(SwipeCard swipeCard) {
		super.delete(swipeCard);
	}

	public void deleteOne(String id){
		swipeCardMapper.deleteOne(id);
	}

	public SwipeCard findByIcCard(String ickh){return swipeCardMapper.findByIcCard(ickh);}

	//接口
	//新增
	@Transactional(readOnly = false)
	public int insertSwipeCard(SwipeCard swipeCard) {
		return swipeCardMapper.insertSwipeCard(swipeCard);
	}
	//删除
	@Transactional(readOnly = false)
	public int deleteSwipeCard(SwipeCard swipeCard) {
		return swipeCardMapper.deleteSwipeCard(swipeCard);
	}

	public void saveIC(SwipeCard swipeCard){
		swipeCardMapper.saveIC(swipeCard);
	}
}