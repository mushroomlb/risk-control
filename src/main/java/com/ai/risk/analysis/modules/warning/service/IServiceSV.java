package com.ai.risk.analysis.modules.warning.service;


import com.ai.risk.analysis.modules.warning.entity.unit.CallUnit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Steven
 */
public interface IServiceSV {
    void increment(String svcName);
    void sinkToHBase(String timestamp);
}
