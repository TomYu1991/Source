/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.check.entity.DeviceCheckRecord;
import com.jeeplus.modules.check.service.DeviceCheckRecordService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 员工通行记录Controller
 * @author 汤进国
 * @version 2019-03-16
 */
@Controller
@RequestMapping(value = "${adminPath}/check/deviceCheckResult")
public class DeviceCheckResultController extends BaseController {

	@Autowired
	private DeviceCheckRecordService deviceCheckRecordService;
	


	@ModelAttribute
	public DeviceCheckRecord get(@RequestParam(required=false) String id) {
		DeviceCheckRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = deviceCheckRecordService.get(id);
		}
		if (entity == null){
			entity = new DeviceCheckRecord();
		}
		return entity;
	}
	
	/**
	 * 员工通行记录列表页面
	 */
	@RequiresPermissions("check:deviceCheckRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list(DeviceCheckRecord deviceCheckRecord, Model model) {
		model.addAttribute("deviceCheckRecord", deviceCheckRecord);
		return "modules/check/deviceCheckResultList";
	}
	
	/**
	 * 员工通行记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("check:DeviceCheckResult:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DeviceCheckRecord deviceCheckRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DeviceCheckRecord> page = new Page<>();
        if(deviceCheckRecord.getSearchFlag()!=null&&"1".equals(deviceCheckRecord.getSearchFlag())){
            page = deviceCheckRecordService.findFinishedPage(new Page<DeviceCheckRecord>(request, response), deviceCheckRecord);
        }
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑员工通行记录表单页面
	 */
	@RequiresPermissions(value={"check:deviceCheckResult:view","check:deviceCheckResult:add","check:deviceCheckResult:edit"},logical= Logical.OR)
	@RequestMapping(value = "editForm")
	public String editForm(DeviceCheckRecord deviceCheckRecord, Model model) {
		model.addAttribute("DeviceCheckRecord", deviceCheckRecord);
		return "modules/check/deviceCheckResultEditForm";
	}

	/*
	*
	* @description 统计报表
	* @author yuzhongtong
	* @date 2019/6/18
	* @time 10:47
	* @param [deviceCheckRecord, model]
	* @return java.lang.String
	*/
	@RequiresPermissions(value={"check:deviceCheckRecord:view","check:deviceCheckRecord:add","check:deviceCheckRecord:edit"},logical= Logical.OR)
	@RequestMapping(value = "report")
	public String report(DeviceCheckRecord deviceCheckRecord, Model model) {
		System.out.println("==========================="+deviceCheckRecord.getStartTime());
		System.out.println("==========================="+deviceCheckRecord.getEndTime());

//		List<DeviceCheckRecord> deviceCheckRecordList = deviceCheckRecordService.getReportList(deviceCheckRecord);
//		model.addAttribute("deviceCheckRecordList", deviceCheckRecordList);
		List<DeviceCheckRecord> deviceCheckRecordList = deviceCheckRecordService.getReportList(deviceCheckRecord);
		List<DeviceCheckRecord> yhb = new ArrayList<>();	//1#磅
		List<DeviceCheckRecord> shb = new ArrayList<>();	//3#磅
		List<DeviceCheckRecord> gdb = new ArrayList<>();	//轨道磅
		List<DeviceCheckRecord> jks = new ArrayList<>();	//集控室
		List<DeviceCheckRecord> aborted = new ArrayList<>();	//取消列表
		for (DeviceCheckRecord record:deviceCheckRecordList
			 ) {
				if("1#磅作业区".equals(record.getWorkingArea()) && "否".equals(record.getIsAborted())){
					yhb.add(record);
				}
				if("3#磅作业区".equals(record.getWorkingArea()) && "否".equals(record.getIsAborted())){
					shb.add(record);
				}
				if("轨道磅作业区".equals(record.getWorkingArea()) && "否".equals(record.getIsAborted())){
					gdb.add(record);
				}
				if("集控室作业区".equals(record.getWorkingArea()) && "否".equals(record.getIsAborted())){
					jks.add(record);
				}
				if("是".equals(record.getIsAborted())){
					aborted.add(record);
				}
		}
		model.addAttribute("yhb",yhb);
		model.addAttribute("shb",shb);
		model.addAttribute("gdb",gdb);
		model.addAttribute("jks",jks);
		model.addAttribute("aborted",aborted);
		model.addAttribute("deviceCheckRecordList",deviceCheckRecordList);
		return "modules/check/deviceCheckResultReport";
	}

	/**
	 * 保存员工修改
	 */
	@ResponseBody
	@RequiresPermissions(value={"check:deviceCheckRecord:add","check:deviceCheckRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(DeviceCheckRecord DeviceCheckRecord, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(DeviceCheckRecord);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		deviceCheckRecordService.updateEdit(DeviceCheckRecord);//保存
		j.setSuccess(true);
		j.setMsg("保存员工通行记录成功");
		return j;
	}

	/**
	 * 回退点检结果
	 */
	@ResponseBody
	@RequiresPermissions("check:deviceCheckRecord:edit")
	@RequestMapping(value = "cancel")
	public AjaxJson cancel(DeviceCheckRecord DeviceCheckRecord) {
		AjaxJson j = new AjaxJson();
		deviceCheckRecordService.cancel(DeviceCheckRecord);
		j.setMsg("点检结果回退成功");
		return j;
	}

	/**
	 * 批量删除员工通行记录
	 */
	@ResponseBody
	@RequiresPermissions("check:deviceCheckRecord:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			deviceCheckRecordService.delete(deviceCheckRecordService.get(id));
		}
		j.setMsg("删除员工通行记录成功");
		return j;
	}

//	/**
//	 * 导出excel文件
//	 */
//	@ResponseBody
//	@RequiresPermissions("userpasscord:DeviceCheckRecord:export")
//    @RequestMapping(value = "export")
//    public AjaxJson exportFile(DeviceCheckRecord DeviceCheckRecord, HttpServletRequest request, HttpServletResponse response) {
//		AjaxJson j = new AjaxJson();
//		try {
//			String  param= URLDecoder.decode(request.getParameter("data"),"UTF-8");
//			JSONObject jo = JSONObject.fromObject(param);
//			if(jo.get("beginpassTime")==null||"".equals(jo.get("beginpassTime"))){
//				jo.remove("beginpassTime");
//			}
//			if(jo.get("endpassTime")==null||"".equals(jo.get("endpassTime"))){
//				jo.remove("endpassTime");
//			}
//			JsonConfig jsonConfig = new JsonConfig();
//			jsonConfig.setRootClass(DeviceCheckRecord.class);
//			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
//			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));
//			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));
//			DeviceCheckRecord = (DeviceCheckRecord)JSONObject.toBean(jo,DeviceCheckRecord.class);
//
//            String fileName = "员工通行记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
//            Page<DeviceCheckRecord> page = DeviceCheckRecordService.findPage(new Page<DeviceCheckRecord>(request, response, -1), DeviceCheckRecord);
//    		new ExportExcel("员工通行记录", DeviceCheckRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
//    		j.setSuccess(true);
//    		j.setMsg("导出成功！");
//    		return j;
//		} catch (Exception e) {
//			j.setSuccess(false);
//			j.setMsg("导出员工通行记录记录失败！失败信息："+e.getMessage());
//		}
//			return j;
//    }
//
//	/**
//	 * 导入Excel数据
//
//	 */
//	@ResponseBody
//	@RequiresPermissions("userpasscord:DeviceCheckRecord:import")
//    @RequestMapping(value = "import")
//   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		try {
//			int successNum = 0;
//			int failureNum = 0;
//			StringBuilder failureMsg = new StringBuilder();
//			ImportExcel ei = new ImportExcel(file, 1, 0);
//			List<DeviceCheckRecord> list = ei.getDataList(DeviceCheckRecord.class);
//			for (DeviceCheckRecord DeviceCheckRecord : list){
//				try{
//					DeviceCheckRecordService.save(DeviceCheckRecord);
//					successNum++;
//				}catch(ConstraintViolationException ex){
//					failureNum++;
//				}catch (Exception ex) {
//					failureNum++;
//				}
//			}
//			if (failureNum>0){
//				failureMsg.insert(0, "，失败 "+failureNum+" 条员工通行记录记录。");
//			}
//			j.setMsg( "已成功导入 "+successNum+" 条员工通行记录记录"+failureMsg);
//		} catch (Exception e) {
//			j.setSuccess(false);
//			j.setMsg("导入员工通行记录失败！失败信息："+e.getMessage());
//		}
//		return j;
//    }
//
//	/**
//	 * 下载导入员工通行记录数据模板
//	 */
//	@ResponseBody
//	@RequiresPermissions("userpasscord:DeviceCheckRecord:import")
//    @RequestMapping(value = "import/template")
//     public AjaxJson importFileTemplate(HttpServletResponse response) {
//		AjaxJson j = new AjaxJson();
//		try {
//            String fileName = "员工通行记录数据导入模板.xlsx";
//    		List<DeviceCheckRecord> list = Lists.newArrayList();
//    		new ExportExcel("员工通行记录数据", DeviceCheckRecord.class, 1).setDataList(list).write(response, fileName).dispose();
//    		return null;
//		} catch (Exception e) {
//			j.setSuccess(false);
//			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
//		}
//		return j;
//    }
//	public static boolean isInteger(String str) {
//		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//		return pattern.matcher(str).matches();
//	}
//	/**
//	 * 6号岗亭人行道闸刷卡器
//	 */
//	@ResponseBody
//	@RequestMapping(value = "checkUserAccesspower")
//	public AjaxJson checkUserAccesspower(String ickh,HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		try {
//
//			List<DeviceCheckRecord> l =DeviceCheckRecordService.findInfoByCard(ickh);
//			//如果有，放行
//			if(l.size()>0){
//				//是否是家属
//				String a = l.get(0).getPersonnelId().substring(0,2);
//				String b = l.get(0).getPersonnelId().substring(2,3);
//				if("JS".equals(a)&&isInteger(b)){
//					j.setSuccess(false);
//					return j;
//				}
//				l.get(0).setPassTime(new Date());
//				l.get(0).setPassStation(request.getRemoteAddr());
//				l.get(0).setCardSerial(ickh);
//				DeviceCheckRecordService.save(l.get(0));
//				j.setSuccess(true);
//				return j;
//			}
//			j.setSuccess(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return j;
//	}
//	/**
//	 * 6号岗亭人行道闸身份证
//	 */
//	@ResponseBody
//	@RequestMapping(value = "checkUserpower")
//	public AjaxJson checkUserpower(String ickh,HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		try {
//			//查询是否有预约
//			List<ConsignTmp> lc = consignService.findInfoByIdCard(ickh);
//			if(lc!=null&&lc.size()>0){
//				//放行并保存
//				DeviceCheckRecord u = new DeviceCheckRecord();
//				u.setPassTime(new Date());
//				u.setCardSerial(ickh);
//				u.setDeptName(lc.get(0).getConsignDept());
//				u.setName(lc.get(0).getUserName());
//				u.setPassStation(request.getRemoteAddr());
//
//				DeviceCheckRecordService.save(u);
//				j.setSuccess(true);
//				j.setMsg("欢迎，预约部门："+lc.get(0).getConsignDept());
//				return j;
//			}
//			j.setSuccess(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return j;
//	}
//	/**
//	 * 1/5号岗亭人行道闸
//	 */
//	@ResponseBody
//	@RequestMapping(value = "saveUserAccessRecord")
//	public AjaxJson saveUserAccessRecord(String ickh,HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		try {
//
//			List<DeviceCheckRecord> l =DeviceCheckRecordService.findInfoByCard(ickh);
//
//			//如果有，放行
//			if(l.size()>0){
//					//放行//保存记录
//
//				l.get(0).setPassTime(new Date());
//				l.get(0).setPassStation(request.getRemoteAddr());
//				l.get(0).setCardSerial(ickh);
//				DeviceCheckRecordService.save(l.get(0));
//				j.setMsg("欢迎，"+l.get(0).getPersonnelId());
//				j.setSuccess(true);
//				return j;
//			}
//
//			//如果没有，查询是否有预约
//			List<ConsignTmp> lc = consignService.findInfoByIdCard(ickh);
//			if(lc!=null&&lc.size()>0){
//				//放行并保存
//				DeviceCheckRecord u = new DeviceCheckRecord();
//				u.setPassTime(new Date());
//				u.setCardSerial(ickh);
//				u.setDeptName(lc.get(0).getDealDept());
//				u.setName(lc.get(0).getUserName());
//				u.setPassStation(request.getRemoteAddr());
//				DeviceCheckRecordService.save(u);
//                j.setMsg("欢迎，预约部门："+lc.get(0).getConsignDept());
//				j.setSuccess(true);
//				return j;
//			}
//
//			j.setMsg("无预约信息");
//			j.setSuccess(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return j;
//	}
//
//	@ResponseBody
//	@RequestMapping(value = "saveUserPassInfo")
//	public AjaxJson saveUser() {
//		AjaxJson j = new AjaxJson();
//		DeviceCheckRecordService userPass = SpringContextHolder.getBean(DeviceCheckRecordService.class);
//		try {
//			userPass.deleteUserPassInfo();
//			System.out.println("更新员工信息："+new Date());
//			MultiDBUtils md = MultiDBUtils.get("ryxx");
//			//同步磅单信息
//			List<DeviceCheckRecord> l = md.queryList(" SELECT p.SMT_PersonnelID personnelId,p.SMT_Name name,p.SMT_Sex sex,d.SMT_DeptName1 deptName,c.SMT_CardSerial cardSerial  \n" +
//					"\t  FROM smart_personnel p  left join smart_card c on p.SMT_PersonnelID = c.SMT_PersonnelID   \n" +
//					"\t   left join Smart_Dept1 d  on p.SMT_DeptCode1 = d.SMT_DeptCode1 ", DeviceCheckRecord.class);
//
//			//如果有，放行
//			if(l.size()>0){
//				for(DeviceCheckRecord u : l){
//
//					u.setId(IdGen.uuid());
//					userPass.saveUserPassInfo(u);
//				}
//				j.setSuccess(true);
//				j.setMsg("操作成功");
//				return j;
//			}
//			j.setSuccess(false);
//			j.setMsg("操作失败");
//
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return j;
//	}

}