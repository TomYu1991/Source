/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.userpasscord.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;

import java.util.List;

/**
 * 员工通行记录MAPPER接口
 * @author 汤进国
 * @version 2019-03-16
 */
@MyBatisMapper
public interface UserPassRecordMapper extends BaseMapper<UserPassRecord> {

    List<UserPassRecord> findInfoByCard(UserPassRecord u);

    void saveUserPassInfo(UserPassRecord u);

    void deleteUserPassInfo();


    int getUserInCounts(UserPassRecord record);

    int getUserOutCounts(UserPassRecord record);
    int getConsignUser(UserPassRecord record);

    int getIntoUserList(UserPassRecord record);
}