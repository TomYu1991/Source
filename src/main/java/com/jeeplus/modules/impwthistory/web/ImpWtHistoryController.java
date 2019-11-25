/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.impwthistory.web;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.impwthistory.entity.ImpWtHistory;
import com.jeeplus.modules.impwthistory.service.ImpWtHistoryService;

/**
 * 皮重历史Controller
 * @author 张鲁蒙
 * @version 2019-03-05
 */
@Controller
@RequestMapping(value = "${adminPath}/impwthistory/impWtHistory")
public class ImpWtHistoryController extends BaseController {

	@Autowired
	private ImpWtHistoryService impWtHistoryService;
	
	@ModelAttribute
	public ImpWtHistory get(@RequestParam(required=false) String id) {
		ImpWtHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = impWtHistoryService.get(id);
		}
		if (entity == null){
			entity = new ImpWtHistory();
		}
		return entity;
	}
	
	/**
	 * 皮重历史列表页面
	 */
	@RequiresPermissions("impwthistory:impWtHistory:list")
	@RequestMapping(value = {"list", ""})
	public String list(ImpWtHistory impWtHistory, Model model) {
		model.addAttribute("impWtHistory", impWtHistory);
		return "modules/impwthistory/impWtHistoryList";
	}
	
		/**
	 * 皮重历史列表数据
	 */
	@ResponseBody
	@RequiresPermissions("impwthistory:impWtHistory:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ImpWtHistory impWtHistory, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<ImpWtHistory> page = impWtHistoryService.findPage(new Page<ImpWtHistory>(request, response), impWtHistory);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑皮重历史表单页面
	 */
	@RequiresPermissions(value={"impwthistory:impWtHistory:view","impwthistory:impWtHistory:add","impwthistory:impWtHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, ImpWtHistory impWtHistory, Model model) {
		model.addAttribute("impWtHistory", impWtHistory);
		model.addAttribute("mode", mode);
		return "modules/impwthistory/impWtHistoryForm";
	}

	/**
	 * 保存皮重历史
	 */
	@ResponseBody
	@RequiresPermissions(value={"impwthistory:impWtHistory:add","impwthistory:impWtHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ImpWtHistory impWtHistory, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(impWtHistory);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		impWtHistoryService.save(impWtHistory);//保存
		j.setSuccess(true);
		j.setMsg("保存皮重历史成功");
		return j;
	}
	
	/**
	 * 删除皮重历史
	 */
	@ResponseBody
	@RequiresPermissions("impwthistory:impWtHistory:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ImpWtHistory impWtHistory) {
		AjaxJson j = new AjaxJson();
		impWtHistoryService.delete(impWtHistory);
		j.setMsg("删除皮重历史成功");
		return j;
	}
	
	/**
	 * 批量删除皮重历史
	 */
	@ResponseBody
	@RequiresPermissions("impwthistory:impWtHistory:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			impWtHistoryService.delete(impWtHistoryService.get(id));
		}
		j.setMsg("删除皮重历史成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("impwthistory:impWtHistory:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ImpWtHistory impWtHistory, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			String  param= URLDecoder.decode(request.getParameter("data"),"UTF-8");
			JSONObject jo = JSONObject.fromObject(param);

			if(jo.get("startTime")==null||"".equals(jo.get("startTime"))){
				jo.remove("startTime");
			}
			if(jo.get("endTime")==null||"".equals(jo.get("endTime"))){
				jo.remove("endTime");
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(ImpWtHistory.class);
			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

			impWtHistory = (ImpWtHistory)JSONObject.toBean(jo,ImpWtHistory.class);

            String fileName = "皮重历史"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ImpWtHistory> page = impWtHistoryService.findPage(new Page<ImpWtHistory>(request, response, -1), impWtHistory);
    		new ExportExcel("皮重历史", ImpWtHistory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出皮重历史记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("impwthistory:impWtHistory:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ImpWtHistory> list = ei.getDataList(ImpWtHistory.class);
			for (ImpWtHistory impWtHistory : list){
				try{
					impWtHistoryService.save(impWtHistory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条皮重历史记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条皮重历史记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入皮重历史失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入皮重历史数据模板
	 */
	@ResponseBody
	@RequiresPermissions("impwthistory:impWtHistory:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "皮重历史数据导入模板.xlsx";
    		List<ImpWtHistory> list = Lists.newArrayList(); 
    		new ExportExcel("皮重历史数据", ImpWtHistory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}