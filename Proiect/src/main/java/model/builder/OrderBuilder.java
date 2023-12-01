package model.builder;

import model.Book;
import model.CartItem;
import model.Order;
import model.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class OrderBuilder {
    private Order order;
    public OrderBuilder(){
        order = new Order();
    }

    public OrderBuilder setId(Long id){
        order.setId(id);
        return this;
    }

    public OrderBuilder setUser(User user) {
        order.setUser(user);
        return this;
    }



    public OrderBuilder setCustomer(User customer) {
        order.setUser(customer);
        return this;
    }


    public OrderBuilder setCartItems(ArrayList<CartItem> cartItems) {
        order.setCartItems(cartItems);
        return this;
    }


    public OrderBuilder setTotal(int total) {
        order.setTotal(total);
        return this;
    }

    public Order build(){
        return order;
    }
}
