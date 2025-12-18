package com.pattern.food_ordering_system.service.customer;

import com.pattern.food_ordering_system.entity.CartItem;
import com.pattern.food_ordering_system.entity.OrderItem;
import com.pattern.food_ordering_system.model.customer.*;
import com.pattern.food_ordering_system.model.user.Customer;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.repository.CustomerRepo;
import com.pattern.food_ordering_system.utils.exception.CartException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

    public static void loadCustomerOrders() {
        customer.setOrders(CustomerRepo.findOrdersByCustomerId(customer.getId()));
    }

    public static void clearCart() throws CartException {
        try {
            CustomerRepo.clearCartByCustomerId(customer.getId());
            customer.getCart().clear();
        } catch (SQLException e) {
            throw new CartException("Failed To Update DB");
        }
    }

    public static void createOrder(String address, PaymentMethod paymentMethod, double totalPriceWithFee) throws CartException {
        try {
            Cart cart = customer.getCart();

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cart.getCartItems()) {
                orderItems.add(new OrderItem(-1, cartItem.getFoodItem().getName(), cartItem.getFoodItem().getId(), cartItem.getQuantity(), cartItem.getFoodItem().getPrice()));
            }

            CustomerOrder order = new CustomerOrder();
            order.setCustomerId(customer.getId());
            order.setRestaurantId(cart.getRestaurantId());
            order.setItems(orderItems);
            order.setPaymentMethod(paymentMethod);
            order.setDeliveryAddress(address);
            order.setRestaurantName(cart.getRestaurantName());
            order.setOrderPrice(cart.getTotalPrice());
            order.setTotalPriceWithFee(totalPriceWithFee);

            CustomerRepo.insertOrder(order);
            List<CustomerOrder> orderList = customer.getOrders();
            orderList.add(order);
        } catch (SQLException e) {
            throw new CartException("Failed to create order: " + e.getMessage());
        }
    }
}
