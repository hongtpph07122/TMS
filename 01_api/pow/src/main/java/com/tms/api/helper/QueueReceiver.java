package com.tms.api.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = Const.QUEUE_TEST)
public class QueueReceiver {

    private Logger logger = LoggerFactory.getLogger(QueueReceiver.class);
    public static String getQueueMessage = "";

    @RabbitHandler
    public void receive(String in) {
        logger.info("getQueueMessage: " + getQueueMessage);
        logger.info(" [x] Received '" + in + "'");
        getQueueMessage = in;
    }


}
