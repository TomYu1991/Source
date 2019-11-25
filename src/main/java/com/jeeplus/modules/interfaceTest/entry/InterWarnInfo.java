package com.jeeplus.modules.interfaceTest.entry;

/**
 * 违章信息接口PO
 */
public class InterWarnInfo {
    private String ROWGUID      	;//	GUID
    private String VEHICLE_NO  		;//	车牌号
    private String STATUS   		;//	处理状态
    private String INPUT_PERSON_NO  ;//	创建人
    private String INPUT_DATE  		;//	创建时间
    private String RMA_CAUSE  		;//	违章原因
    private String DEAL_PERSON_NO  	;//	解除人
    private String DEAL_DATE  		;//	解除时间
    private String DEAL_DESC  		;//	解除理由


    public String getROWGUID() {
        return ROWGUID;
    }

    public void setROWGUID(String ROWGUID) {
        this.ROWGUID = ROWGUID;
    }

    public String getVEHICLE_NO() {
        return VEHICLE_NO;
    }

    public void setVEHICLE_NO(String VEHICLE_NO) {
        this.VEHICLE_NO = VEHICLE_NO;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getINPUT_PERSON_NO() {
        return INPUT_PERSON_NO;
    }

    public void setINPUT_PERSON_NO(String INPUT_PERSON_NO) {
        this.INPUT_PERSON_NO = INPUT_PERSON_NO;
    }

    public String getINPUT_DATE() {
        return INPUT_DATE;
    }

    public void setINPUT_DATE(String INPUT_DATE) {
        this.INPUT_DATE = INPUT_DATE;
    }

    public String getRMA_CAUSE() {
        return RMA_CAUSE;
    }

    public void setRMA_CAUSE(String RMA_CAUSE) {
        this.RMA_CAUSE = RMA_CAUSE;
    }

    public String getDEAL_PERSON_NO() {
        return DEAL_PERSON_NO;
    }

    public void setDEAL_PERSON_NO(String DEAL_PERSON_NO) {
        this.DEAL_PERSON_NO = DEAL_PERSON_NO;
    }

    public String getDEAL_DATE() {
        return DEAL_DATE;
    }

    public void setDEAL_DATE(String DEAL_DATE) {
        this.DEAL_DATE = DEAL_DATE;
    }

    public String getDEAL_DESC() {
        return DEAL_DESC;
    }

    public void setDEAL_DESC(String DEAL_DESC) {
        this.DEAL_DESC = DEAL_DESC;
    }
}
