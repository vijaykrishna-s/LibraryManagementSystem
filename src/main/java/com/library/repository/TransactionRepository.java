package com.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.Book;
import com.library.entity.Transaction;
import com.library.entity.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBookAndIsReturnedFalse(Book book);
    Transaction findByUserAndBook(User user, Book book);
    
}
