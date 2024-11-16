package com.roshan.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ScrollPosition.Direction;
import org.springframework.stereotype.Service;

import com.roshan.entity.Contacts;
import com.roshan.entity.Users;
import com.roshan.repo.IContactRepo;

@Service
public class ContactServiceImpl implements IContactService {
	@Autowired
	IContactRepo repo;

	@Override
	public Contacts save(Contacts contact) {
		String contactId = UUID.randomUUID().toString();
		contact.setId(contactId);

		return repo.save(contact);
	}

	@Override
	public Contacts update(Contacts contact) {
		// TODO Auto-generated method stub
		return repo.save(contact);
	}

	@Override
	public List<Contacts> getAll() {
		return repo.findAll();
	}

	@Override
	public Contacts getById(String id) {
		return repo.findById(id).get();
	}

	@Override
	public boolean delete(String id) {
		Contacts contact = repo.findById(id).get();
		if (contact == null) {
			return false;
		}
		repo.delete(contact);
		return true;
	}

	@Override
	public List<Contacts> getByUserId(String userId) {
		return repo.findByUserId(userId);
	}

	@Override
	public Page<Contacts> getByUser(Users users, int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		var pageable = PageRequest.of(page, size, sort);

		return repo.findByUser(users, pageable);

	}

	@Override
	public Page<Contacts> searchByName(String name, int size, int page, String sortBy, String order, Users user) {

		Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		var pageable = PageRequest.of(page, size, sort);

		return repo.findByUserAndNameContainingIgnoreCase(user,name, pageable);
	}

	@Override
	public Page<Contacts> searchByEmail(String email, int size, int page, String sortBy, String order, Users user) {
		Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		var pageable = PageRequest.of(page, size, sort);

		return repo.findByUserAndEmailContainingIgnoreCase(user, email ,pageable);
	}

	@Override
	public Page<Contacts> searchByPhone(String phone, int size, int page, String sortBy, String order, Users user) {
		Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		var pageable = PageRequest.of(page, size, sort);

		return repo.findByUserAndPhoneNumberContaining(user, phone, pageable);
	}

}
