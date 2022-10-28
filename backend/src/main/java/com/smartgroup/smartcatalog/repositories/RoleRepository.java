package com.smartgroup.smartcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartgroup.smartcatalog.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
