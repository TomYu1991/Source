/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.swipecard.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.swipecard.entity.SwipeCard;

/**
 * 刷卡人员权限MAPPER接口
 * @author zhanglumeng 
 * @version 2019-02-13
 */
@MyBatisMapper
public interface SwipeCardMapper extends BaseMapper<SwipeCard> {

    void deleteOne(String id);

    SwipeCard findByIcCard(String ickh);

    //接口方法
    //新增
    int insertSwipeCard(SwipeCard swipeCard);
    //物理删除
    int deleteSwipeCard(SwipeCard swipeCard);

    void saveIC(SwipeCard swipeCard);
}