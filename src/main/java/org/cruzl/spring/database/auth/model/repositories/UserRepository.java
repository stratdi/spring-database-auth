package org.cruzl.spring.database.auth.model.repositories;

import java.util.Optional;

import org.cruzl.spring.database.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lombok.NonNull;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserName(@NonNull String username);
}
