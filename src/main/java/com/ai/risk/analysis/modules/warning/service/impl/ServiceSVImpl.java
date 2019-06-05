package com.ai.risk.analysis.modules.warning.service.impl;

import com.ai.risk.analysis.modules.warning.entity.unit.CallUnit;
import com.ai.risk.analysis.modules.warning.service.AbstractHBaseAccumulator;
import com.ai.risk.analysis.modules.warning.service.IServiceSV;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ThinkPad
 */
@Service
public class ServiceSVImpl extends AbstractHBaseAccumulator implements IServiceSV {

    /**
     * 服务调用计数器
     */
    private Map<String, AtomicLong> localCounts = new ConcurrentHashMap<>(1024 * 1024);

    /**
     * 服务调用次数统计表
     */
    @Value("${spring.hbase.service-table}")
    private String serviceTable;

    /**
     * 递增调用次数
     *
     * @param svcName
     */
    @Override
    public void increment(String svcName) {
        AtomicLong count = localCounts.get(svcName);
        if (null == count) {
            count = new AtomicLong(0L);
            localCounts.put(svcName, count);
        }
        count.incrementAndGet();
    }

    @Override
    public void sinkToHBase(String timestamp) {
        Map<String, AtomicLong> oldLocalCounts = localCounts;
        this.localCounts = new ConcurrentHashMap<>(1024 * 1024);
        sinkToHBase(serviceTable, timestamp, oldLocalCounts);
    }


}
