package self.sunng.mq.demo.active.p2p;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;

import javax.jms.*;

/**
 * Created by sunxiaodong on 16/8/28.
 */
public class Consumer {

    public static void main(String[] args) throws JMSException {
        // ConnectionFactory：连接工厂，JMS用它创建连接
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
            ActiveMQConnection.DEFAULT_USER,
            ActiveMQConnection.DEFAULT_PASSWORD,
            "tcp://localhost:61616");
        // "failover:(tcp://localhost:61616,tcp://localhost:61626,tcp://localhost:61636)");

        // Connection：JMS客户端到JMS Provider的连接
        Connection connection = connectionFactory.createConnection();

        // Session：一个发送或接收消息的线程
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Destination：消息的目的地;消息发送给谁.
        Destination destination = session.createQueue("FirstQueue");

        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(message -> {
            try {
                ActiveMQMessage activeMQMessage = (ActiveMQMessage) message;

                System.out.println(activeMQMessage.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        connection.start();
    }
}
