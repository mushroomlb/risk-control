package com.ai.risk.analysis.modules.warning.task;

import com.ai.risk.analysis.modules.warning.service.IServiceAndInstanceSV;
import com.ai.risk.analysis.modules.warning.service.IServiceAndIpSV;
import com.ai.risk.analysis.modules.warning.service.IServiceAndOpcodeSV;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Influxdb 聚合
 *
 * @author Steven
 * @date 2019-05-26
 */
@Component
@Slf4j
public class InfluxdbSinkTask {

	@Autowired
	private IServiceAndOpcodeSV serviceAndOpCodeSVImpl;

	@Autowired
	private IServiceAndIpSV serviceAndIpSVImpl;

	@Autowired
	private IServiceAndInstanceSV serviceAndInstanceSVImpl;

	/**
	 * 将本地缓存中的数据下沉到 InfluxDB，每一分钟跑一次
	 */
	@Scheduled(initialDelay = 15000, fixedRate = 1000 * 60 * 1)
	public void scheduled() {
		serviceAndOpCodeSVImpl.sinkToInflux();
		serviceAndIpSVImpl.sinkToInflux();
		serviceAndInstanceSVImpl.sinkToInflux();
	}

}
