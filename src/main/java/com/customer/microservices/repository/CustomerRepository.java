package com.customer.microservices.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.customer.microservices.domain.Customer;

import java.sql.Date;

import java.util.List;

@Repository
public class CustomerRepository {

	private final JdbcTemplate jdbcTemplate;

    private final String SQL_INSERT = "insert into customer(id,email,first_name,last_name,created,modified) values(?,?,?,?,?,?)";
    private final String SQL_QUERY_ALL = "select * from customer";
    private final String SQL_QUERY_BY_ID = "select * from customer where id=?";

    private final RowMapper<Customer> rowMapper = (ResultSet rs, int row) -> {
        Customer customer = new Customer();
        customer.setId(rs.getString("id"));
        customer.setEmail(rs.getString("email"));
        customer.setFirst_name(rs.getString("first_name"));
        customer.setLast_name(rs.getString("last_name"));
        customer.setCreated(rs.getDate("created"));
        customer.setModified(rs.getDate("modified"));
        return customer;
    };

    @Autowired
    public CustomerRepository(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    public Customer save(Customer customer) {
        assert customer.getEmail() != null;
        assert customer.getFirst_name() != null;
        assert customer.getLast_name() != null;

        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
            ps.setString(1, customer.getId());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getFirst_name());
            ps.setString(4, customer.getLast_name());
            ps.setDate(5, new Date(customer.getCreated().getTime()));
            ps.setDate(6, new Date(customer.getModified().getTime()));
            return ps;
        });

        return customer;
    }

    public List<Customer> findAll() {
        return this.jdbcTemplate.query(SQL_QUERY_ALL, rowMapper);
    }

    public Customer findOne(String id) {
        return this.jdbcTemplate.queryForObject(SQL_QUERY_BY_ID, new Object[]{id}, rowMapper);
    }
}
