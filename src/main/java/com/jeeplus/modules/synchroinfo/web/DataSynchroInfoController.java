/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.synchroinfo.web;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.consign.entity.Consign;
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
import com.jeeplus.modules.synchroinfo.entity.DataSynchroInfo;
import com.jeeplus.modules.synchroinfo.service.DataSynchroInfoService;

/**
 * 数据异常信息Controller
 * @author 张鲁蒙
 * @version 2019-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/synchroinfo/dataSynchroInfo")
public class DataSynchroInfoController extends BaseController {

	@Autowired
	private DataSynchroInfoService dataSynchroInfoService;
	
	@ModelAttribute
	public DataSynchroInfo get(@RequestParam(required=false) String id) {
		DataSynchroInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dataSynchroInfoService.get(id);
		}
		if (entity == null){
			entity = new DataSynchroInfo();
		}
		return entity;
	}
	
	/**
	 * synchroinfo列表页面
	 */
	@RequiresPermissions("synchroinfo:dataSynchroInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list(DataSynchroInfo dataSynchroInfo, Model model) {
		model.addAttribute("dataSynchroInfo", dataSynchroInfo);
		return "modules/synchroinfo/dataSynchroInfoList";
	}
	
    /**
	 * synchroinfo列表数据
	 */
	@ResponseBody
	@RequiresPermissions("synchroinfo:dataSynchroInfo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DataSynchroInfo dataSynchroInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DataSynchroInfo> page = new Page<>();
		if (dataSynchroInfo.getSearchFlag() != null && "1".equals(dataSynchroInfo.getSearchFlag())) {
			page = dataSynchroInfoService.findPage(new Page<DataSynchroInfo>(request, response), dataSynchroInfo);
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑synchroinfo表单页面
	 */
	@RequiresPermissions(value={"synchroinfo:dataSynchroInfo:view","synchroinfo:dataSynchroInfo:add","synchroinfo:dataSynchroInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DataSynchroInfo dataSynchroInfo, Model model) {
		model.addAttribute("dataSynchroInfo", dataSynchroInfo);
		return "modules/synchroinfo/dataSynchroInfoForm";
	}

	/**
	 * 保存synchroinfo
	 */
	@ResponseBody
	@RequiresPermissions(value={"synchroinfo:dataSynchroInfo:add","synchroinfo:dataSynchroInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(DataSynchroInfo dataSynchroInfo, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(dataSynchroInfo);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		dataSynchroInfoService.save(dataSynchroInfo);//保存
		j.setSuccess(true);
		j.setMsg("保存synchroinfo成功");
		return j;
	}
	
	/**
	 * 删除synchroinfo
	 */
	@ResponseBody
	@RequiresPermissions("synchroinfo:dataSynchroInfo:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(DataSynchroInfo dataSynchroInfo) {
		AjaxJson j = new AjaxJson();
		dataSynchroInfoService.delete(dataSynchroInfo);
		j.setMsg("删除synchroinfo成功");
		return j;
	}
	
	/**
	 * 批量删除synchroinfo
	 */
	@ResponseBody
	@RequiresPermissions("synchroinfo:dataSynchroInfo:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			dataSynchroInfoService.delete(dataSynchroInfoService.get(id));
		}
		j.setMsg("删除synchroinfo成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("synchroinfo:dataSynchroInfo:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(DataSynchroInfo dataSynchroInfo, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			String param = URLDecoder.decode(request.getParameter("data"), "UTF-8");
			JSONObject jo = JSONObject.fromObject(param);

			if (jo.get("biginCreateDate") == null || "".equals(jo.get("biginCreateDate"))) {
				jo.remove("biginCreateDate");
			}
			if (jo.get("endCreateDate") == null || "".equals(jo.get("endCreateDate"))) {
				jo.remove("endCreateDate");
			}

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(DataSynchroInfo.class);
			String[] dateFamates = new String[]{"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

			dataSynchroInfo = (DataSynchroInfo) JSONObject.toBean(jo,DataSynchroInfo.class);

			String fileName = "同步数据日志" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<DataSynchroInfo> page = dataSynchroInfoService.findPage(new Page<DataSynchroInfo>(request, response, -1), dataSynchroInfo);
			new ExportExcel("同步数据日志",DataSynchroInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出synchroinfo记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("synchroinfo:dataSynchroInfo:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DataSynchroInfo> list = ei.getDataList(DataSynchroInfo.class);
			for (DataSynchroInfo dataSynchroInfo : list){
				try{
					dataSynchroInfoService.save(dataSynchroInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条synchroinfo记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条synchroinfo记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入synchroinfo失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入synchroinfo数据模板
	 */
	@ResponseBody
	@RequiresPermissions("synchroinfo:dataSynchroInfo:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "synchroinfo数据导入模板.xlsx";
    		List<DataSynchroInfo> list = Lists.newArrayList(); 
    		new ExportExcel("synchroinfo数据", DataSynchroInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}