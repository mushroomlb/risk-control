package com.ai.risk.analysis.task;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 将数据写入 InfluxDB
 *
 * @author Steven
 * @date 2019-05-26
 */
@Component
public class InfluxDbAccumulatorTask {

	private static final Logger log = LoggerFactory.getLogger(InfluxDbAccumulatorTask.class);

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private InfluxDB influxDB;

	/**
	 * 保留时长
	 */
	@Value("${lifecycle.hourTimeout}")
	private int hourTimeout;

	@Scheduled(initialDelay = 15000, fixedRate = 1000 * 60)
	public void scheduled() {
		long now = System.currentTimeMillis();
		String prefix = "*";
		Set<String> keys = redisTemplate.keys(prefix);
		int cnt = 0;
		for (String key : keys) {
			Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 5);

			for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
				String svcName = key;
				String opCode = tuple.getValue().toString();
				int score = tuple.getScore().intValue();
				write(svcName, opCode, score);
				cnt++;
			}

		}

		log.info(String.format("二次聚合: %30s, 本次聚合次数: %6d", "Redis -> InfluxDB", cnt));
	}


	/**
	 *
	 * @param svcName 服务名
	 * @param opCode 操作工号
	 * @param score 调用次数
	 */
	private final void write(String svcName, String opCode, int score) {
		Point point = Point.measurement("service_load")
			.tag("svcName", svcName)
			.tag("opCode", opCode)
			.addField("value", score)
			.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
			.build();

		influxDB.write(point);
	}

}
