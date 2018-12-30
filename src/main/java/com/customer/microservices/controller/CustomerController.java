package com.customer.microservices.controller;


import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
@RefreshScope
public class CustomerController {
	
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	CustomerServiceProducer customerServiceProducerEvent;
	
	DBInfo dbinfo;
	
	public CustomerController(DBInfo dbinfo){
		this.dbinfo = dbinfo;
	}
	
	@GetMapping("/service1/dbinfo")
	public DBInfo getInfo(){
		return this.dbinfo;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @GetMapping("/service1/customers")
    public List<Customer> snippets() {
    	
    	logger.debug("Retrieving customer from customer repository ");
        return customerRepository.findAll();
    }

    @GetMapping("/service1/customer/{id}")
    public Customer customer(@PathVariable("id") String id) {
    	
    	logger.debug("Retrieving customer based on ID "+id);
        return customerRepository.findOne(id);
    }

    @PostMapping("/service1/customer")
    public ResponseEntity<?> add(@RequestBody Customer customer) {
    	Customer _customer = customerRepository.save(customer);
        assert _customer != null;
        
        logger.debug("Adding new customer details..");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + _customer.getId())
                .buildAndExpand().toUri());

        
        logger.debug("Adding customer data to Queue..");
        customerServiceProducerEvent.createCustomerEvent(_customer);
        
        return new ResponseEntity<>(_customer, httpHeaders, HttpStatus.CREATED);
    }
}



@Component
class DBInfo {
	private String url;

	public DBInfo(DataSource dataSource) throws SQLException{
		this.url = dataSource.getConnection().getMetaData().getURL();
	}

	public String getUrl() {
		return url;
	}
}