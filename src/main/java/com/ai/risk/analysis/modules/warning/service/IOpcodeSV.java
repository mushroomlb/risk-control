package com.ai.risk.analysis.modules.warning.service;


import com.ai.risk.analysis.modules.warning.entity.unit.CallUnit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Steven
 */
public interface IOpcodeSV {
    void increment(String opCode);
    void sinkToHBase(String timestamp);
}