package repository.user;

import model.User;
import model.validator.Notification;

import java.util.*;

public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);

    Notification<Boolean> save(User user);

    void removeAll();

    boolean existsByUsername(String username);

    User findByUsername(String username);
    User findById(Long id);

    List<User> findEmployees();

    boolean updateUsername(Long id, String username);
    boolean updateRole(Long id, String role);
    boolean deleteUser(String username);
}