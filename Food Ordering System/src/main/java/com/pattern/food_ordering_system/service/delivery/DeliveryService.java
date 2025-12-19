package com.pattern.food_ordering_system.service.delivery;

import com.pattern.food_ordering_system.model.customer.OrderStatus;
import com.pattern.food_ordering_system.model.delivery.DeliveryOrder;
import com.pattern.food_ordering_system.model.user.Delivery;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.repository.DeliveryRepo;

import java.util.List;

public class DeliveryService {

    public static List<DeliveryOrder> getPendingOrders() {
        return DeliveryRepo.findPendingDeliveries();
    }

    public static void pickupOrder(long orderId) {
        Delivery driver = (Delivery) UserFactory.getUser();

        if (DeliveryRepo.hasActiveOrder(driver.getId())) {
            throw new RuntimeException("You already have an active delivery. Finish it first!");
        }

        boolean success = DeliveryRepo.assignOrderToDriver(orderId, driver.getId());
        if (!success) {
            throw new RuntimeException("Order is no longer available or was taken by another driver.");
        }
    }

    public static void markAsDelivered(long orderId) {
        if (!DeliveryRepo.updateOrderStatus(orderId, OrderStatus.DELIVERED)) {
            throw new RuntimeException("Failed to update order status.");
        }
    }
}