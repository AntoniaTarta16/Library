package service.order;

import model.Order;
import repository.order.OrderRepository;

import java.util.List;

public class OrderServiceImpl implements OrderService{
    final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
    @Override
    public boolean save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findOrderByEmployee(Long employeeId){
        return orderRepository.findOrderByEmployee(employeeId);
    }
}
