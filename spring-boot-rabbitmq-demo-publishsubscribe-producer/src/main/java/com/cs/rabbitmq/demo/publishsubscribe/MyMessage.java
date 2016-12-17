package com.cs.rabbitmq.demo.publishsubscribe;

import java.io.Serializable;
import java.util.Date;

public class MyMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String message;

	private final Date created = new Date();

	public MyMessage(final String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message + ", created at: " + created;
	}
}
