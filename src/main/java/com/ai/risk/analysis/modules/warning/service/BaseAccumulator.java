package com.ai.risk.analysis.modules.warning.service;

import com.ai.risk.analysis.modules.warning.entity.unit.PointUnit;
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

	private Map<String, PointUnit> accumulator = new ConcurrentHashMap<>();

	/**
	 * 本地数据汇总
	 *
	 * @param svcName
	 * @param suffix
	 * @param elapsedTime
	 */
	public void accumulation(String svcName, String suffix, int elapsedTime) {
		String key = svcName + seperatorChar + suffix;
		PointUnit pointUnit = accumulator.get(key);
		if (null == pointUnit) {
			pointUnit = new PointUnit();
			accumulator.put(key, pointUnit);
		}

		// 递增调用次数
		pointUnit.getCnt().incrementAndGet();

		// 递增调用耗时
		pointUnit.getTtc().addAndGet(elapsedTime);

		// 条件满足时,设置最高耗时
		if (elapsedTime < pointUnit.getMinCost()) {
			pointUnit.setMinCost(elapsedTime);
		}

		// 条件满足时,设置最高耗时
		if (elapsedTime > pointUnit.getMaxCost()) {
			pointUnit.setMaxCost(elapsedTime);
		}
	}

	/**
	 * 将本地汇总的数据写入 InfluxDB
	 *
	 * @param measurement
	 * @return
	 */
	public int toInfluxDb(String measurement, String tagName, int threshold) {
		Set<String> keySet = accumulator.keySet();
		if (null == keySet) {
			return 0;
		}

		int count = 0;
		for (String key : keySet) {
			PointUnit pointUnit = accumulator.get(key);

			long cnt = pointUnit.getCnt().longValue();
			long ttc = pointUnit.getTtc().longValue();
			int minCost = pointUnit.getMinCost();
			int maxCost = pointUnit.getMaxCost();

			// 计数器清零
			pointUnit.getCnt().set(0L);
			pointUnit.getTtc().set(0L);
			pointUnit.setMinCost(Integer.MAX_VALUE);
			pointUnit.setMaxCost(0);

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
