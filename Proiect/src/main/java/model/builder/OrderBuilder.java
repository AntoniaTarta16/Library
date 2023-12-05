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

    public OrderBuilder setUserId(Long userId) {
        order.setUserId(userId);
        return this;
    }



    public OrderBuilder setCustomerId(Long customerId) {
        order.setCustomerId(customerId);
        return this;
    }


    public OrderBuilder setBookId(Long bookId) {
        order.setBookId(bookId);
        return this;
    }

    public OrderBuilder setQuantity(int quantity){
        order.setQuantity(quantity);
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
