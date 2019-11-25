/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.videos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.videos.entity.Videos;
import com.jeeplus.modules.videos.mapper.VideosMapper;

/**
 * 视频配置管理Service
 * @author zhanglumeng 
 * @version 2019-02-22
 */
@Service
@Transactional(readOnly = true)
public class VideosService extends CrudService<VideosMapper, Videos> {
    @Autowired
	private  VideosMapper videosMapper;

	public Videos get(String id) {
		return super.get(id);
	}
	
	public List<Videos> findList(Videos videos) {
		return super.findList(videos);
	}
	
	public Page<Videos> findPage(Page<Videos> page, Videos videos) {
		return super.findPage(page, videos);
	}
	
	@Transactional(readOnly = false)
	public void save(Videos videos) {
		super.save(videos);
	}
	
	@Transactional(readOnly = false)
	public void delete(Videos videos) {
		super.delete(videos);
	}

	public List<Videos> getVediosByDbid(String dbid){

		return videosMapper.getVediosByDbid(dbid);
	}

	public Videos getCodeByName(String institution){
		return videosMapper.getCodeByName(institution);
	}

	public List<Videos> queryVideoCamera(String ip){
		return videosMapper.queryVideoCamera(ip);
	}
}