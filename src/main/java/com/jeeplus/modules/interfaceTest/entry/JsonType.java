package com.jeeplus.modules.interfaceTest.entry;


import net.sf.json.JSONArray;

/**
 * json接受类型
 */
public class JsonType<T>  {


    private JSONArray Data;
    private String Type;
    private String Flag;


    public JSONArray getData() {
        return Data;
    }

    public void setData(JSONArray data) {
        Data = data;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }
}
