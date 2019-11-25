/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.videos.entity;

import com.jeeplus.modules.station.entity.WorkStation;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 视频配置管理Entity
 * @author zhanglumeng 
 * @version 2019-02-22
 */
public class Videos extends DataEntity<Videos> {
	
	private static final long serialVersionUID = 1L;
	private String cameraName;		// 摄像机名
	private String cameraIp;		// 摄像机IP
	private String cameraPort;		// 摄像机端口号
	private String resolution;		// 分辨率
	private String frames;		// 帧数
	private String cameraType;		// 种类
	private WorkStation station;		// 工作站
	private String username;		// 用户名
	private String password;		// 密码
	private String cameraOrder;		// 显示顺序
	private String isNormal;		// 是否正常
	private String videoGroup;		// 视频组端口号
	private String videoCamera;		// 视频通道
	private String videoMode;		// 视频模式
	private String inputDate;		// 录入时间
	private String inputer;		// 录入人
	private String cameraNo;		// 摄像头的编码
	private String flag;		// 是否有效标志
	private String orderFlag;		// 排序标志
	private String institution;   //单位
    private String stationIp;

    public String getStationIp() {
        return stationIp;
    }
    public void setStationIp(String stationIp) {
        this.stationIp = stationIp;
    }
    public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public Videos() {
		super();
	}

	public Videos(String id){
		super(id);
	}

	@ExcelField(title="摄像机名", align=2, sort=7)
	public String getCameraName() {
		return cameraName;
	}

	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}
	
	@ExcelField(title="摄像机IP", align=2, sort=8)
	public String getCameraIp() {
		return cameraIp;
	}

	public void setCameraIp(String cameraIp) {
		this.cameraIp = cameraIp;
	}
	
	@ExcelField(title="摄像机端口号", align=2, sort=9)
	public String getCameraPort() {
		return cameraPort;
	}

	public void setCameraPort(String cameraPort) {
		this.cameraPort = cameraPort;
	}
	
	@ExcelField(title="分辨率", align=2, sort=10)
	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	
	@ExcelField(title="帧数", align=2, sort=11)
	public String getFrames() {
		return frames;
	}

	public void setFrames(String frames) {
		this.frames = frames;
	}
	
	@ExcelField(title="种类", dictType="camera_type", align=2, sort=12)
	public String getCameraType() {
		return cameraType;
	}

	public void setCameraType(String cameraType) {
		this.cameraType = cameraType;
	}
	
	@NotNull(message="工作站不能为空")
	@ExcelField(title="工作站", dictType="", align=2, sort=13)
	public WorkStation getStation() {
		return station;
	}

	public void setStation(WorkStation station) {
		this.station = station;
	}
	
	@ExcelField(title="用户名", align=2, sort=14)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@ExcelField(title="密码", align=2, sort=15)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@ExcelField(title="显示顺序", align=2, sort=16)
	public String getCameraOrder() {
		return cameraOrder;
	}

	public void setCameraOrder(String cameraOrder) {
		this.cameraOrder = cameraOrder;
	}
	
	@ExcelField(title="是否正常", dictType="is_normal", align=2, sort=17)
	public String getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(String isNormal) {
		this.isNormal = isNormal;
	}
	
	@ExcelField(title="视频组端口号", align=2, sort=18)
	public String getVideoGroup() {
		return videoGroup;
	}

	public void setVideoGroup(String videoGroup) {
		this.videoGroup = videoGroup;
	}
	
	@ExcelField(title="视频通道", align=2, sort=19)
	public String getVideoCamera() {
		return videoCamera;
	}

	public void setVideoCamera(String videoCamera) {
		this.videoCamera = videoCamera;
	}
	
	@ExcelField(title="视频模式", align=2, sort=20)
	public String getVideoMode() {
		return videoMode;
	}

	public void setVideoMode(String videoMode) {
		this.videoMode = videoMode;
	}
	
	@ExcelField(title="录入时间", align=2, sort=21)
	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
	
	@ExcelField(title="录入人", align=2, sort=22)
	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}
	
	@ExcelField(title="摄像头的编码", align=2, sort=23)
	public String getCameraNo() {
		return cameraNo;
	}

	public void setCameraNo(String cameraNo) {
		this.cameraNo = cameraNo;
	}
	
	@ExcelField(title="是否有效标志", align=2, sort=24)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@ExcelField(title="排序标志", align=2, sort=25)
	public String getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(String orderFlag) {
		this.orderFlag = orderFlag;
	}
	
}