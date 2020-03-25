package org.cruzl.spring.database.auth.model.services;

import java.util.Optional;

import org.cruzl.spring.database.auth.model.User;
import org.cruzl.spring.database.auth.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.NonNull;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	@Cacheable("users")
	public Optional<User> get(@NonNull final String username) {
		return this.repository.findByUserName(username);
	}
}
