package com.ilnur.jdbc;

import com.ilnur.jdbc.dao.CustomerDao;
import com.ilnur.jdbc.entity.Customer;

import java.time.LocalDate;

public class DaoRunner {

    public static void main(String[] args) {
        var customerDao = CustomerDao.getInstance();
        var customer = new Customer();
        customer.setFirstName("Lenar");
        customer.setLastName("Idrisov");
        customer.setEmail("lenarID@gmail.com");
        customer.setBirthdate(LocalDate.of(1994, 4, 1));
        customer.setSex("Male");
        customer.setCity("Kazan");

        var savedTicket = customerDao.save(customer);
        System.out.println(savedTicket);
    }
}
