package gsm.service;

import java.util.List;

import gsm.entities.User;

public interface UserService {
public List <User> getAllUsers();
public User findUserById(Long id);
public User createUser(User user);
public User updateUser(User user);
public void deleteUser(Long id);
public boolean isUserExists(String email);
public User updateUserById(Long id, User updatedUser);
public User getUserByMailAndPassword(String email, String password);
}
