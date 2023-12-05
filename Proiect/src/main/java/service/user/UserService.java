package service.user;


import model.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findByUsername(String username);
    User findById(Long id);
}
