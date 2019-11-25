/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.devicemanage.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.weight.entity.Weight;
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
import com.jeeplus.modules.devicemanage.entity.DeviceManage;
import com.jeeplus.modules.devicemanage.service.DeviceManageService;

/**
 * 设备状态管理Controller
 * @author 汤进国
 * @version 2019-01-02
 */
@Controller
@RequestMapping(value = "${adminPath}/devicemanage/deviceManage")
public class DeviceManageController extends BaseController {

	@Autowired
	private DeviceManageService deviceManageService;


	/**
	 * 开启按钮
	 */
	@ResponseBody
	@RequiresPermissions("devicemanage:deviceManage:open")
	@RequestMapping(value = "open")
	public AjaxJson open(DeviceManage deviceManage) {
		AjaxJson j = new AjaxJson();
		deviceManageService.open(deviceManage);
		j.setMsg("开启成功");
		return j;
	}

	/**
	 * 屏蔽按钮
	 */
	@ResponseBody
	@RequiresPermissions("devicemanage:deviceManage:guan")
	@RequestMapping(value = "guan")
	public AjaxJson guan(DeviceManage deviceManage) {
		AjaxJson j = new AjaxJson();
		deviceManageService.guan(deviceManage);
		j.setMsg("屏蔽成功");
		return j;
	}

	@ModelAttribute
	public DeviceManage get(@RequestParam(required=false) String id) {
		DeviceManage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = deviceManageService.get(id);
		}
		if (entity == null){
			entity = new DeviceManage();
		}
		return entity;
	}
	
	/**
	 * 设备状态管理列表页面
	 */
	@RequiresPermissions("devicemanage:deviceManage:list")
	@RequestMapping(value = {"list", ""})
	public String list(DeviceManage deviceManage, Model model) {

		model.addAttribute("deviceManage", deviceManage);
		return "modules/devicemanage/deviceManageList";
	}
	
		/**
	 * 设备状态管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("devicemanage:deviceManage:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DeviceManage deviceManage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DeviceManage> page = new Page<>();
		if(deviceManage.getSearchFlag()!=null&&"1".equals(deviceManage.getSearchFlag())){
			page = deviceManageService.findPage(new Page<DeviceManage>(request, response), deviceManage);
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑设备状态管理表单页面
	 */
	@RequiresPermissions(value={"devicemanage:deviceManage:view","devicemanage:deviceManage:add","devicemanage:deviceManage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DeviceManage deviceManage, Model model) {
		model.addAttribute("deviceManage", deviceManage);
		return "modules/devicemanage/deviceManageForm";
	}

	/**
	 * 保存设备状态管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"devicemanage:deviceManage:add","devicemanage:deviceManage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(DeviceManage deviceManage, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(deviceManage);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		deviceManageService.save(deviceManage);//保存
		j.setSuccess(true);
		j.setMsg("保存设备状态管理成功");
		return j;
	}
	
	/**
	 * 删除设备状态管理
	 */
	@ResponseBody
	@RequiresPermissions("devicemanage:deviceManage:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(DeviceManage deviceManage) {
		AjaxJson j = new AjaxJson();
		deviceManageService.delete(deviceManage);
		j.setMsg("删除设备状态管理成功");
		return j;
	}
	
	/**
	 * 批量删除设备状态管理
	 */
	@ResponseBody
	@RequiresPermissions("devicemanage:deviceManage:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			deviceManageService.delete(deviceManageService.get(id));
		}
		j.setMsg("删除设备状态管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("devicemanage:deviceManage:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(DeviceManage deviceManage, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			String  param= URLDecoder.decode(request.getParameter("data"),"UTF-8");
			JSONObject jo = JSONObject.fromObject(param);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(DeviceManage.class);
			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

			deviceManage = (DeviceManage)JSONObject.toBean(jo,DeviceManage.class);

            String fileName = "设备状态管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DeviceManage> page = deviceManageService.findPage(new Page<DeviceManage>(request, response, -1), deviceManage);
    		new ExportExcel("设备状态管理", DeviceManage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出设备状态管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据
	 */
	@ResponseBody
	@RequiresPermissions("devicemanage:deviceManage:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DeviceManage> list = ei.getDataList(DeviceManage.class);
			for (DeviceManage deviceManage : list){
				try{
					deviceManageService.save(deviceManage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条设备状态管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条设备状态管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入设备状态管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入设备状态管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("devicemanage:deviceManage:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "设备状态管理数据导入模板.xlsx";
    		List<DeviceManage> list = Lists.newArrayList(); 
    		new ExportExcel("设备状态管理数据", DeviceManage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

	/**
	 * 判断所有设备状态
	 */
	@ResponseBody
	@RequestMapping(value = "checkState")
	public AjaxJson checkState() {
		AjaxJson j = new AjaxJson();
		DeviceManage d = new DeviceManage();
		List<DeviceManage> list = deviceManageService.findList(d);
		j.setList(list);
		return j;
	}

}