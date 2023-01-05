package com.ilnur.jdbc.dao;

import com.ilnur.jdbc.entity.Customer;
import com.ilnur.jdbc.exception.DaoException;
import com.ilnur.jdbc.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDao {

    private static final CustomerDao INSTANCE = new CustomerDao();
    private static final String DELETE_SQL = """
            DELETE FROM store_catalog.customer
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO store_catalog.customer (first_name, last_name, email, birthdate, sex, city)
            VALUES (?, ?, ?, ?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE store_catalog.customer
            SET first_name = ?,
                last_name = ?,
                email = ?,
                birthdate = ?,
                sex = ?,
                city = ?
            WHERE id = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                first_name,
                last_name,
                email,
                birthdate,
                sex,
                city
            FROM store_catalog.customer
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private CustomerDao() {
    }

    public boolean delete(int id) {
        try (var connection = ConnectionManager.get(); var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Customer save(Customer customer) {
        try (var connection = ConnectionManager.get(); var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setDate(4, Date.valueOf(customer.getBirthdate()));
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

    public void update(Customer customer) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setDate(4, Date.valueOf(customer.getBirthdate()));
            preparedStatement.setString(5, customer.getSex());
            preparedStatement.setString(6, customer.getCity());
            preparedStatement.setInt(7, customer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private static Customer buildCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getDate("birthdate").toLocalDate(),
                resultSet.getString("sex"),
                resultSet.getString("city")
        );
    }

    public List<Customer> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Customer> customers= new ArrayList<>();
            while (resultSet.next()) {
                customers.add(buildCustomer(resultSet));
            }
            return customers;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<Customer> findById(int id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            Customer customer = null;
            if (resultSet.next()) {
                customer = buildCustomer(resultSet);
            }
            return Optional.ofNullable(customer);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public static CustomerDao getInstance() {
        return INSTANCE;
    }
}
