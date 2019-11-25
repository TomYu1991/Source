/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.videos.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.videos.entity.Videos;

import java.util.List;

/**
 * 视频配置管理MAPPER接口
 * @author zhanglumeng 
 * @version 2019-02-22
 */
@MyBatisMapper
public interface VideosMapper extends BaseMapper<Videos> {

    List<Videos> getVediosByDbid(String dbid);

    Videos getCodeByName(String institution);

    List<Videos> queryVideoCamera(String ip);
}