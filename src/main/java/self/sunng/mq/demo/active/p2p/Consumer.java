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
                "failover:(tcp://10.0.129.118:61616)");

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
            while (true) {
                //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
                TextMessage message = (TextMessage) consumer.receive(100000);
                if (null != message) {
                    System.out.println("收到消息" + message.getText());
                } else {
                    break;
                }
            }
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
}
