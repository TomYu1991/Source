/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.accessreport.web;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.accessreport.entity.QueryResult;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.illegalinfo.entity.IllegalInfo;
import com.jeeplus.modules.illegalinfo.service.IllegalInfoService;
import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.passcheck.service.PassCheckService;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.userpasscord.service.UserPassRecordService;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.service.VehicleAccessRecordService;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
import com.jeeplus.modules.warninginfo.service.WarningInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 安环报表Controller
 * @author
 * @version 2019-03-16
 */
@Controller
@RequestMapping(value = "${adminPath}/accessreport/accessreport/")
public class AccessReportController extends BaseController {
	@Autowired
	private PassCheckService passCheckService;
	@Autowired
	private ConsignService consignService;
	@Autowired
	private UserPassRecordService userPassRecordService;
	//工作站
	@Autowired
	private WorkStationService workStationService;
	//车辆通行记录
	@Autowired
	private VehicleAccessRecordService vehicleAccessRecordService;
	//违章记录
	@Autowired
	private WarningInfoService warningInfoService;
	@Autowired
	private IllegalInfoService illegalInfoService;

	@ModelAttribute
	public UserPassRecord get(@RequestParam(required = false) String id) {
		UserPassRecord entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = userPassRecordService.get(id);
		}
		if (entity == null) {
			entity = new UserPassRecord();
		}
		return entity;
	}

	/**
	 * 安环报表展示页面
	 */
	@RequiresPermissions("accessreport:accessreport:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		QueryResult queryResult = new QueryResult();
		model.addAttribute("queryResult", queryResult);
		return "modules/accessreport/accessReportList";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(HttpServletRequest request, Model model) {
		String type = request.getParameter("type");
		String date = request.getParameter("date");
		Date beginTime = null;
		Date endTime = null;
		String begin = date + " 00:00:00";
		String end = date + " 23:59:59";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if ("day".equals(type)) {
			try {
				beginTime = simpleDateFormat.parse(begin);
				endTime = simpleDateFormat.parse(end);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if ("month".equals(type)) {
			try {
				beginTime = simpleDateFormat.parse(begin);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(beginTime);
				int month = calendar.get(Calendar.MONTH)+1;
				System.out.println("-----------month:"+month);
				int year = calendar.get(Calendar.YEAR);
				System.out.println("-----------year:"+year);
				//本月第一天
				String f = getFisrtDayOfMonth(year,month)+" 00:00:00";
				beginTime = simpleDateFormat.parse(f);
				System.out.println("---------本月第一天："+f);
				//本月最后一天
				String l = getLastDayOfMonth(year,month)+" 23:59:59";
				endTime = simpleDateFormat.parse(l);
				System.out.println("---------本月最后一天："+l);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if ("quarter".equals(type)) {
			try {
				beginTime = simpleDateFormat.parse(begin);
				int quarter = getQuarter(beginTime);
				String[] days = getCurrQuarter(quarter);
				beginTime = simpleDateFormat.parse(days[0]+" 00:00:00");
				endTime = simpleDateFormat.parse(days[1]+" 23:59:59");
				System.out.println("------------本季第一天："+beginTime);
				System.out.println("------------本季最后一天："+endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//进门车辆
		VehicleAccessRecord vehicleIn = new VehicleAccessRecord();
		vehicleIn.setBeginintoTime(beginTime);
		vehicleIn.setEndintoTime(endTime);
		int l1= vehicleAccessRecordService.getVehicleInCount(vehicleIn);
		//出门车辆
		VehicleAccessRecord vehicleOut = new VehicleAccessRecord();
		vehicleOut.setBeginoutTime(beginTime);
		vehicleOut.setEndoutTime(endTime);
		int l2 = vehicleAccessRecordService.getVehicleOutCount(vehicleOut);
		//预约车辆
		VehicleAccessRecord vehicleConsign = new VehicleAccessRecord();
		vehicleConsign.setBeginintoTime(beginTime);
		vehicleConsign.setEndintoTime(endTime);
		int l3 = vehicleAccessRecordService.getConsignVehicle(vehicleConsign);
		//预约入厂车辆
		VehicleAccessRecord vehicleIntoFactory = new VehicleAccessRecord();
		vehicleIntoFactory.setBeginintoTime(beginTime);
		vehicleIntoFactory.setEndintoTime(endTime);
		List<VehicleAccessRecord> l4 = vehicleAccessRecordService.getIntoFactoryVehicleList(vehicleIntoFactory);
		//进门手动放行
		VehicleAccessRecord record = new VehicleAccessRecord();
		record.setBeginintoTime(beginTime);
		record.setEndintoTime(endTime);
		List<VehicleAccessRecord> l5 = vehicleAccessRecordService.getVehicleManual(record);
		//已批过夜车辆
		WarningInfo warn1 = new WarningInfo();
		warn1.setStartTime(beginTime);
		warn1.setEndTime(endTime);
		List<WarningInfo> l6 = warningInfoService.getApproveVehicle(warn1);
		//未批过夜车辆
		WarningInfo warn2 = new WarningInfo();
		warn2.setStartTime(beginTime);
		warn2.setEndTime(endTime);
		List<WarningInfo> l7 = warningInfoService.getUnapproveVehicle(warn2);
		//违章车辆
		WarningInfo warn3 = new WarningInfo();
		warn3.setEndTime(endTime);
		warn3.setStartTime(beginTime);
		List<WarningInfo> l8=warningInfoService.getWarningInfoVehicles(warn3);
		//进门人员
		UserPassRecord r = new UserPassRecord();
		r.setBeginpassTime(beginTime);
		r.setEndpassTime(endTime);
		int l9 = userPassRecordService.getUserInCount(r);
		//出门人员
		UserPassRecord userPassRecord = new UserPassRecord();
		userPassRecord.setBeginpassTime(beginTime);
		userPassRecord.setEndpassTime(endTime);
		int l10 = userPassRecordService.getUserOutCount(userPassRecord);
		//预约人员
		UserPassRecord r2 = new UserPassRecord();
		r2.setBeginpassTime(beginTime);
		r2.setEndpassTime(endTime);
		int l11 = userPassRecordService.getConsignUserCount(r2);
		//预约入厂人员
		UserPassRecord r3 = new UserPassRecord();
		r3.setBeginpassTime(beginTime);
		r3.setEndpassTime(endTime);
		int l12 = userPassRecordService.getIntoFactoryUserList(r3);
		//出门条
		PassCheck passCheck1 = new PassCheck();
		passCheck1.setBeginDealDate(beginTime);
		passCheck1.setEndDealDate(endTime);
		List<PassCheck> l13= passCheckService.getVehiclePassCheck(passCheck1);
		//出门条未出厂
		PassCheck passCheck2 = new PassCheck();
		passCheck2.setBeginDealDate(beginTime);
		passCheck2.setEndDealDate(endTime);
		List<PassCheck> l14=passCheckService.getVehiclePassCheckU(passCheck2);
		//重要部位闯入
		IllegalInfo illegal = new IllegalInfo();
		illegal.setBegiPassTime(beginTime);
		illegal.setEndPassTime(endTime);
		List<IllegalInfo> l15 = illegalInfoService.findList(illegal);

		Map<String, Object> map = new HashMap<>();
		Map<String, Integer> m = new HashMap<>();
		List dataList = new ArrayList();
		m.put("vehicleIn", l1);
		m.put("vehicleOut", l2);
		m.put("vehicleConsign", l3);
		m.put("vehicleToFactory", l4.size());
		m.put("vehicleManual", l5.size());
		m.put("approveVehicle", l6.size());
		m.put("unapproveVehicle", l7.size());
		m.put("illegalVehicle", l8.size());
		m.put("userIn", l9);
		m.put("userOut", l10);
		m.put("userConsign", l11);
		m.put("userToFactory", l12);
		m.put("gatePass", l13.size());
		m.put("gatePassu", l14.size());
		m.put("breakIn", l15.size());
		dataList.add(m);
		map.put("total", 1);
		map.put("rows", dataList);
		return map;
	}

	//获取某年某月的第一天
	public static String getFisrtDayOfMonth(int year,int month)
	{
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最小天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = sdf.format(cal.getTime());
		return firstDayOfMonth;
	}
	//获取某年某月的最后一天
	public static String getLastDayOfMonth(int year,int month)
	{
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}
	//获取季度
	public static int getQuarter(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(c.MONTH) + 1;
		int quarter = 0;
		if (month >= 1 && month <= 3) {
			quarter = 1;
		} else if (month >= 4 && month <= 6) {
			quarter = 2;
		} else if (month >= 7 && month <= 9) {
			quarter = 3;
		} else {
			quarter = 4;
		}
		return quarter;
	}

	public static String[] getCurrQuarter(int num) {
		String[] s = new String[2];
		String str = "";
		// 设置本年的季
		Calendar quarterCalendar = null;
		switch (num) {
			case 1: // 本年到现在经过了一个季度，在加上前4个季度
				quarterCalendar = Calendar.getInstance();
				quarterCalendar.set(Calendar.MONTH, 3);
				quarterCalendar.set(Calendar.DATE, 1);
				quarterCalendar.add(Calendar.DATE, -1);
				str = DateUtils.formatDate(quarterCalendar.getTime(), "yyyy-MM-dd");
				s[0] = str.substring(0, str.length() - 5) + "01-01";
				s[1] = str;
				break;
			case 2: // 本年到现在经过了二个季度，在加上前三个季度
				quarterCalendar = Calendar.getInstance();
				quarterCalendar.set(Calendar.MONTH, 6);
				quarterCalendar.set(Calendar.DATE, 1);
				quarterCalendar.add(Calendar.DATE, -1);
				str = DateUtils.formatDate(quarterCalendar.getTime(), "yyyy-MM-dd");
				s[0] = str.substring(0, str.length() - 5) + "04-01";
				s[1] = str;
				break;
			case 3:// 本年到现在经过了三个季度，在加上前二个季度
				quarterCalendar = Calendar.getInstance();
				quarterCalendar.set(Calendar.MONTH, 9);
				quarterCalendar.set(Calendar.DATE, 1);
				quarterCalendar.add(Calendar.DATE, -1);
				str = DateUtils.formatDate(quarterCalendar.getTime(), "yyyy-MM-dd");
				s[0] = str.substring(0, str.length() - 5) + "07-01";
				s[1] = str;
				break;
			case 4:// 本年到现在经过了四个季度，在加上前一个季度
				quarterCalendar = Calendar.getInstance();
				str = DateUtils.formatDate(quarterCalendar.getTime(), "yyyy-MM-dd");
				s[0] = str.substring(0, str.length() - 5) + "10-01";
				s[1] = str.substring(0, str.length() - 5) + "12-31";
				break;
		}
		return s;
	}

}