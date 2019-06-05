package com.ai.risk.analysis.modules.warning.controller;

import com.ai.risk.analysis.modules.warning.entity.po.Warning;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.ai.risk.analysis.modules.warning.service.IWarningService;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-06-04
 */
@RestController
@Slf4j
@RequestMapping("/api/demo/warning")
public class WarningController {

    @Autowired
    private IWarningService warningServiceImpl;

    @GetMapping("/add")
    public void test() {
        System.out.println("add...... start");
        Warning warning = new Warning();
        warning.setCnt(10L);
        warning.setCreateDate(LocalDateTime.now());
        warning.setIsWarning("yes");
        warning.setName("name");
        warningServiceImpl.save(warning);
        System.out.println("add...... end");
    }

}
