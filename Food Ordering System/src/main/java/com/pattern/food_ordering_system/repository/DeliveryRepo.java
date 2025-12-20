package com.pattern.food_ordering_system.repository;

import com.pattern.food_ordering_system.config.DBConnection;
import com.pattern.food_ordering_system.model.status.OrderStatus;
import com.pattern.food_ordering_system.model.customer.PaymentMethod;
import com.pattern.food_ordering_system.model.delivery.DeliveryOrder;
import com.pattern.food_ordering_system.model.user.UserFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryRepo {

    public static List<DeliveryOrder> findPendingDeliveries() {
        List<DeliveryOrder> orders = new ArrayList<>();
        long driverId = UserFactory.getUser().getId();


        boolean isBusy = hasActiveOrder(driverId);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.*, ");
        sql.append("       c.username AS customer_name, ");
        sql.append("       r.username AS restaurant_name,");
        sql.append("       r.zone AS restaurant_address ");
        sql.append("FROM orders o ");
        sql.append("JOIN users c ON o.customer_id = c.user_id ");
        sql.append("JOIN users r ON o.restaurant_id = r.user_id ");

        if (isBusy) {
            sql.append("WHERE o.delivery_id = ? AND o.status = 'OUT_FOR_DELIVERY'");
        } else {
            sql.append("WHERE o.status = 'READY_FOR_DELIVERY'");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            if (isBusy) {
                pstmt.setLong(1, driverId);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                DeliveryOrder dOrder = new DeliveryOrder();
                dOrder.setOrderId(rs.getLong("order_id"));
                dOrder.setStatus(OrderStatus.valueOf(rs.getString("status")));
                dOrder.setTotalPrice(rs.getDouble("total_price"));
                dOrder.setCustomerName(rs.getString("customer_name"));
                dOrder.setDeliveryAddress(rs.getString("delivery_address"));
                dOrder.setRestaurantName(rs.getString("restaurant_name"));
                dOrder.setRestaurantAddress(rs.getString("restaurant_address"));

                try {
                    dOrder.setDeliveryFee(rs.getDouble("delivery_fee"));
                } catch (SQLException e) {
                    dOrder.setDeliveryFee(0.0);
                }

                String pMethod = rs.getString("payment_method");
                dOrder.setPaymentMethod(pMethod != null ? PaymentMethod.valueOf(pMethod) : PaymentMethod.CASH);

                orders.add(dOrder);
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
        String sql = "UPDATE orders SET status = 'OUT_FOR_DELIVERY', delivery_id = ? WHERE order_id = ? AND status = 'READY_FOR_DELIVERY'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, driverId);
            pstmt.setLong(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}