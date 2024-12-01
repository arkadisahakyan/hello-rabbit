package arkadisahakyan;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Publisher
{
    private static final String QUEUE_NAME = "hello";
    private static final Integer MAX_MESSAGES_COUNT = 100;

    public static void main( String[] args ) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("rabbitmq");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Send your message. To exit press CTRL+C");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message;
            for (int i = 0; i < MAX_MESSAGES_COUNT; i++) {
                if ((message = reader.readLine()) != null) {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println(" [x] Sent '" + message + "'");
                }
            }
        }
    }
}
