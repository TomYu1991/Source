/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.passcheck.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.passchecksub.entity.PassCheckSub;
import net.sf.json.JSONArray;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 出门条信息Entity
 * @author 汤进国
 * @version 2019-01-17
 */
public class PassCheck extends DataEntity<PassCheck> {
	
	private static final long serialVersionUID = 1L;
    private String id;
	private String trnpAppNo;		// 出门条号
	private String userCode;		// 用户代码
	private String userName;		// 用户姓名
	private String vehicleNo;		// 车牌号
	private String transContactPerson;		// 司机
	private String deptCode;		// 签发部门代码
	private String depName;		// 签发部门名称
	private String dealPersonNo;		// 签发人员工号
	private String dealPersonName;		// 签发人员姓名
	private Date dealDate;		// 签发日期
	private String rfidNo;		// RFID卡号
	private String passCode;		// 放行码
	private String archiveFlag;		// 归档标记
	private String companyCode;		// 公司代码
	private String companyCname;		// 公司中文名称
	private String typeCode;		// 类别代码
	private String typeName;		// 类别
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String carryCompanyName;		// 承运公司名称
	private Date applyTime;		// 申请时刻
	private String applyPersonNo;		// 申请人工号
	private Date approveTime;		// 审批时间
	private String approvePersonNo;		// 审批人工号
	private String validFlag;		// 生效标记
	private String feedbackFlag;		// 反馈标记
	private String feebackContent;		// 反馈内容
	private String remark;		// 备注
	private Date beginDealDate;		// 开始 签发日期
	private Date endDealDate;		// 结束 签发日期
	private JSONArray subList;   //委托单明细列表
	private List<PassCheckSub> PassSubList;   //委托单明细列表
    private Date identTime;   //识别时间
    private String delFlag;   //逻辑删除
	private String searchFlag;
	private String msg;
	private String errorCode;
	private String type;
    private boolean success;
    private Date createDate;
    private String dataType;


	@Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String getDelFlag() {
		return delFlag;
	}
	@Override
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public JSONArray getSubList() {
		return subList;
	}

	public void setSubList(JSONArray subList) {
		this.subList = subList;
	}

	public List<PassCheckSub> getPassSubList() {
		return PassSubList;
	}

	public void setPassSubList(List<PassCheckSub> passSubList) {
		PassSubList = passSubList;
	}

	public PassCheck() {
		super();
	}

	public PassCheck(String id){
		super(id);
	}

	@ExcelField(title="出门条号", align=2, sort=1)
	public String getTrnpAppNo() {
		return trnpAppNo;
	}

	public void setTrnpAppNo(String trnpAppNo) {
		this.trnpAppNo = trnpAppNo;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	@ExcelField(title="用户姓名", align=2, sort=2)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@ExcelField(title="车牌号", align=2, sort=3)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="司机", align=2, sort=4)
	public String getTransContactPerson() {
		return transContactPerson;
	}

	public void setTransContactPerson(String transContactPerson) {
		this.transContactPerson = transContactPerson;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	@ExcelField(title="签发部门名称", align=2, sort=5)
	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	@ExcelField(title="签发人员工号", align=2, sort=6)
	public String getDealPersonNo() {
		return dealPersonNo;
	}

	public void setDealPersonNo(String dealPersonNo) {
		this.dealPersonNo = dealPersonNo;
	}
	
	@ExcelField(title="签发人员姓名", align=2, sort=7)
	public String getDealPersonName() {
		return dealPersonName;
	}

	public void setDealPersonName(String dealPersonName) {
		this.dealPersonName = dealPersonName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="签发日期", align=2, sort=8)
	public Date getDealDate() {
		return dealDate;
	}

	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}
	
	@ExcelField(title="RFID卡号", align=2, sort=9)
	public String getRfidNo() {
		return rfidNo;
	}

	public void setRfidNo(String rfidNo) {
		this.rfidNo = rfidNo;
	}
	
	@ExcelField(title="放行码", align=2, sort=10)
	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	public String getArchiveFlag() {
		return archiveFlag;
	}

	public void setArchiveFlag(String archiveFlag) {
		this.archiveFlag = archiveFlag;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	@ExcelField(title="公司中文名称", align=2, sort=11)
	public String getCompanyCname() {
		return companyCname;
	}

	public void setCompanyCname(String companyCname) {
		this.companyCname = companyCname;
	}

	@ExcelField(title="车辆类别",dictType = "vehicle_type",align=2, sort=12)
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始时间", align=2, sort=13)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=14)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@ExcelField(title="承运公司名称", align=2, sort=15)
	public String getCarryCompanyName() {
		return carryCompanyName;
	}

	public void setCarryCompanyName(String carryCompanyName) {
		this.carryCompanyName = carryCompanyName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="申请时刻", align=2, sort=16)
	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	
	@ExcelField(title="申请人工号", align=2, sort=17)
	public String getApplyPersonNo() {
		return applyPersonNo;
	}

	public void setApplyPersonNo(String applyPersonNo) {
		this.applyPersonNo = applyPersonNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审批时间", align=2, sort=18)
	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}
	
	@ExcelField(title="审批人工号", align=2, sort=19)
	public String getApprovePersonNo() {
		return approvePersonNo;
	}

	public void setApprovePersonNo(String approvePersonNo) {
		this.approvePersonNo = approvePersonNo;
	}
	
	@ExcelField(title="生效标记",dictType = "pass_check_effect",align=2, sort=20)
	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getFeedbackFlag() {
		return feedbackFlag;
	}

	public void setFeedbackFlag(String feedbackFlag) {
		this.feedbackFlag = feedbackFlag;
	}

	public String getFeebackContent() {
		return feebackContent;
	}

	public void setFeebackContent(String feebackContent) {
		this.feebackContent = feebackContent;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@ExcelField(title="识别时间", align=2, sort=22)
	public Date getIdentTime() {
		return identTime;
	}

	public void setIdentTime(Date identTime) {
		this.identTime = identTime;
	}

	@ExcelField(title="备注", align=2, sort=23)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginDealDate() {
		return beginDealDate;
	}

	public void setBeginDealDate(Date beginDealDate) {
		this.beginDealDate = beginDealDate;
	}
	
	public Date getEndDealDate() {
		return endDealDate;
	}

	public void setEndDealDate(Date endDealDate) {
		this.endDealDate = endDealDate;
	}
		
}