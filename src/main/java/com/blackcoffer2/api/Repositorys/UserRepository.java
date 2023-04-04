package com.blackcoffer2.api.Repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackcoffer2.api.Entitys.User;

public interface UserRepository  extends JpaRepository<User, Integer>{
	Optional<User> findByName(String name);

}
