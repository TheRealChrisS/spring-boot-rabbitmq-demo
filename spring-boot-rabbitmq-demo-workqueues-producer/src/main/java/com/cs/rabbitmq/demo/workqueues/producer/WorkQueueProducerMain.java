package com.cs.rabbitmq.demo.workqueues.producer;

import com.cs.rabbitmq.demo.workqueues.MyMessage;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@EnableScheduling
public class WorkQueueProducerMain implements CommandLineRunner {

	private static final Logger logger = Logger.getLogger(WorkQueueProducerMain.class);

	private final static String queueName = "spring-boot";

	private final AtomicInteger count = new AtomicInteger(0);

	@Autowired
	private
	AnnotationConfigApplicationContext context;

	@Autowired
	private
	RabbitTemplate rabbitTemplate;

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

//  @Bean
//  SimpleMessageListenerContainer container(final ConnectionFactory connectionFactory,
//      final MessageListenerAdapter listenerAdapter) {
//
//    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//    container.setConnectionFactory(connectionFactory);
//    container.setQueueNames(queueName);
//    container.setMessageListener(listenerAdapter);
//    return container;
//  }
//
//  @Bean
//  Receiver receiver() {
//    return new Receiver();
//  }
//
//  @Bean
//  MessageListenerAdapter listenerAdapter(final Receiver receiver) {
//    return new MessageListenerAdapter(receiver, "receiveMessage");
//  }

	public static void main(final String[] args) {
		SpringApplication.run(WorkQueueProducerMain.class, args);
	}

	//  @Scheduled(fixedRate = 1000L) // every 5 seconds
	private void sendMessage() {
		logger.info("Sending message...");
		rabbitTemplate.convertAndSend(queueName, new MyMessage("Hello from RabbitMQ! - " + count.incrementAndGet()));
	}

	@Override
	public void run(final String... args) throws Exception {

		logger.info("connectionFactory => " + context.getBean(ConnectionFactory.class));

		for (int i = 0; i < 50; i++) {
			sendMessage();
		}

//    System.out.println("Press enter to exit ...");
//    System.in.read();

		context.close();
	}
}
