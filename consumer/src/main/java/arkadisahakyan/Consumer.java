package arkadisahakyan;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class Consumer
{
    private static final String QUEUE_NAME = "hello";
    private static final Integer MAX_MESSAGES_COUNT = 100;

    public static void main( String[] args ) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("rabbitmq");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            CountDownLatch countDownLatch = new CountDownLatch(MAX_MESSAGES_COUNT);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                countDownLatch.countDown();
            };
            String consumer = channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
            countDownLatch.await();
            channel.basicCancel(consumer);
        }
    }
}
