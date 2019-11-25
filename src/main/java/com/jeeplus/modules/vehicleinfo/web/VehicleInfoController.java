/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.vehicleinfo.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.gatelog.entity.GateLog;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import com.jeeplus.modules.vehicleinfo.service.VehicleInfoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 车辆信息表Controller
 * @author 汤进国
 * @version 2019-01-17
 */
@Controller
@RequestMapping(value = "${adminPath}/vehicleinfo/vehicleInfo")
public class VehicleInfoController extends BaseController {

	@Autowired
	private VehicleInfoService vehicleInfoService;
	
	@ModelAttribute
	public VehicleInfo get(@RequestParam(required=false) String id) {
		VehicleInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = vehicleInfoService.get(id);
		}
		if (entity == null){
			entity = new VehicleInfo();
		}
		return entity;
	}
	
	/**
	 * 车辆信息列表页面
	 */
	@RequiresPermissions("vehicleinfo:vehicleInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list(VehicleInfo vehicleInfo, Model model) {
		model.addAttribute("vehicleInfo", vehicleInfo);
		return "modules/vehicleinfo/vehicleInfoList";
	}
	
		/**
	 * 车辆信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("vehicleinfo:vehicleInfo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(VehicleInfo vehicleInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VehicleInfo> page = new Page<>();
		if(vehicleInfo.getSearchFlag()!=null&&"1".equals(vehicleInfo.getSearchFlag())){
			page = vehicleInfoService.findPage(new Page<VehicleInfo>(request, response), vehicleInfo);
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑车辆信息表单页面
	 */
	@RequiresPermissions(value={"vehicleinfo:vehicleInfo:view","vehicleinfo:vehicleInfo:add","vehicleinfo:vehicleInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(VehicleInfo vehicleInfo, Model model) {
		model.addAttribute("vehicleInfo", vehicleInfo);
		return "modules/vehicleinfo/vehicleInfoForm";
	}

	/**
	 * 保存车辆信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"vehicleinfo:vehicleInfo:add","vehicleinfo:vehicleInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(VehicleInfo vehicleInfo, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(vehicleInfo);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		vehicleInfoService.save(vehicleInfo);//保存
		j.setSuccess(true);
		j.setMsg("保存车辆信息成功");
		return j;
	}
	
	/**
	 * 删除车辆信息
	 */
	@ResponseBody
	@RequiresPermissions("vehicleinfo:vehicleInfo:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(VehicleInfo vehicleInfo) {
		AjaxJson j = new AjaxJson();
		vehicleInfoService.delete(vehicleInfo);
		j.setMsg("删除车辆信息成功");
		return j;
	}
	
	/**
	 * 批量删除车辆信息
	 */
	@ResponseBody
	@RequiresPermissions("vehicleinfo:vehicleInfo:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			vehicleInfoService.delete(vehicleInfoService.get(id));
		}
		j.setMsg("删除车辆信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("vehicleinfo:vehicleInfo:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(VehicleInfo vehicleInfo, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			String  param= URLDecoder.decode(request.getParameter("data"),"UTF-8");
			JSONObject jo = JSONObject.fromObject(param);

			if(jo.get("startTime")==null||"".equals(jo.get("startTime"))){
				jo.remove("startTime");
			}
			if(jo.get("endTime")==null||"".equals(jo.get("endTime"))){
				jo.remove("endTime");;
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(VehicleInfo.class);
			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

			vehicleInfo = (VehicleInfo)JSONObject.toBean(jo,VehicleInfo.class);

            String fileName = "车辆信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<VehicleInfo> page = vehicleInfoService.findPage(new Page<VehicleInfo>(request, response, -1), vehicleInfo);
    		new ExportExcel("车辆信息", VehicleInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车辆信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("vehicleinfo:vehicleInfo:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<VehicleInfo> list = ei.getDataList(VehicleInfo.class);
			for (VehicleInfo vehicleInfo : list){
				try{
					vehicleInfoService.save(vehicleInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车辆信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条车辆信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入车辆信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入车辆信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("vehicleinfo:vehicleInfo:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车辆信息数据导入模板.xlsx";
    		List<VehicleInfo> list = Lists.newArrayList(); 
    		new ExportExcel("车辆信息数据", VehicleInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }
	/**
	 * 绑定RFID
	 * @param vehicleInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateRFID")
	public AjaxJson updateRFID(VehicleInfo vehicleInfo) {
		AjaxJson j = new AjaxJson();
		try {
			List<VehicleInfo> v = vehicleInfoService.getVehicleNoByRfid(vehicleInfo.getRfidNo().trim());
			if(v.size()>0){
				j.setSuccess(false);
				j.setMsg("RFID卡号已存在");
				return j;
			}
			List<VehicleInfo> vs = vehicleInfoService.getVehicleNoBySrfid(vehicleInfo.getRfidNo().trim());
			if(vs.size()>0){
				j.setSuccess(false);
				j.setMsg("RFID卡号已存在");
				return j;
			}
			vehicleInfo.setUpdateBy(UserUtils.getUser());
			vehicleInfo.setUpdateDate(new Date());
			vehicleInfoService.updaterfid(vehicleInfo);
			j.setSuccess(true);
			j.setMsg("RFID卡号绑定成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 绑定轨道衡RFID
	 * @param vehicleInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateRailRFID")
	public AjaxJson updateRailRFID(VehicleInfo vehicleInfo) {
		AjaxJson j = new AjaxJson();
		try {
			List<VehicleInfo> v = vehicleInfoService.getVehicleNoByRfid(vehicleInfo.getSrfidNo().trim());
			if(v.size()>0){
				j.setSuccess(false);
				j.setMsg("RFID卡号已存在");
				return j;
			}
			List<VehicleInfo> vs = vehicleInfoService.getVehicleNoBySrfid(vehicleInfo.getSrfidNo().trim());
			if(vs.size()>0){
				j.setSuccess(false);
				j.setMsg("RFID卡号已存在");
				return j;
			}
			vehicleInfo.setUpdateBy(UserUtils.getUser());
			vehicleInfo.setUpdateDate(new Date());
			vehicleInfoService.updaterRailfid(vehicleInfo);
			j.setSuccess(true);
			j.setMsg("轨道衡RFID卡号绑定成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 查询是否有重复的RFID
	 * @param vehicleInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "repeat")
	public AjaxJson repeat(VehicleInfo vehicleInfo,String flag) {
		AjaxJson j = new AjaxJson();
		VehicleInfo v = vehicleInfoService.get(vehicleInfo.getId());
		try {
		  if(vehicleInfo.getRfidNo() != null && !"".equals(vehicleInfo.getRfidNo().trim())&&"1".equals(flag)){
		  	  	 j.setSuccess(false);
			     return j;
		  }
		 if(vehicleInfo.getSrfidNo() != null && !"".equals(vehicleInfo.getSrfidNo().trim())&&"2".equals(flag)){
			 j.setSuccess(false);
			 return j;
		 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		j.setSuccess(true);
		return j;
	}
}