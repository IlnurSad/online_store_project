package com.ilnur.jdbc.dao;

import com.ilnur.jdbc.dto.CartFilter;
import com.ilnur.jdbc.entity.Cart;
import com.ilnur.jdbc.exception.DaoException;
import com.ilnur.jdbc.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartDao {

    private static final CartDao INSTANCE = new CartDao();

    private static final String SAVE_SQL = """
            INSERT INTO store_catalog.cart (order_id, product_id, number)
            VALUES (?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE store_catalog.cart
            SET product_id = ?,
                number = ?
            WHERE order_id = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT order_id,
                product_id,
                number
            FROM store_catalog.cart
            """;
    private static final String FIND_BY_ORDER_ID_SQL = FIND_ALL_SQL + """
            WHERE order_id = ?;
            """;

    private CartDao() {
    }

    public void save(Cart carts) {
        try (var connection = ConnectionManager.get(); var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setLong(1, carts.getOrderId());
            preparedStatement.setLong(2, carts.getProductId());
            preparedStatement.setInt(3, carts.getNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public void update(Cart carts) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setLong(1, carts.getOrderId());
            preparedStatement.setLong(2, carts.getProductId());
            preparedStatement.setInt(3, carts.getNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<Cart> findAll(CartFilter filter) {
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
            List<Cart> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(buildCart(resultSet));
            }
            return orders;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<Cart> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Cart> carts = new ArrayList<>();
            while (resultSet.next()) {
                carts.add(buildCart(resultSet));
            }
            return carts;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<Cart> findById(int id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ORDER_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Cart carts = null;
            if (resultSet.next()) {
                carts = buildCart(resultSet);
            }
            return Optional.ofNullable(carts);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public static CartDao getInstance() {
        return INSTANCE;
    }

    private static Cart buildCart(ResultSet resultSet) throws SQLException {
        return new Cart(
                resultSet.getLong("order_id"),
                resultSet.getLong("product_id"),
                resultSet.getInt("number")
        );
    }
}
