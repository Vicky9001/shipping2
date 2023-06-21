package com.example.shipping.repository;

import java.util.Optional;

import com.example.shipping.models.ERole;
import com.example.shipping.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRoleName(ERole roleName);
}
