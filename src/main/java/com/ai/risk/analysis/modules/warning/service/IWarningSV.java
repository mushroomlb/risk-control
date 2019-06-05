package com.ai.risk.analysis.modules.warning.service;

import com.ai.risk.analysis.modules.warning.entity.unit.CallUnit;
import com.ai.risk.analysis.modules.warning.entity.po.Warning;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  预警服务
 * </p>
 *
 * @author Steven
 * @since 2019-06-04
 */
public interface IWarningSV extends IService<Warning> {
    void warning(String tableName, String time, List<CallUnit> list) throws Exception;
}
