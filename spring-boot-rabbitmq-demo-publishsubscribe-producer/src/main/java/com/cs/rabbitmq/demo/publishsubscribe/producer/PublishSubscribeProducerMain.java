package com.cs.rabbitmq.demo.publishsubscribe.producer;

import com.cs.rabbitmq.demo.publishsubscribe.Constants;
import com.cs.rabbitmq.demo.publishsubscribe.MyMessage;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@EnableScheduling
public class PublishSubscribeProducerMain implements CommandLineRunner {

	private static final Logger logger = Logger.getLogger(PublishSubscribeProducerMain.class);

	private final AtomicInteger count = new AtomicInteger(0);

	@Autowired
	private AnnotationConfigApplicationContext context;

	@Autowired
	private
	RabbitTemplate rabbitTemplate;

	@Bean
	FanoutExchange exchange() {
		return new FanoutExchange(Constants.EXCHANGE_NAME);
	}

//  @Bean
//  Binding binding(final FanoutExchange exchange) {
//    return BindingBuilder.bind(exchange).to(exchange);
//  }

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
		SpringApplication.run(PublishSubscribeProducerMain.class, args);
	}

	@Scheduled(fixedRate = 1000L)
	public void sendMessage() {
		int i = count.incrementAndGet();
		logger.info("Sending message " + i + " ...");
		rabbitTemplate.convertAndSend(Constants.EXCHANGE_NAME, "", new MyMessage("Hello from RabbitMQ! - " + i));
	}

	@Override
	public void run(final String... args) throws Exception {

		logger.info("connectionFactory => " + context.getBean(ConnectionFactory.class));

		System.out.println("Press enter to exit ...");
		System.in.read();

		context.close();
	}
}
