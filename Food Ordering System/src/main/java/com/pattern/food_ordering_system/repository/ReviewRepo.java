package com.pattern.food_ordering_system.repository;

import com.pattern.food_ordering_system.config.DBConnection;
import com.pattern.food_ordering_system.entity.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRepo {

    public static boolean addReview(long orderId, long customerId, long restaurantId, Review review) {
        Connection conn = null;
        PreparedStatement pstmtInsert = null;
        PreparedStatement pstmtUpdate = null;


        String sqlInsert = "INSERT INTO reviews (order_id, customer_id, restaurant_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?, NOW())";


        String sqlUpdateRestaurant = """
                    UPDATE restaurants 
                    SET rate = (SELECT AVG(rating) FROM reviews WHERE restaurant_id = ?) 
                    WHERE restaurant_id = ?
                """;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtInsert.setLong(1, orderId);
            pstmtInsert.setLong(2, customerId);
            pstmtInsert.setLong(3, restaurantId);
            pstmtInsert.setDouble(4, review.getRating());
            pstmtInsert.setString(5, review.getComment());
            pstmtInsert.executeUpdate();


            pstmtUpdate = conn.prepareStatement(sqlUpdateRestaurant);
            pstmtUpdate.setLong(1, restaurantId);
            pstmtUpdate.setLong(2, restaurantId);
            pstmtUpdate.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (pstmtInsert != null) pstmtInsert.close();
            } catch (SQLException e) {
            }
            try {
                if (pstmtUpdate != null) pstmtUpdate.close();
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public static Review getReviewByOrderId(long orderId) {
        String sql = "SELECT rating, comment FROM reviews WHERE order_id = ?";

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {

            pstmt.setLong(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Review r = new Review(
                        rs.getDouble("rating"),
                        rs.getString("comment")
                );
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}