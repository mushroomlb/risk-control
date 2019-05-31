package com.ai.risk.analysis;

import com.ai.risk.analysis.sink.InfluxDbSink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Steven
 * @date 2019-05-26
 */
@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//		InfluxDbSink influxDbSink = context.getBean(InfluxDbSink.class);
//		influxDbSink.execute();
	}

}
