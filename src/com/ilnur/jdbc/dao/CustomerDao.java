package com.ilnur.jdbc.dao;

import com.ilnur.jdbc.entity.Customer;
import com.ilnur.jdbc.exception.DaoException;
import com.ilnur.jdbc.util.ConnectionManager;

import java.sql.*;

public class CustomerDao {

    private static final CustomerDao INSTANCE = new CustomerDao();
    private static final String DELETE_SQL = """
            DELETE FROM customer
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO customer (first_name, last_name, email, birthdate, sex, city) 
            VALUES (?, ?, ?, ?, ?, ?);
            """;
    private CustomerDao() {
    }

    public Customer save(Customer customer) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setDate(4, (Date) customer.getBirthdate());
            preparedStatement.setString(5, customer.getSex());
            preparedStatement.setString(6, customer.getCity());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                customer.setId(generatedKeys.getInt("id"));
            }
            return customer;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public boolean delete(int id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public static CustomerDao getInstance() {
        return INSTANCE;
    }
}
