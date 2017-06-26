package self.sunng.mq.demo.active.p2p;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by sunxiaodong on 16/8/28.
 */
public class Producer {

    public static void main(String[] args) {
        // ConnectionFactory：连接工厂，JMS用它创建连接
        ConnectionFactory connectionFactory;

        // Connection：JMS客户端到JMS Provider的连接
        Connection connection = null;

        // Session：一个发送或接收消息的线程
        Session session;

        // Destination：消息的目的地;消息发送给谁.
        Destination destination;

        // MessageProducer：消息发送者
        MessageProducer producer;

        // TextMessage message;
        //构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");
                // "failover:(tcp://localhost:61616,tcp://localhost:61626,tcp://localhost:61636)");
                // "failover:(tcp://localhost:61646,tcp://localhost:61656)");
        //?maxReconnectAttempts=1

        try {
            //构造从工厂得到连接对象
            connection = connectionFactory.createConnection();

            connection.setExceptionListener(new ExceptionListener() {
                public void onException(JMSException e) {
                    e.printStackTrace();
                }
            });

            connection.start();

            session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);

            destination = session.createQueue("FirstQueue");

            producer = session.createProducer(destination);

            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            sendMessage(session, producer);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer producer) throws Exception {

        int sendCount = 1;
        System.out.println(System.currentTimeMillis());
        for (int i = 1; i <= sendCount; i++) {

            TextMessage message = session
                    .createTextMessage("ActiveMq发送的消息" + i);

            //发送消息到目的地方
            // System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);

            // Thread.sleep(1000);

        }
        System.out.println(System.currentTimeMillis());

    }
}
