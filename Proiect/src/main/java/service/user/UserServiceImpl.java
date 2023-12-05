package service.user;


import model.User;
import repository.book.BookRepository;
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
    public User findById(Long id){
        return userRepository.findById(id);
    }
}
