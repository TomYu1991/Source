package com.jeeplus.modules.interfaceTest.entry;

/**
 * 出门条反馈
 */
public class PassFeedBack {

    private String TRNP_APP_NO  						;//出门条单号
    private String DEAL_PERSON_NO  						;//出厂处理人工号
    private String DEAL_PERSON_NAME  					;//出厂处理人姓名
    private String DEAL_DATE 							;//出厂时间


    public String getTRNP_APP_NO() {
        return TRNP_APP_NO;
    }

    public void setTRNP_APP_NO(String TRNP_APP_NO) {
        this.TRNP_APP_NO = TRNP_APP_NO;
    }

    public String getDEAL_PERSON_NO() {
        return DEAL_PERSON_NO;
    }

    public void setDEAL_PERSON_NO(String DEAL_PERSON_NO) {
        this.DEAL_PERSON_NO = DEAL_PERSON_NO;
    }

    public String getDEAL_PERSON_NAME() {
        return DEAL_PERSON_NAME;
    }

    public void setDEAL_PERSON_NAME(String DEAL_PERSON_NAME) {
        this.DEAL_PERSON_NAME = DEAL_PERSON_NAME;
    }

    public String getDEAL_DATE() {
        return DEAL_DATE;
    }

    public void setDEAL_DATE(String DEAL_DATE) {
        this.DEAL_DATE = DEAL_DATE;
    }
}
