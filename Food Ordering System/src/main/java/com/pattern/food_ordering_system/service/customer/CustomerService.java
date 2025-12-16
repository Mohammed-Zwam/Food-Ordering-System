package com.pattern.food_ordering_system.service.customer;

import com.pattern.food_ordering_system.model.customer.CartProxy;
import com.pattern.food_ordering_system.model.customer.FoodItem;
import com.pattern.food_ordering_system.model.user.Customer;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.repository.CustomerRepo;
import com.pattern.food_ordering_system.utils.exception.CartException;

import java.sql.SQLException;


public class CustomerService {
    private static final Customer customer = (Customer) UserFactory.getUser();

    public static void updateCartItem(FoodItem foodItem, int change, int oldQuantity /* FOR ROLLBACK */) throws CartException {
        CartProxy cartProxy = new CartProxy(customer.getCart());
        change = cartProxy.updateCartItem(foodItem, change);  // Update local cart
        try {
            // Update DB
            if (change == 0) CustomerRepo.deleteCartItemByFoodIdAndCustomerId(foodItem.getId(), customer.getId());
            else CustomerRepo.updateCartItem(customer.getId(), foodItem.getId(), change);
        } catch (SQLException e) {
            customer.getCart().setCartItemQuantity(foodItem.getId(), oldQuantity); // rollback <real system ;)>
            throw new CartException("Failed To Update DB");
        }
    }

    public static void loadCustomerCart() {
        customer.setCart(CustomerRepo.getCustomerCartById(customer.getId()));
        customer.getCart().setCartRestaurantInfo();
    }

    public static void clearCart() throws CartException {
        try {
            CustomerRepo.clearCartByCustomerId(customer.getId());
            customer.getCart().clear();
        } catch (SQLException e) {
            throw new CartException("Failed To Update DB");
        }
    }
}
