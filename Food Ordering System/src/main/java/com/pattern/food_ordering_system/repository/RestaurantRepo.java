package com.pattern.food_ordering_system.repository;

import com.pattern.food_ordering_system.config.DBConnection;
import com.pattern.food_ordering_system.entity.MenuItem;
import com.pattern.food_ordering_system.entity.OrderItem;
import com.pattern.food_ordering_system.model.status.OrderStatus;
import com.pattern.food_ordering_system.model.customer.PaymentMethod;
import com.pattern.food_ordering_system.model.restaurant.Menu;
import com.pattern.food_ordering_system.model.restaurant.RestaurantOrder;
import com.pattern.food_ordering_system.model.status.Status;
import com.pattern.food_ordering_system.model.status.StatusFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RestaurantRepo {

    public static long addFoodItem(long restaurantId, String category, MenuItem foodItem) {
        long newFoodItemId = -1;
        String sql = "INSERT INTO menus (restaurant_id, category, food_item_name, price, description, food_item_image, availability) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setLong(1, restaurantId);
            pstmt.setString(2, category);
            pstmt.setString(3, foodItem.getName());
            pstmt.setDouble(4, foodItem.getPrice());
            pstmt.setString(5, foodItem.getDescription());
            pstmt.setString(6, foodItem.getImagePath());
            pstmt.setBoolean(7, foodItem.isAvailable());


            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newFoodItemId = generatedKeys.getLong(1);
                    }
                }
            }

            return newFoodItemId;

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public static Menu getMenuByRestaurantId(long restaurantId) {
        Menu menu = new Menu("ROOT");

        HashMap<String, Menu> menuCategories = new HashMap<>();

        String sql = "SELECT food_item_id, food_item_name, category, price, description, food_item_image, availability FROM menus WHERE restaurant_id = ?";
        String category;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                category = rs.getString("category");
                Menu menuCategory = menuCategories.getOrDefault(category, null);
                if (menuCategory == null) {
                    menuCategory = new Menu(category);
                    menu.add(menuCategory);
                    menuCategories.put(category, menuCategory);
                }
                MenuItem menuItem = new MenuItem(
                        rs.getInt("food_item_id"),
                        rs.getString("food_item_name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("food_item_image"),
                        rs.getBoolean("availability"));
                menuItem.setParent(menuCategory);
                menuCategory.add(menuItem);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu: " + e.getMessage());
        }
        return menu;
    }

    public static int findAvgRateByRestaurantId(long restaurantId) {
        try {
            String sql = "SELECT rate FROM restaurants WHERE restaurant_id=?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setLong(1, restaurantId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else return -1;
        } catch (SQLException e) {
            System.err.println("Error fetching menu: " + e.getMessage());
            return -1;
        }
    }

    public static void addRestaurantIdToRestaurants(long id) {
        String sql = "INSERT INTO restaurants(restaurant_id, rate) VALUES (?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setInt(2, 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateFoodItem(String category, MenuItem item) {
        String sql = "UPDATE menus SET food_item_name = ?, description = ?, price = ?, availability = ?, food_item_image = ?, category = ? WHERE food_item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setBoolean(4, item.isAvailable());
            stmt.setString(5, item.getImagePath());
            stmt.setString(6, category);
            stmt.setLong(7, item.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean deleteFoodItem(long id) {
        String sql = "DELETE FROM menus WHERE food_item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<RestaurantOrder> findOrdersByRestaurantId(long id) {
        List<RestaurantOrder> orders = new ArrayList<>();

        String ordersQuery = """
                    SELECT user.username as customer_name, ords.*
                    FROM orders ords
                    JOIN users user
                        ON ords.customer_id = user.user_id
                    WHERE ords.restaurant_id = ?
                """;

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(ordersQuery)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RestaurantOrder restaurantOrder = new RestaurantOrder();
                restaurantOrder.setCustomerName(rs.getString("customer_name"));
                restaurantOrder.setOrderId(rs.getLong("order_id"));
                restaurantOrder.setDeliveryAddress(rs.getString("delivery_address"));

                OrderStatus orderStatus = OrderStatus.valueOf(rs.getString("status"));
                restaurantOrder.setStatus(orderStatus); // TEMP
                Status status = StatusFactory.getOrderStatusObj(orderStatus);
                restaurantOrder.setOrderStatus(status);

                restaurantOrder.setOrderPrice(rs.getDouble("total_price") - rs.getDouble("delivery_fee"));
                restaurantOrder.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
                restaurantOrder.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                restaurantOrder.setReview(ReviewRepo.getReviewByOrderId(restaurantOrder.getOrderId()));

                orders.add(restaurantOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (RestaurantOrder restaurantOrder : orders) {
            restaurantOrder.setItems(findOrderItemsByOrderId(restaurantOrder.getOrderId()));
        }

        return orders;
    }

    public static List<OrderItem> findOrderItemsByOrderId(long id) {
        List<OrderItem> orderItems = new ArrayList<>();

        String ordersItemsQuery = """
                    SELECT order_item.*, food_item.food_item_name as food_item_name
                    FROM order_items order_item
                    JOIN menus food_item
                        ON order_item.food_item_id = food_item.food_item_id
                    WHERE order_item.order_id = ?
                """;


        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(ordersItemsQuery)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderItem orderItem = new OrderItem(
                        rs.getLong("order_item_id"),
                        rs.getString("food_item_name"),
                        rs.getLong("food_item_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );

                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

}