package com.blackcoffer2.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackcoffer2.api.Entitys.Books;

public interface BooksRepository extends JpaRepository<Books, Integer>{

}
