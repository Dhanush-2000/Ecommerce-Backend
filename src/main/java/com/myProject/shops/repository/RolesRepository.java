package com.myProject.shops.repository;

import com.myProject.shops.model.Roles;
import com.myProject.shops.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {


    Roles findByName(String roles);

    boolean existsByName(String role);
}