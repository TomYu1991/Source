/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.print.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.print.entity.Print;
import com.jeeplus.modules.weight.entity.Weight;

import java.util.List;

/**
 * 打印MAPPER接口
 * @author 打印
 * @version 2019-03-20
 */
@MyBatisMapper
public interface PrintMapper extends BaseMapper<Print> {

    List<Print> querybill();

    List<Print> queryweight();
	
}