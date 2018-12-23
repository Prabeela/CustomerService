package com.customer.microservices.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.customer.microservices.domain.CustomQueueMessage;
import com.customer.microservices.domain.Customer;
import com.rabbitmq.client.AMQP.Exchange;
import org.springframework.amqp.core.Queue;

@Service
public class CustomerServiceProducer {

	private final RabbitTemplate template;
	
	@Autowired
	public CustomerServiceProducer(RabbitTemplate template){
		this.template = template;
	}
	 
	 
	 


	  public void createCustomerEvent(Customer customer) {
	    // ... do some database stuff
		System.out.println("Inside customer creation");
		
		CustomQueueMessage custMsg= new CustomQueueMessage("customer.created",customer);
	    
	    template.convertAndSend(custMsg);
	  }

	}