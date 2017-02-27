package org.ednovo.gooru.kafka.producer;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class KafkaProperties {

	private static KafkaProperties instance = null;

	@Autowired
	@Resource(name = "configSettings")
	private Properties searchSettings;

	protected Properties getSearchSettings() {
		return this.searchSettings;
	}

	public KafkaProperties() {
		if (instance == null)
			instance = this;
	}

	public static final String KAFKA_PREFIX = "kafka.";
	public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
	public static final String TOPIC = "topic";
	public static final String RETRIES = "retries";
	public static final String BATCH_SIZE = "batch.size";
	public static final String LINGER_MS = "linger.ms";
	public static final String BUFFER_MEMORY = "buffer.memory";
	public static final String KEY_SERIALIZER = "key.serializer";
	public static final String VALUE_SERIALIZER = "value.serializer";

	public String bootstrapServers;
	public Integer retries;
	public Integer batchSize;
	public Long lingerMs;
	public Long bufferMemory;
	public String keySerializer;
	public String valueSerializer;
	public String topicValue;
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProperties.class);

	@PostConstruct
	public void init() {
		try {
			bootstrapServers = instance.getSearchSettings().getProperty(KAFKA_PREFIX + BOOTSTRAP_SERVERS);
			retries = Integer.valueOf(instance.getSearchSettings().getProperty(KAFKA_PREFIX + RETRIES));
			batchSize = Integer.valueOf(instance.getSearchSettings().getProperty(KAFKA_PREFIX + BATCH_SIZE));
			lingerMs = Long.valueOf(instance.getSearchSettings().getProperty(KAFKA_PREFIX + LINGER_MS));
			bufferMemory = Long.valueOf(instance.getSearchSettings().getProperty(KAFKA_PREFIX + BUFFER_MEMORY));
			keySerializer = instance.getSearchSettings().getProperty(KAFKA_PREFIX + KEY_SERIALIZER);
			valueSerializer = instance.getSearchSettings().getProperty(KAFKA_PREFIX + VALUE_SERIALIZER);
			topicValue = instance.getSearchSettings().getProperty(KAFKA_PREFIX + TOPIC);
		} catch (Exception e) {
			LOGGER.info("kafka error while getting config setting fields value :" + e);
		}
	}

}
