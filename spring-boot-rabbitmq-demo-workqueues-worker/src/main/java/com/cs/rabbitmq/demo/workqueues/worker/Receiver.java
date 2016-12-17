package com.cs.rabbitmq.demo.workqueues.worker;

import com.cs.rabbitmq.demo.workqueues.MyMessage;
import org.apache.log4j.Logger;

class Receiver {

	private static final Logger logger = Logger.getLogger(Receiver.class);

	public void receiveMessage(final MyMessage message) {
		logger.info("Received <" + message + ">");
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
