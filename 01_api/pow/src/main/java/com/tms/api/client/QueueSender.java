package com.tms.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dinhanhthai on 26/08/2019.
 */
@Service
public class QueueSender {
    private final Logger logger = LoggerFactory.getLogger(QueueSender.class);
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;


    public void send(String message) {
        this.template.convertAndSend(queue.getName(), message);
        logger.info(" [x] Sent '{}'", message);
    }
}
