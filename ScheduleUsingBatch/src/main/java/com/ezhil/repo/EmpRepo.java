package com.ezhil.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezhil.model.Employee;

@Repository
public interface EmpRepo extends JpaRepository<Employee, Integer> {

}
