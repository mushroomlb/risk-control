package com.ai.risk.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

	@Autowired
	private RedisTemplate redisTemplate;

	private ObjectMapper mapper = new ObjectMapper();
	private AtomicLong index = new AtomicLong(0);


	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "0")})
	public void listen_G0(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 0);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "1")})
	public void listen_G1(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 1);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "2")})
	public void listen_G2(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 2);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "3")})
	public void listen_G3(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 3);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "4")})
	public void listen_G4(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 4);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "5")})
	public void listen_G5(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 5);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "6")})
	public void listen_G6(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 6);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "7")})
	public void listen_G7(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 7);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "8")})
	public void listen_G8(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 8);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "9")})
	public void listen_G9(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 9);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "10")})
	public void listen_G10(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 10);
		} catch (IOException e) {
		}
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = "LOG4X-TRACE-TOPIC", partitions = "11")})
	public void listen_G11(ConsumerRecord<?, String> record) {
		String value = record.value();
		try {
			Map span = mapper.readValue(value, Map.class);
			toRedis(span, 11);
		} catch (IOException e) {
		}
	}

	private void toRedis(Map span, int partition) {

		String callType = (String) span.get("callType");
		if (entranceName.equals(callType)) {
			String serviceName = (String) span.get("serviceName");
			Long startTime = (Long) span.get("startTime");
			Map extMap = (Map) span.get("ext");
			String opCode = (String) extMap.get("opCode");
			if (StringUtils.isBlank(serviceName) || StringUtils.isBlank(opCode)) {
				return;
			}

			String startTimeStr = DateFormatUtils.format(startTime, "yyyyMMddHH");

			String key = startTimeStr + "-" + serviceName;
			redisTemplate.opsForZSet().incrementScore(key, opCode, 1);

			if (0 == index.incrementAndGet() % step) {
				String msg = String.format("%s [%3d] -> %s  %-80s", DateFormatUtils.format(startTime, "yyyy-MM-dd HH:mm:ss"), partition, opCode, serviceName);
				System.out.println(msg);
			}

		}

	}

}
