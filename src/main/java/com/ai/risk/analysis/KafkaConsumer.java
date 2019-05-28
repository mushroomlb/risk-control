package com.ai.risk.analysis;

import com.ai.risk.analysis.accumulator.ServiceAndInstanceAccumulator;
import com.ai.risk.analysis.accumulator.ServiceAndIpAccumulator;
import com.ai.risk.analysis.accumulator.ServiceAndOpCodeAccumulator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消费者
 * 使用@KafkaListener注解, 可以指定: 主题, 分区, 消费组
 *
 * @author Steven
 * @date 2019-05-24
 */
@Component
public class KafkaConsumer {

	private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

	/**
	 * 入口服务类型
	 */
	@Value("${risk.entrance.name}")
	private String entranceName;

	/**
	 * 步长，即多少条记录打印一行日志
	 */
	@Value("${risk.step}")
	private int step;

	@Value("${risk.seperatorChar}")
	private char seperatorChar;

	/**
	 * 链路 Topic
	 */
	private static final String TOPIC = "LOG4X-TRACE-TOPIC";

	private ObjectMapper mapper = new ObjectMapper();
	private AtomicLong index = new AtomicLong(0);


	@Autowired
	private ServiceAndOpCodeAccumulator serviceAndOpCodeAccumulator;

	@Autowired
	private ServiceAndIpAccumulator serviceAndIpAccumulator;

	@Autowired
	private ServiceAndInstanceAccumulator serviceAndInstanceAccumulator;

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "0")})
	public void listen_p0(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 0);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "1")})
	public void listen_p1(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 1);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "2")})
	public void listen_p2(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 2);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "3")})
	public void listen_p3(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 3);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "4")})
	public void listen_p4(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 4);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "5")})
	public void listen_p5(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 5);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "6")})
	public void listen_p6(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 6);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "7")})
	public void listen_p7(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 7);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "8")})
	public void listen_p8(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 8);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "9")})
	public void listen_p9(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 9);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "10")})
	public void listen_p10(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 10);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "11")})
	public void listen_p11(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			accumulation(span, 11);
		} catch (IOException e) {
		}
	}

	private String getHostIp(String strHostName) {
		String[] slice = StringUtils.split(strHostName, ',');
		if (null != slice) {
			for (String hostname : slice) {
				if (!"127.0.0.1".equals(hostname)) {
					return hostname;
				}
			}
		}
		return null;
	}

	/**
	 * 本地数据聚合
	 *
	 * @param span
	 * @param partition
	 */
	private void accumulation(Map span, int partition) {

		String callType = (String) span.get("callType");
		if (entranceName.equals(callType)) {
			String serviceName = (String) span.get("serviceName");
			Long startTime = (Long) span.get("startTime");
			Integer elapsedTime = (Integer) span.get("elapsedTime");
			String ip = getHostIp((String) span.get("hostName"));
			String instance = (String) span.get("appName");
			Map extMap = (Map) span.get("ext");
			String opCode = (String) extMap.get("opCode");
			if (StringUtils.isBlank(serviceName)) {
				return;
			}

			if (StringUtils.isNotBlank(opCode)) {
				serviceAndOpCodeAccumulator.localAccumulation(serviceName, opCode, elapsedTime);
			}

			if (StringUtils.isNotBlank(ip)) {
				serviceAndIpAccumulator.localAccumulation(serviceName, ip, elapsedTime);
			}

			if (StringUtils.isNotBlank(instance)) {
				serviceAndInstanceAccumulator.localAccumulation(serviceName, instance, elapsedTime);
			}

			long i = index.incrementAndGet();
			if (0 == i % 100000) {
				log.info(DateFormatUtils.format(startTime, "yyyy-MM-dd HH:mm:ss") + " process " + i);
			}
		}

	}

}
