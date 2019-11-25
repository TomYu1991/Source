package com.matrix.widget.service;

import com.jeeplus.common.config.Global;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.matrix.widget.mapper.AutoCompleteWidgetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 拼音码自动完成插件Service
 */

@Service
public class AutoCompleteWidgetService {

    @Autowired
    protected AutoCompleteWidgetMapper mapper;

    public List<LinkedHashMap<String, Object>> execSelectSql(Map<String,Object> params) {
        params.put("dbName",Global.getConfig("jdbc.type"));
        params.put("user", UserUtils.getUser());
        return mapper.execSelectSql(params);
    }
}
