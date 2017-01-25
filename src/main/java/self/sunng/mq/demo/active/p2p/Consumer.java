package self.sunng.mq.demo.active.p2p;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by sunxiaodong on 16/8/28.
 */
public class Consumer {

    public static void main(String[] args) {
        // ConnectionFactory：连接工厂，JMS用它创建连接
        ConnectionFactory connectionFactory;

        // Connection：JMS客户端到JMS Provider的连接
        Connection connection = null;

        // Session：一个发送或接收消息的线程
        Session session;

        // Destination：消息的目的地;消息发送给谁.
        Destination destination;

        //消费者，消息接收者
        MessageConsumer consumer;

        connectionFactory = new ActiveMQConnectionFactory(
            ActiveMQConnection.DEFAULT_USER,
            ActiveMQConnection.DEFAULT_PASSWORD,
            "tcp://localhost:61646");
            // "failover:(tcp://localhost:61616,tcp://localhost:61626,tcp://localhost:61636)");

        try {
            //构造从工厂得到连接对象
            connection = connectionFactory.createConnection();

            //启动
            connection.start();

            //获取操作连接
            session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

            //获取session
            destination = session.createQueue("FirstQueue");

            consumer = session.createConsumer(destination);
            System.out.println(System.currentTimeMillis());
            while (true) {
                TextMessage message = (TextMessage) consumer.receive(1000);
                if (null != message) {
                    // System.out.println("收到消息" + message.getText());
                } else {
                    break;
                }
                // Thread.sleep(1000);
            }
            System.out.println(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (Throwable ignore) {
                ignore.printStackTrace();
            }
        }
    }
}
