package com.matrix.widget.mapper;

import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@MyBatisMapper
public interface AutoCompleteWidgetMapper {

    public List<LinkedHashMap<String, Object>> execSelectSql(Map<String, Object> params);
}
