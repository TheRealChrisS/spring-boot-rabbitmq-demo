package com.cs.rabbitmq.demo.workqueues.worker;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
public class WorkQueueWorkerMain implements CommandLineRunner {

	private static final Logger logger = Logger.getLogger(WorkQueueWorkerMain.class);

	private final static String queueName = "spring-boot";

	@Autowired
	private AnnotationConfigApplicationContext context;

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(final Queue queue, final TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	@Bean
	SimpleMessageListenerContainer container(final ConnectionFactory connectionFactory,
											 final MessageListenerAdapter listenerAdapter) {

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
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
		SpringApplication.run(WorkQueueWorkerMain.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {

		logger.info("connectionFactory => " + context.getBean(ConnectionFactory.class));

		System.out.println("Press enter to exit ...");
		System.in.read();

		context.close();
	}
}
