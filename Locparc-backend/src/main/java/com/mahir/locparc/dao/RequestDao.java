package com.mahir.locparc.dao;

import com.mahir.locparc.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDao extends JpaRepository<Request, Long> {
}
