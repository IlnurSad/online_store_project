package com.ilnur.jdbc.dao;

import com.ilnur.jdbc.dto.ReviewFilter;
import com.ilnur.jdbc.entity.Review;
import com.ilnur.jdbc.exception.DaoException;
import com.ilnur.jdbc.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDao {

    private static final ReviewDao INSTANCE = new ReviewDao();

    private static final String DELETE_SQL = """
            DELETE FROM store_catalog.review
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO store_catalog.review (product_id, costumer_id, content_rv, rv_created_at)
            VALUES (?, ?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE store_catalog.review
            SET product_id = ?,
                costumer_id = ?,
                content_rv = ?,
                rv_created_at = ?
            WHERE id = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                product_id,
                costumer_id,
                content_rv,
                rv_created_at
            FROM store_catalog.review
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private ReviewDao() {
    }

    public boolean delete(int id) {
        try (var connection = ConnectionManager.get(); var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Review save(Review review) {
        try (var connection = ConnectionManager.get(); var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, review.getProductId());
            preparedStatement.setInt(2, review.getCostumerId());
            preparedStatement.setString(3, review.getContentRv());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(review.getRvCreatedAt()));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                review.setId(generatedKeys.getLong("id"));
            }
            return review;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public void update(Review review) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setLong(1, review.getProductId());
            preparedStatement.setInt(2, review.getCostumerId());
            preparedStatement.setString(3, review.getContentRv());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(review.getRvCreatedAt()));
            preparedStatement.setLong(5, review.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<Review> findAll(ReviewFilter filter) {
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
            List<Review> reviews = new ArrayList<>();
            while (resultSet.next()) {
                reviews.add(buildReview(resultSet));
            }
            return reviews;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<Review> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while (resultSet.next()) {
                reviews.add(buildReview(resultSet));
            }
            return reviews;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<Review> findById(int id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Review review = null;
            if (resultSet.next()) {
                review = buildReview(resultSet);
            }
            return Optional.ofNullable(review);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public static ReviewDao getInstance() {
        return INSTANCE;
    }

    private static Review buildReview(ResultSet resultSet) throws SQLException {
        return new Review(
                resultSet.getLong("id"),
                resultSet.getLong("product_id"),
                resultSet.getInt("customer_id"),
                resultSet.getString("content_rv"),
                resultSet.getTimestamp("rv_created_at").toLocalDateTime()
        );
    }
}
