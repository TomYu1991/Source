package com.jeeplus.modules.station.entity;

import java.io.Serializable;

/**
 * 接收设备position信息
 */
public class Device implements Serializable {
    private static final long serialVersionUID = -5971786101947261282L;

    private String pos;//道闸位置
    private String cp;//车号识别位置
    private String led;//LED位置
    private String hld;//红绿灯位置
    private String positionStr;//IC读卡器位置
    private String rfidStr;//读卡器位置
    private String spzp;//视频抓拍位置


    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getLed() {
        return led;
    }

    public void setLed(String led) {
        this.led = led;
    }

    public String getHld() {
        return hld;
    }

    public void setHld(String hld) {
        this.hld = hld;
    }

    public String getPositionStr() {
        return positionStr;
    }

    public void setPositionStr(String positionStr) {
        this.positionStr = positionStr;
    }

    public String getRfidStr() {
        return rfidStr;
    }

    public void setRfidStr(String rfidStr) {
        this.rfidStr = rfidStr;
    }

    public String getSpzp() {
        return spzp;
    }

    public void setSpzp(String spzp) {
        this.spzp = spzp;
    }
}
