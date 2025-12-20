package com.pattern.food_ordering_system.model.status;

import java.util.Map;

public class StatusFactory {
    private static Map<OrderStatus, Status> statusMap;

    static {
        statusMap.put(OrderStatus.ORDER_PLACED, new OrderPlacedStatus());
        statusMap.put(OrderStatus.CONFIRMED, new ConfirmedStatus());
        statusMap.put(OrderStatus.BEING_PREPARED, new BeingPreparedStatus());
        statusMap.put(OrderStatus.OUT_FOR_DELIVERY, new OurForDeliveryStatus());
        statusMap.put(OrderStatus.READY_FOR_DELIVERY, new ReadyForDeliveryStatus());
        statusMap.put(OrderStatus.DELIVERED, new DeliveredStatus());
    }

    private StatusFactory() {
    }

    public static Status getOrderStatusObj(OrderStatus orderStatus) {
        return statusMap.get(orderStatus);
    }
}
