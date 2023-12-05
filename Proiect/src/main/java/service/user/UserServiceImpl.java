package service.user;


import model.User;
import repository.user.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    @Override
    public List<User> findEmployees(){
        return userRepository.findEmployees();
    }
    @Override
    public User findById(Long id){
        return userRepository.findById(id);
    }

    @Override
    public boolean updateRole(Long id, String role){
        return userRepository.updateRole(id,role);
    }

    @Override
    public boolean updateUsername(Long id, String username){
        return userRepository.updateUsername(id,username);
    }

    @Override
    public boolean deleteUser(String username){
        return userRepository.deleteUser(username);
    }
}
