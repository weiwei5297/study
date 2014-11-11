package org.weiping.study.rabbitmq.topic;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiveLogsTopic {

	private static final String EXCHANGE_NAME = "topic_logs";

//	private static final String QUEUE_NAME = "Group_Refresh";
	private static final String QUEUE_NAME = "study_test";
	public static void main(String[] argv) {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");

			connection = factory.newConnection();
			channel = connection.createChannel();

//			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			channel.exchangeDeclare(EXCHANGE_NAME, "topic", true, false, null);
//			String queueName = channel.queueDeclare().getQueue();
			String queueName = QUEUE_NAME;
			channel.queueDeclare(queueName, true, false, false, null);

//			String queueName = channel.queueDeclare("", true, false, false, null).getQueue();
System.out.println("queueName: " + queueName);
			if (argv.length < 1) {
				System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
				System.exit(1);
			}

			for (String bindingKey : argv) {
				channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
			}

			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);

			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				String routingKey = delivery.getEnvelope().getRoutingKey();

				System.out.println(" [x] Received '" + routingKey + "':'"
						+ message + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}
	}
}
