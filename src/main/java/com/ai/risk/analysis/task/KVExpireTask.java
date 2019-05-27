package com.ai.risk.analysis.task;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 键值对，生命周期管理
 *
 * @author Steven
 * @date 2019-05-24
 */
//@Component
public class KVExpireTask {

	private static final Logger log = LoggerFactory.getLogger(KVExpireTask.class);

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 保留时长
	 */
	@Value("${lifecycle.hourTimeout}")
	private int hourTimeout;

	/**
	 * 上一次开始执行时间点之后多长时间再执行
	 */
	@Scheduled(initialDelayString = "${lifecycle.check.initialDelay}", fixedRateString = "${lifecycle.check.fixedRate}")
	public void scheduled() {

		long now = System.currentTimeMillis();
		String prefix = DateFormatUtils.format(now - 3600000, "yyyyMMddHH") + "-*";
		Set<String> keys = redisTemplate.keys(prefix);
		for (String key : keys) {
			redisTemplate.expire(key, hourTimeout, TimeUnit.HOURS);
		}

		log.info("设置KV的存活时间，前缀: " + prefix);

	}

}
