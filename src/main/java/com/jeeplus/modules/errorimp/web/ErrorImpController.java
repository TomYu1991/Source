/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.errorimp.web;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.jeeplus.modules.errorimp.entity.ErrorImp;
import com.jeeplus.modules.errorimp.service.ErrorImpService;

/**
 * 异常皮重信息Controller
 * @author 汤进国
 * @version 2019-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/errorimp/errorImp")
public class ErrorImpController extends BaseController {

	@Autowired
	private ErrorImpService errorImpService;
	
	@ModelAttribute
	public ErrorImp get(@RequestParam(required=false) String id) {
		ErrorImp entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = errorImpService.get(id);
		}
		if (entity == null){
			entity = new ErrorImp();
		}
		return entity;
	}
	
	/**
	 * 异常皮重信息列表页面
	 */
	@RequiresPermissions("errorimp:errorImp:list")
	@RequestMapping(value = {"list", ""})
	public String list(ErrorImp errorImp, Model model) {
		model.addAttribute("errorImp", errorImp);
		return "modules/errorimp/errorImpList";
	}
	
		/**
	 * 异常皮重信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("errorimp:errorImp:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ErrorImp errorImp, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErrorImp> page = errorImpService.findPage(new Page<ErrorImp>(request, response), errorImp); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑异常皮重信息表单页面
	 */
	@RequiresPermissions(value={"errorimp:errorImp:view","errorimp:errorImp:add","errorimp:errorImp:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, ErrorImp errorImp, Model model) {
		model.addAttribute("errorImp", errorImp);
		model.addAttribute("mode", mode);
		return "modules/errorimp/errorImpForm";
	}

	/**
	 * 保存异常皮重信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"errorimp:errorImp:add","errorimp:errorImp:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ErrorImp errorImp, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(errorImp);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		errorImpService.save(errorImp);//保存
		j.setSuccess(true);
		j.setMsg("保存异常皮重信息成功");
		return j;
	}
	
	/**
	 * 删除异常皮重信息
	 */
	@ResponseBody
	@RequiresPermissions("errorimp:errorImp:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ErrorImp errorImp) {
		AjaxJson j = new AjaxJson();
		errorImpService.delete(errorImp);
		j.setMsg("删除异常皮重信息成功");
		return j;
	}
	
	/**
	 * 批量删除异常皮重信息
	 */
	@ResponseBody
	@RequiresPermissions("errorimp:errorImp:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			errorImpService.delete(errorImpService.get(id));
		}
		j.setMsg("删除异常皮重信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("errorimp:errorImp:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ErrorImp errorImp, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			String param = URLDecoder.decode(request.getParameter("data"), "UTF-8");
			JSONObject jo = JSONObject.fromObject(param);

			if (jo.get("beginCreateDate") == null || "".equals(jo.get("beginCreateDate"))) {
				jo.remove("beginCreateDate");
			}
			if (jo.get("endCreateDate") == null || "".equals(jo.get("endCreateDate"))) {
				jo.remove("endCreateDate");
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(ErrorImp.class);
			String[] dateFamates = new String[]{"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

			errorImp = (ErrorImp) JSONObject.toBean(jo, ErrorImp.class);

			String fileName = "异常皮重信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ErrorImp> page = errorImpService.findPage(new Page<ErrorImp>(request, response, -1), errorImp);
    		new ExportExcel("异常皮重信息", ErrorImp.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出异常皮重信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("errorimp:errorImp:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ErrorImp> list = ei.getDataList(ErrorImp.class);
			for (ErrorImp errorImp : list){
				try{
					errorImpService.save(errorImp);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条异常皮重信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条异常皮重信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入异常皮重信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入异常皮重信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("errorimp:errorImp:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "异常皮重信息数据导入模板.xlsx";
    		List<ErrorImp> list = Lists.newArrayList(); 
    		new ExportExcel("异常皮重信息数据", ErrorImp.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}