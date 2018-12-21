package com.customer.microservices.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.customer.microservices.domain.Customer;
import com.rabbitmq.client.AMQP.Exchange;

public class CustomerServiceProducer {

	 private final RabbitTemplate rabbitTemplate;

	  
	 @Autowired
	  public CustomerServiceProducer(RabbitTemplate rabbitTemplate) {
	    this.rabbitTemplate = rabbitTemplate;
	  
	  }

	  public void createCustomerEvent(Customer customer) {
	    // ... do some database stuff
	    String routingKey = "customer.created";
	   // String message = "customer created";
	    rabbitTemplate.convertAndSend(routingKey, customer);
	  }

	}