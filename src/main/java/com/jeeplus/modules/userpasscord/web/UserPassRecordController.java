/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.userpasscord.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.tools.utils.MultiDBUtils;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.userpasscord.service.UserPassRecordService;
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
import java.util.regex.Pattern;

/**
 * 员工通行记录Controller
 * @author 汤进国
 * @version 2019-03-16
 */
@Controller
@RequestMapping(value = "${adminPath}/userpasscord/userPassRecord")
public class UserPassRecordController extends BaseController {

	@Autowired
	private UserPassRecordService userPassRecordService;
	@Autowired
	private ConsignService consignService;
	//工作站
	@Autowired
	private WorkStationService workStationService;


	@ModelAttribute
	public UserPassRecord get(@RequestParam(required=false) String id) {
		UserPassRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userPassRecordService.get(id);
		}
		if (entity == null){
			entity = new UserPassRecord();
		}
		return entity;
	}
	
	/**
	 * 员工通行记录列表页面
	 */
	@RequiresPermissions("userpasscord:userPassRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list(UserPassRecord userPassRecord, Model model) {

		model.addAttribute("userPassRecord", userPassRecord);
		return "modules/userpasscord/userPassRecordList";
	}
	
		/**
	 * 员工通行记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("userpasscord:userPassRecord:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(UserPassRecord userPassRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<UserPassRecord> page = new Page<>();
        if(userPassRecord.getSearchFlag()!=null&&"1".equals(userPassRecord.getSearchFlag())){
            page = userPassRecordService.findPage(new Page<UserPassRecord>(request, response), userPassRecord);
        }
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑员工通行记录表单页面
	 */
	@RequiresPermissions(value={"userpasscord:userPassRecord:view","userpasscord:userPassRecord:add","userpasscord:userPassRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UserPassRecord userPassRecord, Model model) {
		model.addAttribute("userPassRecord", userPassRecord);
		return "modules/userpasscord/userPassRecordForm";
	}

	/**
	 * 保存员工通行记录
	 */
	@ResponseBody
	@RequiresPermissions(value={"userpasscord:userPassRecord:add","userpasscord:userPassRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(UserPassRecord userPassRecord, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(userPassRecord);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		userPassRecordService.save(userPassRecord);//保存
		j.setSuccess(true);
		j.setMsg("保存员工通行记录成功");
		return j;
	}
	
	/**
	 * 删除员工通行记录
	 */
	@ResponseBody
	@RequiresPermissions("userpasscord:userPassRecord:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(UserPassRecord userPassRecord) {
		AjaxJson j = new AjaxJson();
		userPassRecordService.delete(userPassRecord);
		j.setMsg("删除员工通行记录成功");
		return j;
	}
	
	/**
	 * 批量删除员工通行记录
	 */
	@ResponseBody
	@RequiresPermissions("userpasscord:userPassRecord:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			userPassRecordService.delete(userPassRecordService.get(id));
		}
		j.setMsg("删除员工通行记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("userpasscord:userPassRecord:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(UserPassRecord userPassRecord, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			String  param= URLDecoder.decode(request.getParameter("data"),"UTF-8");
			JSONObject jo = JSONObject.fromObject(param);
			if(jo.get("beginpassTime")==null||"".equals(jo.get("beginpassTime"))){
				jo.remove("beginpassTime");
			}
			if(jo.get("endpassTime")==null||"".equals(jo.get("endpassTime"))){
				jo.remove("endpassTime");
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(UserPassRecord.class);
			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));
			userPassRecord = (UserPassRecord)JSONObject.toBean(jo,UserPassRecord.class);

            String fileName = "员工通行记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserPassRecord> page = userPassRecordService.findPage(new Page<UserPassRecord>(request, response, -1), userPassRecord);
    		new ExportExcel("员工通行记录", UserPassRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出员工通行记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("userpasscord:userPassRecord:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserPassRecord> list = ei.getDataList(UserPassRecord.class);
			for (UserPassRecord userPassRecord : list){
				try{
					userPassRecordService.save(userPassRecord);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条员工通行记录记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条员工通行记录记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入员工通行记录失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入员工通行记录数据模板
	 */
	@ResponseBody
	@RequiresPermissions("userpasscord:userPassRecord:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "员工通行记录数据导入模板.xlsx";
    		List<UserPassRecord> list = Lists.newArrayList(); 
    		new ExportExcel("员工通行记录数据", UserPassRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
	/**
	 * 6号岗亭人行道闸刷卡器
	 */
	@ResponseBody
	@RequestMapping(value = "checkUserAccesspower")
	public AjaxJson checkUserAccesspower(String ickh,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {

			List<UserPassRecord> l =userPassRecordService.findInfoByCard(ickh);
			//如果有，放行
			if(l.size()>0){
				//是否是家属
				String a = l.get(0).getPersonnelId().substring(0,2);
				String b = l.get(0).getPersonnelId().substring(2,3);
				if("JS".equals(a)&&isInteger(b)){
					j.setSuccess(false);
					return j;
				}
				l.get(0).setPassTime(new Date());
				l.get(0).setPassStation(request.getRemoteAddr());
				l.get(0).setCardSerial(ickh);
				userPassRecordService.save(l.get(0));
				j.setSuccess(true);
				return j;
			}
			j.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 6号岗亭人行道闸身份证
	 */
	@ResponseBody
	@RequestMapping(value = "checkUserpower")
	public AjaxJson checkUserpower(String ickh,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			//查询当前门岗放行码
			WorkStation ws = workStationService.queryByStationIp(request.getRemoteAddr());
			if(ws!=null){
				//查询是否有预约
				List<Consign> lc = consignService.findInfoByIdCard(ickh);
				if(lc!=null&&lc.size()>0){
					for(Consign c:lc){
						//判断放行码
						String inPass = ws.getInPassCode();
						String[] vtrs = c.getPassCode().split(",");
						for (String vs : vtrs) {
							if (inPass.equals(vs)) {
								//放行并保存
								UserPassRecord u = new UserPassRecord();
								u.setPassTime(new Date());
								u.setCardSerial(ickh);
								u.setDeptName(c.getConsignDept());
								u.setName(c.getUserName());
								u.setPassStation(request.getRemoteAddr());

								userPassRecordService.save(u);
								j.setSuccess(true);
								j.setMsg("欢迎，预约部门："+c.getConsignDept());
								return j;
							}
						}
						j.setSuccess(false);
						j.setMsg("欢迎，预约部门："+lc.get(0).getConsignDept());
						return j;
					}
				}
			}
			j.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 1/5号岗亭人行道闸
	 */
	@ResponseBody
	@RequestMapping(value = "saveUserAccessRecord")
	public AjaxJson saveUserAccessRecord(String ickh,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {

			List<UserPassRecord> l =userPassRecordService.findInfoByCard(ickh);

			//如果有，放行
			if(l.size()>0){
					//放行//保存记录

				l.get(0).setPassTime(new Date());
				l.get(0).setPassStation(request.getRemoteAddr());
				l.get(0).setCardSerial(ickh);
				userPassRecordService.save(l.get(0));
				j.setMsg("欢迎，"+l.get(0).getPersonnelId());
				j.setSuccess(true);
				return j;
			}

			j.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "saveUserPassInfo")
	public AjaxJson saveUser() {
		AjaxJson j = new AjaxJson();
		UserPassRecordService userPass = SpringContextHolder.getBean(UserPassRecordService.class);
		try {
			System.out.println("更新员工信息："+new Date());
			MultiDBUtils md = MultiDBUtils.get("ryxx");
			//同步磅单信息
			List<UserPassRecord> l = md.queryList(" SELECT p.SMT_PersonnelID personnelId,p.SMT_Name name,p.SMT_Sex sex,d.SMT_DeptName1 deptName,c.SMT_state state,c.SMT_CardSerial cardSerial  \n" +
					"\t  FROM smart_personnel p  left join smart_card c on p.SMT_PersonnelID = c.SMT_PersonnelID   \n" +
					"\t   left join Smart_Dept1 d  on p.SMT_DeptCode1 = d.SMT_DeptCode1  where c.SMT_state =0 ", UserPassRecord.class);

			if(l.size()>0){
				for(UserPassRecord u : l){
					if(u.getCardSerial()!=null&&!"".equals(u.getCardSerial())){
						int len = u.getCardSerial().length();// 取得字符串的长度
						int index = 0;// 预定义第一个非零字符串的位置

						char strs[] = u.getCardSerial().toCharArray();// 将字符串转化成字符数组
						for (int i = 0; i < len; i++) {
							if ('0' != strs[i]) {
								index = i;// 找到非零字符串并跳出
								break;
							}
						}
						String strLast = u.getCardSerial().substring(index, len);// 截取字符串
						u.setCardSerial(strLast);
					}
					u.setId(IdGen.uuid());

					userPass.saveUserPassInfo(u);
				}
				j.setSuccess(true);
				j.setMsg("操作成功");
				return j;
			}
			j.setSuccess(false);
			j.setMsg("操作失败");



		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

}