package com.roshan.repo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.roshan.entity.Users;
@Repository
public interface IUserRepo extends JpaRepository<Users, String> {
    // extra methods for db related operations
    // custom query methods
    // custom finder methods
    Optional<Users> findByEmail(String email);
}