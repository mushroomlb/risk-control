package com.ai.risk.analysis.modules.warning.service;

import com.ai.risk.analysis.modules.collect.entity.po.CallCountUnit;
import com.ai.risk.analysis.modules.warning.entity.po.Warning;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Steven
 * @since 2019-06-04
 */
public interface IWarningService extends IService<Warning> {
    void warning(String tableName, String time, List<CallCountUnit> list) throws Exception;
}
