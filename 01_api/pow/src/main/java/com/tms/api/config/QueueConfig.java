package com.tms.api.config;

import com.tms.api.helper.Const;
import com.tms.api.helper.QueueReceiver;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Created by dinhanhthai on 24/08/2019.
 */
@Configuration
public class QueueConfig {


    @Bean
    public Queue hello() {
        return new Queue(Const.QUEUE_TEST);
    }

    private static class ReceiverConfig {
        @Bean
        public QueueReceiver receiver() {
            return new QueueReceiver();
        }
    }
}
