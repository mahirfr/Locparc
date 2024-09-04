package com.mahir.locparc.dao;

import com.mahir.locparc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("FROM User u " +
            "WHERE u.role.name = 'ROLE_ADMIN'")
    Optional<List<User>> findAllAdmins();

    @Query("FROM User u " +
            "WHERE u.role.name = 'ROLE_LENDER'")
    Optional<List<User>> findAllLenders();
}
