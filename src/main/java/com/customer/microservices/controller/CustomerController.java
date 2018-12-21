package com.customer.microservices.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.customer.microservices.config.CustomerServiceProducer;
import com.customer.microservices.domain.Customer;
import com.customer.microservices.repository.CustomerRepository;

@RestController
public class CustomerController {
	
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	CustomerServiceProducer customerServiceProducerEvent;

    @GetMapping("/service1/customers")
    public List<Customer> snippets() {
        return customerRepository.findAll();
    }

    @GetMapping("/service1/customer/{id}")
    public Customer customer(@PathVariable("id") String id) {
        return customerRepository.findOne(id);
    }

    @PostMapping("/service1/customer")
    public ResponseEntity<?> add(@RequestBody Customer customer) {
    	Customer _customer = customerRepository.save(customer);
        assert _customer != null;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + _customer.getId())
                .buildAndExpand().toUri());

       
        customerServiceProducerEvent.createCustomerEvent(_customer);
        return new ResponseEntity<>(_customer, httpHeaders, HttpStatus.CREATED);
    }
}

