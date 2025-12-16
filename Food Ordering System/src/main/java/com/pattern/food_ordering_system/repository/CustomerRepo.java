package com.pattern.food_ordering_system.repository;

import com.pattern.food_ordering_system.config.DBConnection;
import com.pattern.food_ordering_system.model.customer.Cart;
import com.pattern.food_ordering_system.model.customer.CartItem;
import com.pattern.food_ordering_system.model.customer.FoodItem;
import com.pattern.food_ordering_system.model.restaurant.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepo {

    public static Menu findAllFoodItems() {
        Menu menu = new Menu();

        String sql = """
                    SELECT food_item.*, 
                           user.username AS restaurant_name, 
                           user.user_id AS restaurant_id, 
                           user.zone AS restaurant_zone,
                           rest.rate AS restaurant_rate
                    FROM menus food_item
                    JOIN users user
                        ON food_item.restaurant_id = user.user_id
                    JOIN restaurants rest
                        ON food_item.restaurant_id = rest.restaurant_id
                    WHERE food_item.availability = 1
                """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                FoodItem foodItem = new FoodItem();
                foodItem.setId(rs.getLong("food_item_id"));
                foodItem.setRestaurantId(rs.getLong("restaurant_id"));
                foodItem.setCategory(rs.getString("category"));
                foodItem.setName(rs.getString("food_item_name"));
                foodItem.setPrice(rs.getDouble("price"));
                foodItem.setDescription(rs.getString("description"));
                foodItem.setImagePath(rs.getString("food_item_image"));
                foodItem.setAvailable(rs.getBoolean("availability"));
                foodItem.setRestaurantName(rs.getString("restaurant_name"));
                foodItem.setRating(rs.getDouble("restaurant_rate"));
                foodItem.setLocation(rs.getString("restaurant_zone"));

                menu.add(foodItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menu;
    }


    public static void updateCartItem(long customerId, long foodItemId, int quantity) throws SQLException {
        String sql = """
                    INSERT INTO cart (customer_id, food_item_id, quantity)
                    VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                    quantity = VALUES(quantity);
                """;
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, customerId);
        pstmt.setLong(2, foodItemId);
        pstmt.setInt(3, quantity);
        pstmt.executeUpdate();
    }

    public static void deleteCartItemByFoodIdAndCustomerId(long foodItemId, long customerItemId) throws SQLException {
        String sql = "DELETE FROM cart WHERE food_item_id = ? AND customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, foodItemId);
            stmt.setLong(2, customerItemId);
           stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearCartByCustomerId(long customerItemId) throws SQLException {
        String sql = "DELETE FROM cart WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, customerItemId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Cart getCustomerCartById(long id) {
        Cart cart = new Cart();
        FoodItem foodItem;
        CartItem cartItem;

        String sql = """
                    SELECT cart_item.*, food_item.*, user.username AS restaurant_name
                    FROM cart cart_item
                    JOIN menus food_item
                        ON food_item.food_item_id = cart_item.food_item_id
                    JOIN users user
                        ON food_item.restaurant_id = user.user_id
                    WHERE cart_item.customer_id = ?
                """;


        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                foodItem = new FoodItem();
                foodItem.setId(rs.getLong("food_item_id"));
                foodItem.setRestaurantId(rs.getLong("restaurant_id"));
                foodItem.setCategory(rs.getString("category"));
                foodItem.setName(rs.getString("food_item_name"));
                foodItem.setPrice(rs.getDouble("price"));
                foodItem.setDescription(rs.getString("description"));
                foodItem.setImagePath(rs.getString("food_item_image"));
                foodItem.setAvailable(rs.getBoolean("availability"));
                foodItem.setRestaurantName(rs.getString("restaurant_name"));
                cartItem = new CartItem(foodItem, rs.getInt("quantity"));
                if (!foodItem.isAvailable()) cartItem.setQuantity(0);
                cart.addCartItem(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }


//    public static List<String> getCategories
}