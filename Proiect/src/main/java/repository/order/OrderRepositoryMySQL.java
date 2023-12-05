package repository.order;

import model.Book;
import model.Order;
import model.builder.BookBuilder;
import model.builder.OrderBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryMySQL implements OrderRepository{

    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection){
        this.connection = connection;
    }
    @Override
    public boolean save(Order order) {
        String sql = "INSERT INTO _order VALUES(null, ?, ?, ?, ?, ?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, order.getUserId().intValue());
            preparedStatement.setInt(2, order.getCustomerId().intValue());
            preparedStatement.setInt(3, order.getBookId().intValue());
            preparedStatement.setInt(4, order.getQuantity());
            preparedStatement.setInt(5, order.getTotal());

            int rowsInserted = preparedStatement.executeUpdate();

            return (rowsInserted != 1) ? false : true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<Order> findOrderByEmployee(Long employeeId){
        String sql = "SELECT * FROM _order WHERE userId = " + employeeId;

        List<Order> orders = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                orders.add(getOrderFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException{
        return new OrderBuilder()
                .setId(resultSet.getLong("id"))
                .setUserId(resultSet.getLong("userId"))
                .setCustomerId(resultSet.getLong("customerId"))
                .setBookId(resultSet.getLong("bookId"))
                .setQuantity(resultSet.getInt("quantity"))
                .setTotal(resultSet.getInt("total"))
                .build();
    }


}
