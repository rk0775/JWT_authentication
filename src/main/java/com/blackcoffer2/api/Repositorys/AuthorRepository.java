package com.blackcoffer2.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackcoffer2.api.Entitys.Authors;

public interface AuthorRepository extends JpaRepository<Authors, Integer> {
	Authors findByAuthorName(String name);
}
