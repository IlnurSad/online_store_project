package com.ilnur.jdbc.dao;

import com.ilnur.jdbc.dto.OrdersFilter;
import com.ilnur.jdbc.entity.Orders;
import com.ilnur.jdbc.exception.DaoException;
import com.ilnur.jdbc.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdersDao {

    private static final OrdersDao INSTANCE = new OrdersDao();

    private static final String DELETE_SQL = """
            DELETE FROM store_catalog.orders
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO store_catalog.orders (customer_id, sum, or_created_at, status)
            VALUES (?, ?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE store_catalog.orders
            SET customer_id = ?,
                sum = ?,
                or_created_at = ?,
                status = ?
            WHERE id = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                customer_id,
                sum,
                or_created_at,
                status
            FROM store_catalog.orders
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private OrdersDao() {
    }

    public boolean delete(int id) {
        try (var connection = ConnectionManager.get(); var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Orders save(Orders orders) {
        try (var connection = ConnectionManager.get(); var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, orders.getCustomerId());
            preparedStatement.setBigDecimal(2, orders.getSum());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(orders.getOrCreatedAt()));
            preparedStatement.setString(4, orders.getStatus());
            preparedStatement.setLong(5, orders.getId());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orders.setId(generatedKeys.getLong("id"));
            }
            return orders;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public void update(Orders orders) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, orders.getCustomerId());
            preparedStatement.setBigDecimal(2, orders.getSum());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(orders.getOrCreatedAt()));
            preparedStatement.setString(4, orders.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<Orders> findAll(OrdersFilter filter) {
        List<Object> parameters = new ArrayList<>();
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        var sql = FIND_ALL_SQL + """
                LIMIT ?
                OFFSET ?
                """;
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            var resultSet = preparedStatement.executeQuery();
            List<Orders> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(buildOrders(resultSet));
            }
            return orders;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<Orders> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Orders> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(buildOrders(resultSet));
            }
            return orders;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<Orders> findById(int id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Orders orders = null;
            if (resultSet.next()) {
                orders = buildOrders(resultSet);
            }
            return Optional.ofNullable(orders);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public static OrdersDao getInstance() {
        return INSTANCE;
    }

    private static Orders buildOrders(ResultSet resultSet) throws SQLException {
        return new Orders(
                resultSet.getLong("id"),
                resultSet.getInt("customer_id"),
                resultSet.getBigDecimal("sum"),
                resultSet.getTimestamp("or_created_at").toLocalDateTime(),
                resultSet.getString("status")
        );
    }
}
