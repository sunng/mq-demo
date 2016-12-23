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
                "failover:(tcp://10.0.129.118:61616)?maxReconnectAttempts=1");

        try {
            //构造从工厂得到连接对象
            connection = connectionFactory.createConnection();

            connection.setExceptionListener(new ExceptionListener() {
                public void onException(JMSException e) {
                    e.printStackTrace();
                }
            });

            //启动
            connection.start();

            //获取操作连接
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

            //获取session
            destination = session.createQueue("FirstQueue");

            //得到消息生成者【发送者】
            producer = session.createProducer(destination);

            //设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            //构造消息，此处写死，项目就是参数，或者方法获取
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

    public static void sendMessage(Session session, MessageProducer producer)

            throws Exception {

        int SEND_NUMBER = 5;
        for (int i = 1; i <= SEND_NUMBER; i++) {

            TextMessage message = session
                    .createTextMessage("ActiveMq发送的消息" + i);

            //发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);

        }

    }
}
