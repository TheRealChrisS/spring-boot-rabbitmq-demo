package com.cs.rabbitmq.client;

import com.cs.rabbitmq.Constants;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PublishSubscriberWorkerMain implements CommandLineRunner {

	private static final Logger logger = Logger.getLogger(PublishSubscriberWorkerMain.class);

	@Autowired
	AnnotationConfigApplicationContext context;

	@Bean
	Queue queue() {
		return new AnonymousQueue();
	}

	@Bean
	FanoutExchange exchange() {
		return new FanoutExchange(Constants.EXCHANGE_NAME);
	}

	@Bean
	Binding binding(final Queue queue, final FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}

	@Bean
	SimpleMessageListenerContainer container(final ConnectionFactory connectionFactory,
											 final MessageListenerAdapter listenerAdapter, final Queue queue) {

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queue.getName());
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	Receiver receiver() {
		return new Receiver();
	}

	@Bean
	MessageListenerAdapter listenerAdapter(final Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(final String[] args) {
		SpringApplication.run(PublishSubscriberWorkerMain.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {

		logger.info("connectionFactory => " + context.getBean(ConnectionFactory.class));

		System.out.println("Press enter to exit ...");
		System.in.read();

		context.close();
	}
}
