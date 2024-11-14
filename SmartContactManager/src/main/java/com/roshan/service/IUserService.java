package com.roshan.service;
import java.util.List;
import com.roshan.entity.Users;
public interface IUserService {
    Users saveUser(Users user);
    Users getUserById(String id);

    Users getUserByEmail(String email);
    Users updateUser(Users user);
    boolean deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    List<Users> getAllUsers();

    // add more methods here related to user service
}