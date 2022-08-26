package com.cs.rabbitmq.demo.publishsubscribe.worker;

import com.cs.rabbitmq.demo.MyMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class Receiver {

	public void receiveMessage(final MyMessage message) {
		log.info("Received <" + message + ">");
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
