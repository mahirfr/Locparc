package com.mahir.locparc.dao;

import com.mahir.locparc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);
}
