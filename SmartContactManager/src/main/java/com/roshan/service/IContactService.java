package com.roshan.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.roshan.entity.Contacts;
import com.roshan.entity.Users;

public interface IContactService {

	Contacts save(Contacts contact);

	Contacts update(Contacts contact);

	List<Contacts> getAll();

	Contacts getById(String id);

	boolean delete(String id);

	Page<Contacts> searchByName(String name, int size, int page, String sortBy, String order, Users user);

	Page<Contacts> searchByEmail(String email, int size, int page, String sortBy, String order, Users user);

	Page<Contacts> searchByPhone(String phone, int size, int page, String sortBy, String order, Users user);

	List<Contacts> getByUserId(String userId);

	Page<Contacts> getByUser(Users users, int page, int size, String sortBy, String direction);

}
