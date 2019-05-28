package com.ai.risk.analysis.task;

import com.ai.risk.analysis.Entity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 将数据写入 InfluxDB
 *
 * @author Steven
 * @date 2019-05-26
 */
// @Component
public class RedisAccumulatorTask {

	private static final Logger log = LoggerFactory.getLogger(RedisAccumulatorTask.class);

	@Value("${risk.seperatorChar}")
	private char seperatorChar;

	@Value("${risk.redisAccumulator.threshold}")
	private int threshold;

	@Autowired
	private Map<String, Entity> localCounter;

	@Autowired
	private RedisTemplate redisTemplate;

	@Scheduled(initialDelay = 15000, fixedRate = 1000 * 60)
	public void scheduled() {

		Set<String> keySet = localCounter.keySet();
		if (null == keySet) {
			return;
		}

		int cnt = 0;
		for (String key : keySet) {
			Entity entity = localCounter.get(key);

			String[] slice = StringUtils.split(key, seperatorChar);
			String svnName = slice[0];
			String opCode = slice[1];

			redisTemplate.opsForHash().increment(svnName, "CNT-" + opCode, entity.getCnt().get());
			redisTemplate.opsForHash().increment(svnName, "TTC-" + opCode, entity.getTtc().get());
			cnt++;

			entity.getCnt().set(0L);
			entity.getTtc().set(0L);
		}

		log.info(String.format("一次聚合: %30s, 本次聚合次数: %6d", "localCounter -> Redis", cnt));
	}

}
