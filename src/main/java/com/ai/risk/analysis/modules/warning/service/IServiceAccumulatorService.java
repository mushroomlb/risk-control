package com.ai.risk.analysis.modules.warning.service;


import com.ai.risk.analysis.modules.collect.entity.po.CallCountUnit;

import java.util.List;

public interface IServiceAccumulatorService {
    void serviceAccumulator(String time, List<CallCountUnit> list);
}
