package self.sunng.mq.demo.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by sunxiaodong on 2016/10/25.
 */
public class MyConsumer2 {

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
        channel.basicConsume("test.queue", autoAck, "myConsumerTag",
            new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    long deliveryTag = envelope.getDeliveryTag();
                    System.out.println(new String(body));
                    channel.basicAck(deliveryTag, false);
                }
            });

        channel.close();
        conn.close();
    }
}
