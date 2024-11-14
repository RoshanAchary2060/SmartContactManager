package com.roshan.service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.roshan.entity.Users;
import com.roshan.helper.AppConstants;
import com.roshan.repo.IUserRepo;
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Users saveUser(Users user) {
        // generate userId
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        return userRepo.save(user);

    } // end of method
    @Override
    public Users getUserById(String id) {

        return userRepo.findById(id).get();

    } // end of method
    @Override
    public Users updateUser(Users user) {
        return userRepo.save(user);
    }
    @Override
    public boolean deleteUser(String id) {

        Users user = userRepo.findById(id).get();
        if (user == null) {
            return false;
        } else {
            userRepo.delete(user);
            return true;
        }

    } // end of method
    @Override
    public boolean isUserExist(String userId) {

        Users user = userRepo.findById(userId).get();
        return user == null ? false : true;

    }// end of method
    @Override
    public boolean isUserExistByEmail(String email) {
        // Find user by email. Optional is used to avoid null pointer exceptions.
        Optional<Users> userOptional = userRepo.findByEmail(email);

        // Check if the user is present in the Optional
        return userOptional.isPresent();
    }
    @Override
    public List<Users> getAllUsers() {

        return userRepo.findAll();

    } // end of method
    @Override
    public Users getUserByEmail(String email) {
        return userRepo.findByEmail(email).get();
    }
} // end of class