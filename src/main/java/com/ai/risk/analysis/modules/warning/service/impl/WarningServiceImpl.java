package com.ai.risk.analysis.modules.warning.service.impl;

import com.ai.risk.analysis.modules.warning.entity.po.Warning;
import com.ai.risk.analysis.modules.warning.mapper.WarningMapper;
import com.ai.risk.analysis.modules.warning.service.IWarningService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-06-04
 */
@Slf4j
@Service
public class WarningServiceImpl extends ServiceImpl<WarningMapper, Warning> implements IWarningService {

}
