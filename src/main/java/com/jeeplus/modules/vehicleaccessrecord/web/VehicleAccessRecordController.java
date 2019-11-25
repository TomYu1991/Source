/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.vehicleaccessrecord.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.PropertiesLoader;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.service.VehicleAccessRecordService;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import com.jeeplus.modules.vehicleinfo.service.VehicleInfoService;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
import com.jeeplus.modules.warninginfo.service.WarningInfoService;
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
 * 车辆进出记录Controller
 * @author 汤进国
 * @version 2019-01-18
 */
@Controller
@RequestMapping(value = "${adminPath}/vehicleaccessrecord/vehicleAccessRecord")
public class VehicleAccessRecordController extends BaseController {

	@Autowired
	private VehicleAccessRecordService vehicleAccessRecordService;
	@Autowired
	private WarningInfoService warningInfoService;
    @Autowired
    private VehicleInfoService vehicleInfoService;
    @Autowired
    private ConsignService consignService;
	//工作站
	@Autowired
	private WorkStationService workStationService;
	/**
	 * 查看，增加，编辑车辆违章信息表单页面
	 */
	@RequestMapping(value = "peccancyForm")
	public String peccancyForm(VehicleAccessRecord vehicleaccessRecord, Model model) {

		model.addAttribute("vehicleAccessRecord", vehicleaccessRecord);
		return "modules/vehicleaccessrecord/peccancyForm";
	}

	/**
	 * 保存车辆违章信息
	 */
	@ResponseBody
	@RequestMapping(value = "savePeccancyInfo")
	public AjaxJson savePeccancyInfo(VehicleAccessRecord vehicleaccessRecord, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(vehicleaccessRecord);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}

		//存入违章信息，标记违章
		vehicleaccessRecord.setPeccancy("1");
		vehicleAccessRecordService.savePeccancyInfo(vehicleaccessRecord);//保存

		//存入违章记录表
		if(vehicleaccessRecord != null){
			WarningInfo war = new WarningInfo();
			war.setVehicleNo(vehicleaccessRecord.getVehicleNo());
			war.setReason(vehicleaccessRecord.getRemarks());
			war.setState("1");
			war.setCreateDate(new Date());
			war.setCreateBy(UserUtils.getUser());
			war.setDataType("0");
			war.setDelFlag("0");
			//查询车辆所属单位
          List<VehicleInfo> vi =  vehicleInfoService.checkByVehicleNo(vehicleaccessRecord.getVehicleNo());
           if(vi.size()>0){
               war.setCompanyCname(vi.get(0).getCompanyCname());
           }else{
               List<Consign> ci= consignService.queryConsignByVehicleNo(vehicleaccessRecord.getVehicleNo());
                if(ci.size()>0){
                    war.setCompanyCname(ci.get(0).getCarryCompanyName());
                }
           }
			war.setId(IdGen.uuid());
			if( null != vehicleaccessRecord.getPic() && !"".equals(vehicleaccessRecord.getPic().trim())){
				PropertiesLoader pro = new PropertiesLoader("/properties/config.properties");

				String http = "http://";
				String hostip = pro.getProperty("hostip") + "";
				String hostport = pro.getProperty("hostport") + "";

				String picpath = vehicleaccessRecord.getPic();

				//String picpa =picpath.substring(2);

				String pic = http + hostip + ":" + hostport + picpath;
				war.setPic( pic);
			}
			warningInfoService.insertInter(war);
			InterfaceTest ift = new InterfaceTest();
			ift.InterWarn(war);
		}

		j.setMsg("保存车辆违章记录成功");
		j.setSuccess(true);
		return j;

	}
	
	/**
	 * 车辆进出记录列表页面
	 */
	@RequiresPermissions("vehicleaccessrecord:vehicleAccessRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list(VehicleAccessRecord vehicleaccessRecord, Model model) {
		WorkStation w = new WorkStation();
		w.setType("1");
		List<WorkStation> list =workStationService.findList(w);
		model.addAttribute("list",list);
		model.addAttribute("vehicleAccessRecord", vehicleaccessRecord);
		return "modules/vehicleaccessrecord/vehicleAccessRecordList";
	}
	
		/**
	 * 车辆进出记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("vehicleaccessrecord:vehicleAccessRecord:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(VehicleAccessRecord vehicleaccessRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VehicleAccessRecord> page = new Page<>();
		if(vehicleaccessRecord.getSearchFlag()!=null&&"1".equals(vehicleaccessRecord.getSearchFlag())){
			page = vehicleAccessRecordService.findPage(new Page<VehicleAccessRecord>(request, response), vehicleaccessRecord);
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑车辆进出记录表单页面
	 */
	@RequiresPermissions(value={"vehicleaccessrecord:vehicleAccessRecord:view","vehicleaccessrecord:vehicleAccessRecord:add","vehicleaccessrecord:vehicleAccessRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(VehicleAccessRecord vehicleaccessRecord, Model model) {
		model.addAttribute("vehicleAccessRecord", vehicleaccessRecord);
		return "modules/vehicleaccessrecord/vehicleAccessRecordForm";
	}

	/**
	 * 保存车辆进出记录
	 */
	@ResponseBody
	@RequiresPermissions(value={"vehicleaccessrecord:vehicleAccessRecord:add","vehicleaccessrecord:vehicleAccessRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(VehicleAccessRecord vehicleaccessRecord, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(vehicleaccessRecord);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		vehicleAccessRecordService.save(vehicleaccessRecord);//保存
		j.setSuccess(true);
		j.setMsg("保存车辆进出记录成功");
		return j;
	}
	
	/**
	 * 删除车辆进出记录
	 */
	@ResponseBody
	@RequiresPermissions("vehicleaccessrecord:vehicleAccessRecord:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(VehicleAccessRecord vehicleaccessRecord) {
		AjaxJson j = new AjaxJson();
		vehicleAccessRecordService.delete(vehicleaccessRecord);
		j.setMsg("删除车辆进出记录成功");
		return j;
	}
	
	/**
	 * 批量删除车辆进出记录
	 */
	@ResponseBody
	@RequiresPermissions("vehicleaccessrecord:vehicleAccessRecord:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			vehicleAccessRecordService.delete(vehicleAccessRecordService.get(id));
		}
		j.setMsg("删除车辆进出记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("vehicleaccessrecord:vehicleAccessRecord:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(VehicleAccessRecord vehicleaccessRecord, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {

			String  param=URLDecoder.decode(request.getParameter("data"),"UTF-8");
			JSONObject jo = JSONObject.fromObject(param);

            if(jo.get("beginintoTime")==null||"".equals(jo.get("beginintoTime"))){
				jo.remove("beginintoTime");
			}
			if(jo.get("endintoTime")==null||"".equals(jo.get("endintoTime"))){
				jo.remove("endintoTime");
			}
			if(jo.get("beginoutTime")==null||"".equals(jo.get("beginoutTime"))){
				jo.remove("beginoutTime");
			}
			if(jo.get("endoutTime")==null||"".equals(jo.get("endoutTime"))){
				jo.remove("endoutTime");
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(VehicleAccessRecord.class);
			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

			vehicleaccessRecord = (VehicleAccessRecord)JSONObject.toBean(jo,VehicleAccessRecord.class);

			String fileName = "车辆进出记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<VehicleAccessRecord> page = vehicleAccessRecordService.findPage(new Page<VehicleAccessRecord>(request, response, -1), vehicleaccessRecord);
    		new ExportExcel("车辆进出记录", VehicleAccessRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车辆进出记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("vehicleaccessrecord:vehicleAccessRecord:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<VehicleAccessRecord> list = ei.getDataList(VehicleAccessRecord.class);
			for (VehicleAccessRecord vehicleAccessRecord : list){
				try{
					vehicleAccessRecordService.save(vehicleAccessRecord);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车辆进出记录记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条车辆进出记录记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入车辆进出记录失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入车辆进出记录数据模板
	 */
	@ResponseBody
	@RequiresPermissions("vehicleaccessrecord:vehicleAccessRecord:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车辆进出记录数据导入模板.xlsx";
    		List<VehicleAccessRecord> list = Lists.newArrayList();
    		new ExportExcel("车辆进出记录数据", VehicleAccessRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }




}