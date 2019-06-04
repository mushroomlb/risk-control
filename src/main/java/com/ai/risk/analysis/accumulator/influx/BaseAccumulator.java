package com.ai.risk.analysis.accumulator.influx;

import com.ai.risk.analysis.accumulator.CntUnit;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 聚合器基类
 *
 * @author Steven
 * @date 2019-05-28
 */
public class BaseAccumulator {

	@Value("${risk.seperatorChar}")
	private char seperatorChar;

	@Autowired
	private InfluxDB influxDB;

	private Map<String, CntUnit> accumulator = new ConcurrentHashMap<>();

	/**
	 * 本地数据汇总
	 *
	 * @param svcName
	 * @param suffix
	 * @param elapsedTime
	 */
	void accumulation(String svcName, String suffix, int elapsedTime) {
		String key = svcName + seperatorChar + suffix;
		CntUnit entity = accumulator.get(key);
		if (null == entity) {
			entity = new CntUnit();
			accumulator.put(key, entity);
		}

		// 递增调用次数
		entity.getCnt().incrementAndGet();

		// 递增调用耗时
		entity.getTtc().addAndGet(elapsedTime);

		// 条件满足时,设置最高耗时
		if (elapsedTime < entity.getMinCost()) {
			entity.setMinCost(elapsedTime);
		}

		// 条件满足时,设置最高耗时
		if (elapsedTime > entity.getMaxCost()) {
			entity.setMaxCost(elapsedTime);
		}
	}

	/**
	 * 将本地汇总的数据写入 InfluxDB
	 *
	 * @param measurement
	 * @return
	 */
	int toInfluxDb(String measurement, String tagName, int threshold) {
		Set<String> keySet = accumulator.keySet();
		if (null == keySet) {
			return 0;
		}

		int count = 0;
		for (String key : keySet) {
			CntUnit entity = accumulator.get(key);

			long cnt = entity.getCnt().longValue();
			long ttc = entity.getTtc().longValue();
			int minCost = entity.getMinCost();
			int maxCost = entity.getMaxCost();

			// 计数器清零
			entity.getCnt().set(0L);
			entity.getTtc().set(0L);
			entity.setMinCost(Integer.MAX_VALUE);
			entity.setMaxCost(0);

			if (0 == cnt || cnt < threshold) {
				// 只统计周期内总调用次数大于阀值的
				continue;
			}

			// 平均耗时，毫秒
			long cost = ttc / cnt;

			String[] slice = StringUtils.split(key, seperatorChar);
			String svcName = slice[0];
			String tagValue = slice[1];

			write(measurement, svcName, tagName, tagValue, cnt, cost, minCost, maxCost);
			count++;
		}

		return count;
	}

	/**
	 *
	 * @param svcName 服务名
	 * @param tagName
	 * @param cnt 调用次数
	 * @param cost 总耗时
	 */
	private void write(String measurement, String svcName, String tagName, String tagValue, long cnt, long cost, int minCost, int maxCost) {
		Point point = Point.measurement(measurement)
			.tag("svcName", svcName)
			.tag(tagName, tagValue)
			.addField("cnt", cnt)
			.addField("cost", cost)
			.addField("minCost", minCost)
			.addField("maxCost", maxCost)
			.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
			.build();

		influxDB.write(point);
	}

}
