package com.ai.risk.analysis.modules.warning.service.impl;

import com.ai.risk.analysis.modules.warning.service.AbstractHBaseAccumulator;
import com.ai.risk.analysis.modules.warning.service.IOpcodeSV;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ThinkPad
 */
@Service
public class OpcodeSVImpl extends AbstractHBaseAccumulator implements IOpcodeSV {

    /**
     * 工号调用计数器
     */
    private Map<String, AtomicLong> localCounts = new ConcurrentHashMap<>(1024 * 1024);

    /**
     * 工号调用次数统计表
     */
    @Value("${spring.hbase.opcode-table}")
    private String opCodeTable;

    /**
     * 递增调用次数
     *
     * @param opCode
     */
    @Override
    public void increment(String opCode) {
        AtomicLong count = localCounts.get(opCode);
        if (null == count) {
            count = new AtomicLong(0L);
            localCounts.put(opCode, count);
        }
        count.incrementAndGet();
    }

    @Override
    public void sinkToHBase(String time) {
        Map<String, AtomicLong> oldLocalCounts = localCounts;
        this.localCounts = new ConcurrentHashMap<>(1024 * 1024);
        sinkToHBase(opCodeTable, time, oldLocalCounts);
    }

}
