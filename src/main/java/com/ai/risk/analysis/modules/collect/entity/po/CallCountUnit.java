package com.ai.risk.analysis.modules.collect.entity.po;

import lombok.Data;

/**
 * 调用次数单元
 *
 * @author lijun17
 * @date 2019-06-03
 */
@Data
public class CallCountUnit {

    /**
     * 调用名
     */
    private String name;

    /**
     * 调用次数
     */
    private long count;

}
