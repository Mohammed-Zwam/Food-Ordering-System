package com.pattern.food_ordering_system.service.customer;

import com.pattern.food_ordering_system.utils.Locations;



public class DeliveryTimeService {
    private static final double BASE_FEE = 15.0;
    private static final double COST_PER_MINUTE = 1.0;
    private static final double MIN_FEE = 20.0;
    private static final double ERROR_FEE = 30.0;

    public static double getDeliveryTimeInMinutes(String originZone, String destinationZone) {
        if (Locations.deliveryTimeCache.containsKey(originZone)) {
            return Locations.deliveryTimeCache.get(originZone).getOrDefault(destinationZone, 999.0);
        }
        return 999.0;
    }
    public static double calculateDeliveryFee(double minutes) {
        if (minutes >= 999) {
            return ERROR_FEE;
        }
        double fee = BASE_FEE + (minutes * COST_PER_MINUTE);
        fee = Math.max(fee, MIN_FEE);

        return Math.ceil(fee);
    }

}