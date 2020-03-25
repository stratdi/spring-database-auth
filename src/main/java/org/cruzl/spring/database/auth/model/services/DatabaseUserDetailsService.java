package org.cruzl.spring.database.auth.model.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.cruzl.spring.database.auth.model.Role;
import org.cruzl.spring.database.auth.model.User;
import org.cruzl.spring.database.auth.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		log.info("Checking if username '{}' exists", username);
		Optional<User> user = this.userRepository.findByUserName(username);

		if (!user.isPresent()) {
			log.error("Username not found: {}", username);
			throw new UsernameNotFoundException(username);
		}

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (Role role : user.get().getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		return new org.springframework.security.core.userdetails.User(user.get().getUserName(),
				this.passwordEncoder.encode(user.get().getPassword()), grantedAuthorities);
	}
}