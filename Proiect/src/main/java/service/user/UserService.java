package service.user;


import model.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findByUsername(String username);
    User findById(Long id);
    List<User> findEmployees();

    boolean updateRole(Long id, String username);
    boolean updateUsername(Long id, String username);

    boolean deleteUser(String username);
}
