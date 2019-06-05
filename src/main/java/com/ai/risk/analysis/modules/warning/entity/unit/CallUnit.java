package com.ai.risk.analysis.modules.warning.entity.unit;

import lombok.Data;

/**
 * 调用单元
 *
 * @author lijun17
 * @date 2019-06-03
 */
@Data
public class CallUnit {

    /**
     * 调用名
     */
    private String name;

    /**
     * 被调用次数
     */
    private long count;

    public CallUnit(String name, long count) {
        this.name = name;
        this.count = count;
    }

}
