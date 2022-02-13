package com.tms.api.helper;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by dinhanhthai on 23/08/2019.
 */

public class QueueHelper {
    private static final Logger logger = LoggerFactory.getLogger(QueueHelper.class);
    
    private static final String _QUEUE_USER_NAME = "tms_app";
    private static final String _QUEUE_PASSWORD = "P@ssw0rdTm5";
    private static final String _QUEUE_HOST = "localhost";
    private static final Integer _QUEUE_PORT = 5672;
    private static final String _QUEUE_NAME = "tms";
    private static final String _QUEUE_VIRTUAL_HOST = "/";
    private static ConnectionFactory _FACTORY_QUEUE = null;


    public static ConnectionFactory createQueueConnection() {
        if (_FACTORY_QUEUE == null) {
            String qUser = com.tms.ff.utils.AppProperties.getPropertyValue("config.queue-user");
            String qPassword = com.tms.ff.utils.AppProperties.getPropertyValue("config.queue-password");
            String qHost = com.tms.ff.utils.AppProperties.getPropertyValue("config.queue-host");
            String qPort = com.tms.ff.utils.AppProperties.getPropertyValue("config.queue-port");
            String qvHost = com.tms.ff.utils.AppProperties.getPropertyValue("config.queue-vhost");

            ConnectionFactory factory = new ConnectionFactory();
// "guest"/"guest" by default, limited to localhost connections
            factory.setUsername(qUser != null ? qUser : _QUEUE_USER_NAME);
            factory.setPassword(qPassword != null ? qPassword : _QUEUE_PASSWORD);
            factory.setVirtualHost(qvHost != null ? qvHost : _QUEUE_VIRTUAL_HOST);
            factory.setHost(qHost != null ? qHost : _QUEUE_HOST);
            factory.setPort(qPort != null ? Integer.parseInt(qPort) : _QUEUE_PORT);
            _FACTORY_QUEUE = factory;

            logger.info("KKKKKKKKKKKKKKKKK {} - {} - {} - {} - {} - ", qUser, qPassword, qHost, qvHost, qPort);
        }


        return _FACTORY_QUEUE;
    }

    public static void sendMessage(com.tms.entity.CLFresh clFresh, int type) {
        if (clFresh.getAgcId() != null && clFresh.getClickId() != null) {
            String mesage = createQueueMessage(clFresh.getOrgId(), clFresh.getAgcId(), clFresh.getClickId(), type, clFresh.getOfferId(), clFresh.getLeadId(), clFresh.getPrice(), Integer.parseInt(clFresh.getTerms()));
            sendMessage(mesage);
        }
    }

    public static String createQueueMessage(int orgId, int agcId, String clickId, int type, String offerId, int leadId, String payout, int terms, int numOfProd) {
        if(agcId == Const.AGENCY_ADFLEX)
            payout = String.format("%s#;%s", payout, numOfProd);
        return createQueueMessage(orgId, agcId, clickId, type, offerId, leadId, payout, terms);
    }

    /**
     * @param orgId
     * @param agcId
     * @param clickId
     * @param type    -1: reject, 0 hold, 1 approve
     * @return
     */
    public static String createQueueMessage(int orgId, int agcId, String clickId, int type, String offerId, int leadId, String payout, int terms) {
        String mesage = "";
        switch (type) {
            case -1:
                mesage = "reject";
                break;
            case 0:
                mesage = "hold";
                break;
            case 1:
                mesage = "approve";
                break;
            case 2:
                mesage = "trash";
                break;
            case 3:
                mesage = "fake";
                break;
            case 4:
                mesage = "delivered";
                break;
            default:
                mesage = "hold";
                break;
        }
//        return String.format("%s|%s|%s|%s", agcId, leadId, soId == null ? "" : soId, doId == null ? "" : doId);
        if (payout == null || payout.isEmpty()) {
            payout = "0";
        }
        return String.format("%s|%s|%s|%s|%s|%s|%s", orgId, agcId, mesage, offerId, leadId, clickId, payout);
//        return String.format("%s|%s|%s|%s", orgId, agcId, clickId, mesage);
    }

    public static String createQueueMessage(int orgId, int agcId, String clickId, int type, String offerId, int leadId, String note, String payout, int terms) {
        String mesage = createQueueMessage(orgId, agcId, clickId, type, offerId, leadId, payout, terms);
        if (note != null && !note.isEmpty()) {
            note = note.replaceAll("\\|", "-");
        } else {
            note = "other reason";
        }
        return String.format("%s|%s|%s", mesage, terms, note);
    }

    public static String createQueueMessage(int orgId, int agcId, String clickId, int type, String offerId, int leadId,
            String note, String payout, int terms, int trackerId, String affClickId) {
        String mesage = createQueueMessage(orgId, agcId, clickId, type, offerId, leadId, payout, terms);
        if (note != null && !note.isEmpty()) {
            note = note.replaceAll("\\|", "-");
        } else {
            note = "other reason";
        }
        return String.format("%s|%s|%s|%s|%s", mesage, terms, note, trackerId, affClickId);
    }

    public static void sendMessage(String message) {
        sendMessage(message, null);
    }

    public static void sendMessage(String message, String queueName) {
        ConnectionFactory factory = createQueueConnection();
        String nameOfQueue = (queueName == null ? _QUEUE_NAME : queueName);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.basicPublish("", nameOfQueue, new AMQP.BasicProperties.Builder()
                    .contentType("text/plain")
                    .deliveryMode(2)
                    .build(), message.getBytes());
            logger.info(" [x] QueueHelper Sent '{}' to {}", message, nameOfQueue);
        } catch (Exception e) {
            logger.error("{} {}", message, e.getMessage(), e);
        }

    }

    public static void countMessage(String queueName, JavaMailSender javaMailSender) {
        ConnectionFactory factory = createQueueConnection();
        int messageCount = 0;
        String nameOfQueue = (queueName == null ? _QUEUE_NAME : queueName);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            AMQP.Queue.DeclareOk response = channel.queueDeclarePassive(nameOfQueue);
            // returns the number of messages in Ready state in the queue
            messageCount= response.getMessageCount();
            logger.info(" [x] countMessage Count  to {}", messageCount);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            if(messageCount > 100){//gui mail canh bao
                try {
                    MailHelper.sendMail("xxx", "thai.dinh@kiwi-eco.com", "QUEUE HAVE A PROBLEM ", "More than 100 : " + messageCount, javaMailSender);
                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
            }
        }

    }
}
