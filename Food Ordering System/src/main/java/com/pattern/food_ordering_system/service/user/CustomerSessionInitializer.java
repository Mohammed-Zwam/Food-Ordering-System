package com.pattern.food_ordering_system.service.user;

import com.pattern.food_ordering_system.model.user.Customer;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.repository.CustomerRepo;
import com.pattern.food_ordering_system.service.customer.CustomerService;

public class CustomerSessionInitializer implements UserSessionInitializer {
    @Override
    public String getTargetScene() {
        return "customer-views/customer-view";
    }

    @Override
    public void loadUserData() {
        CustomerService.loadCustomerCart();
        CustomerService.loadCustomerOrders();
    }
}
