package com.pattern.food_ordering_system.repository;

import com.pattern.food_ordering_system.config.DBConnection;
import com.pattern.food_ordering_system.model.customer.CustomerOrder;
import com.pattern.food_ordering_system.model.customer.OrderStatus;
import com.pattern.food_ordering_system.model.customer.PaymentMethod;
import com.pattern.food_ordering_system.model.user.UserFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryRepo {

    public static List<CustomerOrder> findPendingDeliveries() {
        List<CustomerOrder> orders = new ArrayList<>();
        long driverId = UserFactory.getUser().getId();


        String sql = """
            SELECT o.*, 
                   c.username AS customer_name, 
                   r.username AS restaurant_name,
                   r.zone AS restaurant_address 
            FROM orders o
            JOIN users c ON o.customer_id = c.user_id
            JOIN users r ON o.restaurant_id = r.user_id
            WHERE o.status = 'BEING_PREPARED' 
               OR (o.delivery_id = ? AND o.status = 'OUT_FOR_DELIVERY')
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, driverId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CustomerOrder order = new CustomerOrder();
                order.setOrderId(rs.getLong("order_id"));
                order.setDeliveryAddress(rs.getString("delivery_address"));
                order.setRestaurantAddress(rs.getString("restaurant_address"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setRestaurantName(rs.getString("restaurant_name"));
                double total = rs.getDouble("total_price");
                order.setTotalPriceWithFee(total);

                try {
                    order.setDeliveryFee(rs.getDouble("delivery_fee"));
                } catch (SQLException e) {
                    order.setDeliveryFee(0.0);
                }

                order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                String pMethod = rs.getString("payment_method");
                order.setPaymentMethod(pMethod != null ? PaymentMethod.valueOf(pMethod) : PaymentMethod.CASH);

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static boolean hasActiveOrder(long driverId) {
        String sql = "SELECT COUNT(*) FROM orders WHERE delivery_id = ? AND status = 'OUT_FOR_DELIVERY'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, driverId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean assignOrderToDriver(long orderId, long driverId) {
        String sql = "UPDATE orders SET status = 'OUT_FOR_DELIVERY', delivery_id = ? WHERE order_id = ? AND status = 'BEING_PREPARED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, driverId);
            pstmt.setLong(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean markAsDelivered(long orderId) {
        return updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    public static boolean updateOrderStatus(long orderId, OrderStatus newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus.toString());
            pstmt.setLong(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}