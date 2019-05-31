package com.ai.risk.analysis.sink;

import com.ai.risk.analysis.ServicePoint;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Steven
 */
@Component
public class InfluxDbSink {

	private static final Logger log = LoggerFactory.getLogger(InfluxDbSink.class);

	@Autowired
	private InfluxDB influxDB;

	@Autowired
	private LinkedBlockingQueue<ServicePoint> linkedBlockingQueue;

	private long count = 0L;

	public void execute() {

		try {
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (true) {
			ServicePoint servicePoint;
			try {
				servicePoint = linkedBlockingQueue.take();
				Point point = Point.measurement("entry_service")
					.tag("svcName", servicePoint.svcName)
					.tag("opCode", servicePoint.opCode)
					.tag("ip", servicePoint.ip)
					.tag("instance", servicePoint.instance)
					.tag("success", String.valueOf(servicePoint.success))
					.addField("cost", servicePoint.cost)
					.time(servicePoint.startTime, TimeUnit.MILLISECONDS)
					.build();

				influxDB.write(point);

				if (0 == count++ % 100000) {
					log.info("InfluxDbSink: " + count);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
