package com.example.shipping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shipping.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	List<User> findByUsernameContainingAndRealNameContaining(String username, String realName);

	List<User> findByUsernameContaining(String username);

	User findByPhone(String phone);

	Optional<User> findById(Long id);

	Boolean existsByUsername(String username);

	Boolean existsByPhone(String phone);

	User save(User user);

	boolean existsById(Long id);

	void deleteInBatch(Iterable<User> iterable);
}
