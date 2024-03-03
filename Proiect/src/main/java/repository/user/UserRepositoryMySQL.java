package repository.user;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {

        String sql = "SELECT * FROM user;";

        List<User> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                users.add(getUserFromResultSet(resultSet));
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean updateUsername(Long id, String username) {
        String sql = "UPDATE user SET username = ? WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setLong(2, id);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteUser(String username){
        String sql = "DELETE FROM user WHERE username = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRole(Long id, String role){
        String sql = "UPDATE user_role SET role_id=(SELECT id FROM role where role = ?) where user_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,role);
            preparedStatement.setLong(2, id);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // SQL Injection Attacks should not work after fixing functions
    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            PreparedStatement statement = connection
                    .prepareStatement(  "Select * from `" + USER + "` where `username`= ? and `password`= ?");
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet userResultSet =  statement.executeQuery();

            if (userResultSet.next())
            {
                User user = getUserFromResultSet(userResultSet);
                findByUsernameAndPasswordNotification.setResult(user);
            }
            else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        }
        catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public Notification<Boolean> save(User user) {

        Notification<Boolean> userSaveNotification = new Notification<>();
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());
            userSaveNotification.setResult(Boolean.TRUE);
        }
        catch (SQLException e) {
            System.out.println(e.toString());
            userSaveNotification.addError("Something is wrong with the Database!");
            userSaveNotification.setResult(Boolean.FALSE);
        }

        return userSaveNotification;

    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<User> findEmployees(){
        List<User> employees = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT user.id, username FROM user, user_role, role WHERE user.id = user_role.id and user_role.role_id = role.id and role = \"employee\";";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                User user = new UserBuilder()
                        .setId(id)
                        .setUsername(username)
                        .build();
                employees.add(user);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement(   "Select * from `" + USER + "` where `username`= ?");
            statement.setString(1, email);

            ResultSet userResultSet = statement.executeQuery();

            return userResultSet.next();

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findByUsername(String username){
        User user = null;
        try {
            PreparedStatement statement = connection
                    .prepareStatement(   "Select * from `" + USER + "` where `username`= ?");
            statement.setString(1, username);

            ResultSet userResultSet = statement.executeQuery();
            if (userResultSet.next())
            {
                 user = getUserFromResultSet(userResultSet);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User findById(Long id){
        User user = null;
        try {
            PreparedStatement statement = connection
                    .prepareStatement(   "Select * from `" + USER + "` where `id`= ?");
            statement.setLong(1, id);

            ResultSet userResultSet = statement.executeQuery();
            if (userResultSet.next())
            {
                user = getUserFromResultSet(userResultSet);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException{
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                .build();
    }

}
