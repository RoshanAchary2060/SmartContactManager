package com.roshan.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.roshan.entity.Contacts;
import com.roshan.entity.Users;

@Repository
public interface IContactRepo extends JpaRepository<Contacts, String> {
	// find the contacts by user
	Page<Contacts> findByUser(Users user, Pageable pageable);

	@Query("SELECT c FROM Contacts c WHERE c.id = :userId")
	List<Contacts> findByUserId(@Param("userId") String userId);

	Page<Contacts> findByUserAndNameContainingIgnoreCase(Users user, String name,Pageable pageable);

	Page<Contacts> findByUserAndEmailContainingIgnoreCase(Users user,String email, Pageable pageable);

	Page<Contacts> findByUserAndPhoneNumberContaining( Users user,String phone, Pageable pageable);

}
