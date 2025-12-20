package com.pattern.food_ordering_system.repository;

import com.pattern.food_ordering_system.config.DBConnection;
import com.pattern.food_ordering_system.entity.CartItem;
import com.pattern.food_ordering_system.entity.OrderItem;
import com.pattern.food_ordering_system.entity.Review;
import com.pattern.food_ordering_system.model.customer.*;
import com.pattern.food_ordering_system.model.restaurant.Menu;
import com.pattern.food_ordering_system.model.status.OrderStatus;
import com.pattern.food_ordering_system.model.status.StatusFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void insertOrder(CustomerOrder order) throws SQLException {
        String orderSql = """
                    INSERT INTO orders (restaurant_id, customer_id, total_price, payment_method, 
                                       delivery_address, order_time, status)
                    VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, order.getRestaurantId());
            pstmt.setLong(2, order.getCustomerId());
            pstmt.setDouble(3, order.getTotalPrice());
            pstmt.setString(4, order.getPaymentMethod().name());
            pstmt.setString(5, order.getDeliveryAddress());
            pstmt.setTimestamp(6, Timestamp.valueOf(order.getOrderTime()));
            pstmt.setString(7, order.getStatus().toString());

            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setOrderId(generatedKeys.getLong(1));
            }

            insertOrderItems(order);
            clearCartByCustomerId(order.getCustomerId());
        }
    }

    private static void insertOrderItems(CustomerOrder order) throws SQLException {
        String itemSql = """
                    INSERT INTO order_items (order_id, food_item_id, quantity, price)
                    VALUES (?, ?, ?, ?);
                """;

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(itemSql)) {
            for (OrderItem orderItem : order.getItems()) {
                pstmt.setLong(1, order.getOrderId());
                pstmt.setLong(2, orderItem.getFoodItemId());
                pstmt.setInt(3, orderItem.getQuantity());
                pstmt.setDouble(4, orderItem.getPrice());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public static List<CustomerOrder> findOrdersByCustomerId(long customerId) {
        List<CustomerOrder> orders = new ArrayList<>();

        String sql = """
                    SELECT o.*, u.username as restaurant_name, u.image_path as restaurant_logo
                    FROM orders o
                    LEFT JOIN users u ON o.restaurant_id = u.user_id
                    WHERE o.customer_id = ?
                    ORDER BY o.order_time DESC
                """;

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {

            pstmt.setLong(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                long currentOrderId = rs.getLong("order_id");

                List<OrderItem> orderItems = findItemsByOrderId(currentOrderId);
                Review review = ReviewRepo.getReviewByOrderId(currentOrderId);

                CustomerOrder order = new CustomerOrder();
                order.setItems(orderItems);
                order.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                order.setRestaurantName(rs.getString("restaurant_name"));
                order.setTotalPriceWithFee(rs.getDouble("total_price"));
                order.setRestaurantLogo(rs.getString("restaurant_logo"));
                order.setOrderId(rs.getLong("order_id"));
                order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                order.setRestaurantId(rs.getLong("restaurant_id"));
                order.setReview(review);

                if (rs.getTimestamp("order_time") != null) {
                    order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
                }

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private static List<OrderItem> findItemsByOrderId(long orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = """
                    SELECT oi.quantity, m.* FROM order_items oi
                    JOIN menus m ON oi.food_item_id = m.food_item_id
                    WHERE oi.order_id = ?
                """;

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {

            pstmt.setLong(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderItem orderItem = new OrderItem(
                        -1,
                        rs.getString("food_item_name"),
                        rs.getLong("food_item_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                items.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}