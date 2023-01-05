package com.ilnur.jdbc;

import com.ilnur.jdbc.dao.CustomerDao;
import com.ilnur.jdbc.entity.Customer;

import java.time.LocalDate;


public class DaoRunner {

    public static void main(String[] args) {
        var customers = CustomerDao.getInstance().findAll();
        System.out.println(customers);
    }

    private static void updateTest() {
        var customerDao = CustomerDao.getInstance();
        var maybeCustomer = customerDao.findById(2);
        System.out.println(maybeCustomer);

        maybeCustomer.ifPresent(customer -> {
            customer.setCity("Moscow");
            customerDao.update(customer);
        });
    }

    private static void deleteTest() {
        var customerDao = CustomerDao.getInstance();
        var deleteResult = customerDao.delete(1);
        System.out.println(deleteResult);
    }

    private static void saveTest() {
        var customerDao = CustomerDao.getInstance();
        var customer = new Customer();
        customer.setFirstName("Lenar");
        customer.setLastName("Idrisov");
        customer.setEmail("lenarID@gmail.com");
        customer.setBirthdate(LocalDate.of(1994, 4, 1));
        customer.setSex("Male");
        customer.setCity("Kazan");

        var savedCustomer = customerDao.save(customer);
        System.out.println(savedCustomer);
    }
}
