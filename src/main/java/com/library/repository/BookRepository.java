package com.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{
	
	List<Book> findByName(String name);
	Book findById(int id);
	Book findBookByName(String name); 
}
