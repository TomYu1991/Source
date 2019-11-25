/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.changeDatabase.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.changeDatabase.entity.ChangeDatabase;
import com.jeeplus.modules.changeDatabase.service.ChangeDatabaseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 磅单管理Controller
 *
 * @author jeeplus
 * @version 2018-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/changeDatabase/changeDatabase")
public class ChangeDatabaseController extends BaseController {

    @Autowired
    private ChangeDatabaseService changeDatabaseService;


    /**
     * 数据库切换管理页面
     */
    @RequiresPermissions("change:changeDatabase:changeDatabase")
    @RequestMapping(value = {"list", ""})
    public String list(ChangeDatabase changeDatabase, Model model) {
        model.addAttribute("changeDatabase", changeDatabase);
        return "modules/changedatabase/changeDatabase";
    }


    /**
     * 修改为本地的数据库
     */
    @ResponseBody
    @RequestMapping(value = "updateDataBase")
    public AjaxJson updateDataBase() {
        AjaxJson j = new AjaxJson();

        ChangeDatabase n =new ChangeDatabase();
        n.setNetStatus("2");
        changeDatabaseService.updateNetStatus(n);

        //保存，并加入注释
        j.setSuccess(true);
        j.setMsg("更改数据库成功!");
        return j;
    }

    /**
     * 修改为服务器的数据库
     */
    @ResponseBody
    @RequestMapping(value = "revertDataBase")
    public AjaxJson revertDataBase() {
        AjaxJson j = new AjaxJson();

        ChangeDatabase n =new ChangeDatabase();
        n.setNetStatus("1");
        changeDatabaseService.updateNetStatus(n);

        //保存，并加入注释
        j.setSuccess(true);
        j.setMsg("更改数据库成功!");
        return j;
    }
}
