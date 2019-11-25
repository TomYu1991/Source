package com.matrix.widget.web;

import com.matrix.widget.service.AutoCompleteWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 拼音码自动完成插件Controller
 *
 */
@Controller
@RequestMapping(value = "/widget/autocomplete/")
public class AutoCompleteWidgetController {

    @Autowired
    private AutoCompleteWidgetService autoCompleteWidgetService;

    @RequestMapping(value = {"list", ""})
    @ResponseBody
    public List<LinkedHashMap<String, Object>> autocomplete(HttpServletRequest request) {

        Map<String,Object> requestParam = new HashMap<String,Object>();

        Enumeration enumer = request.getParameterNames();
        while (enumer.hasMoreElements()) {
            String paramName = (String) enumer.nextElement();
            String paramValue = request.getParameter(paramName);
            requestParam.put(paramName, paramValue);
        }
        return autoCompleteWidgetService.execSelectSql(requestParam);
    }
}
