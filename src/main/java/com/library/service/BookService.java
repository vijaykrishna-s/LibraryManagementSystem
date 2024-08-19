package com.library.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.entity.Book;
import com.library.entity.Transaction;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.TransactionRepository;
import com.library.repository.UserRepository;

@Service
public class BookService {

	@Autowired
	public BookRepository repository;
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

	
	public void save(Book b) {
		b.setTotalCopies(b.getTotalCopies());
		repository.save(b);
	}
	
	public List<Book> getAllBooks(){
		return repository.findAll();
	}
	
	public void deleteById(int id) {
		repository.deleteById(id);
	}
	
	public Book getBookById(int id) {
		return repository.findById(id);
	}

	 public List<Book> findBooksByName(String name) {
		    return repository.findByName(name);
	 }
	 
	 public void takeBook(String email, String bname) {
	        User user = userRepository.findByEmail(email);
	        Book book = repository.findBookByName(bname);

	        if (book.getAvailableCopies() > 0) {
	            book.setAvailableCopies(book.getAvailableCopies() - 1);
	            repository.save(book);

	            Transaction transaction = new Transaction();
	            transaction.setUser(user);;
	            transaction.setBook(book);
	            transaction.setTransactionDate(LocalDateTime.now());
	            transaction.setReturned(false);
	            transactionRepository.save(transaction);
	        } else {
	            throw new RuntimeException("No available copies");
	        }
	    }
	 
	 public boolean isBookAlreadyReturned(String email, String bname) {
	        // Fetch user by email
	        User user = userRepository.findByEmail(email);
	        if (user == null) {
	            throw new RuntimeException("User not found");
	        }

	        // Fetch book by name
	        Book book = repository.findBookByName(bname);
	        if (book == null) {
	            throw new RuntimeException("Book not found");
	        }

	        // Check if the book is already returned by the user
	        Transaction transaction = transactionRepository.findByUserAndBook(user, book);
	        if (transaction == null || transaction.isReturned()) {
	            return true; // Book is already returned
	        }

	        return false; // Book is not yet returned
	    }

	    public void returnBook(String email, String bookName) {
	        // Fetch user by email
	        User user = userRepository.findByEmail(email);
	        if (user == null) {
	            throw new RuntimeException("User not found");
	        }

	        // Fetch book by name
	        Book book = repository.findBookByName(bookName);
	        if (book == null) {
	            throw new RuntimeException("Book not found");
	        }

	        // Fetch the borrowed book record
	        Transaction transaction = transactionRepository.findByUserAndBook(user, book);
	        if (transaction == null || transaction.isReturned()) {
	            throw new RuntimeException("Book is not borrowed or already returned");
	        }

	        // Mark the book as returned
	        transaction.setReturned(true);
	        transactionRepository.save(transaction);

	        // Update available copies of the book
	        book.setAvailableCopies(book.getAvailableCopies() + 1);
	        repository.save(book);
	    }
	
	    public List<Transaction> getAllTransaction(){
	    	return transactionRepository.findAll();
	    }
	 
	    public int getAvailableCopies(String bname) {
	        Book book = repository.findBookByName(bname);
	        return book.getAvailableCopies();
	    }

	    public List<Transaction> getCurrentTransactions(String bname) {
	        Book book = repository.findBookByName(bname);
	        return transactionRepository.findByBookAndIsReturnedFalse(book);
	    }
	    
	}
	 
	 

