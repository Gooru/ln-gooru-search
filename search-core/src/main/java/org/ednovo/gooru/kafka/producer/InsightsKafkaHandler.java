package org.ednovo.gooru.kafka.producer;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.ednovo.gooru.kafka.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InsightsKafkaHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsightsKafkaHandler.class);

	private static final String INSIGHTS_TOPIC = "insights.kafka.topic";

	private static final String INSIGHTS_KAFKA_IP = "insights.kafka.ip";

	private static final String INSIGHTS_PORT_NO = "insights.kafka.port";

	private static String INSIGHTS_DEFAULT_TOPIC;

	private static InsightsKafkaHandler instance = null;

	@Autowired
	@Resource(name = "configSettings")
	private Properties searchSettings;

	protected Properties getSearchSettings() {
		return this.searchSettings;
	}

	public InsightsKafkaHandler() {
		if (instance == null)
			instance = this;
	}

	@Autowired
	private KafkaProducer kafkaProducer;

	@PostConstruct
	private void init() {
		try {
			INSIGHTS_DEFAULT_TOPIC = instance.getSearchSettings().getProperty(INSIGHTS_TOPIC);
			getKafkaProducer().init(instance.getSearchSettings().getProperty(INSIGHTS_KAFKA_IP), instance.getSearchSettings().getProperty(INSIGHTS_PORT_NO), INSIGHTS_DEFAULT_TOPIC);
		} catch (Exception e) {
			LOGGER.error("insights kafka connectionConfig attributes not found");
		}
	}

	public void sendEventLog(String data) {
		getKafkaProducer().send(data);
	}

	public void sendEventLog(String eventName, String data) {
		getKafkaProducer().send(getTopic(eventName, INSIGHTS_DEFAULT_TOPIC), data);
	}

	private String getTopic(String eventName, String topic) {
		return topic;
	}

	public KafkaProducer getKafkaProducer() {
		return kafkaProducer;
	}

}
