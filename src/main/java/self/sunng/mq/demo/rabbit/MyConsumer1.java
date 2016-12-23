package self.sunng.mq.demo.rabbit;

import com.rabbitmq.client.*;

/**
 * Created by sunxiaodong on 2016/10/25.
 */
public class MyConsumer1 {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
//        factory.setVirtualHost("test");
        factory.setHost("localhost");
        factory.setPort(5672);
        Connection conn = factory.newConnection();

        final Channel channel = conn.createChannel();

        boolean autoAck = false;
        GetResponse response = channel.basicGet("test.queue", autoAck);
        if (response == null) {
            System.out.println("no message");
        } else {
            AMQP.BasicProperties props = response.getProps();
            byte[] body = response.getBody();
            long deliveryTag = response.getEnvelope().getDeliveryTag();

            System.out.println(new String(body));

            channel.basicAck(deliveryTag, false); // acknowledge receipt of the message
        }

        channel.close();
        conn.close();
    }
}
