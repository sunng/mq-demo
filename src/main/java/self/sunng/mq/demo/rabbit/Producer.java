package self.sunng.mq.demo.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by sunxiaodong on 2016/10/25.
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
//        factory.setVirtualHost("test");
        factory.setHost("localhost");
        factory.setPort(5672);
//        factory.setUri("amqp://guest:guest@localhost:5672");
        Connection conn = factory.newConnection();

        Channel channel = conn.createChannel();

        channel.exchangeDeclare("test.exchange", "direct", true);
        channel.queueDeclare("test.queue", true, false, false, null);
        channel.queueBind("test.queue", "test.exchange", "test.routingKey");

        byte[] messageBodyBytes = "Hello, world!".getBytes();
        channel.basicPublish("test.exchange", "test.routingKey", null, messageBodyBytes);

        channel.close();
        conn.close();
    }
}
