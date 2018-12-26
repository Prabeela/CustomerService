package com.customer.microservices.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.customer.microservices.domain.CustomQueueMessage;
import com.customer.microservices.domain.Customer;
import com.rabbitmq.client.AMQP.Exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;

@Service
public class CustomerServiceProducer {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final RabbitTemplate template;
	
	@Autowired
	public CustomerServiceProducer(RabbitTemplate template){
		this.template = template;
	}
	 
	 
	 


	  public void createCustomerEvent(Customer customer) {
	    // ... do some database stuff
		
		logger.debug("Creating a new customer in the queue");
		
		CustomQueueMessage custMsg= new CustomQueueMessage("customer.created",customer);
	    
	    template.convertAndSend(custMsg);
	    logger.debug("Created new message in the queue");
	  }

	}