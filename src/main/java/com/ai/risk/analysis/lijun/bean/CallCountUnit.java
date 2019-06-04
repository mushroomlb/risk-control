package com.ai.risk.analysis.lijun.bean;

/**
 * 调用次数单元
 *
 * @author lijun17
 * @date 2019-06-03
 */
public class CallCountUnit {
    /**
     * 调用名
     */
    private String name;

    /**
     * 调用次数
     */
    private String number;

    /**
     * 服务名或者工号名,1-服务名，2-工号
     */
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
