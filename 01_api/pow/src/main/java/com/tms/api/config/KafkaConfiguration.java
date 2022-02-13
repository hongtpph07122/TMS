package com.tms.api.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {
    @Value("${config.kafka.consumer.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS_CONFIG;
    @Value("${config.kafka.consumer.group-id}")
    private String GROUP_ID_CONFIG;
    @Value("${config.kafka.security-protocol}")
    private String SECURITY_PROTOCOL;
    @Value("${config.kafka.sasl.mechanism}")
    private String SASL_MECHANISM;
    @Value("${config.kafka.sasl.jaas-config}")
    private String SASL_JAAS_CONFIG;
    @Value("${config.kafka.auto-offset-reset}")
    private String AUTO_OFFSET_RESET;
    @Value("${config.kafka.max-poll-record}")
    private Integer MAX_POLL_RECORD;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put("security.protocol", SECURITY_PROTOCOL);
        config.put("sasl.mechanism", SASL_MECHANISM);
        config.put("sasl.jaas.config", SASL_JAAS_CONFIG);
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORD);
        return new DefaultKafkaConsumerFactory<>(config);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
