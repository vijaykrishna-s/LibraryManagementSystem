package com.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.entity.WishList;
import com.library.repository.UserRepository;
import com.library.repository.WishListRepository;

@Service
public class WishListService {
	@Autowired
	private WishListRepository listRepository;
	 @Autowired
	    private UserRepository userRepository;
	 
	 
	
	 public void addToWishlist(String email, WishList wishlist) {
	        User user = userRepository.findByEmail(email);
	                
	        wishlist.setUser(user);
	        listRepository.save(wishlist);
	    }
	 
	 public List<WishList> getUserWishlist(String email) {
	        User user = userRepository.findByEmail(email);
	        return listRepository.findByUser(user);
	    }
	
	public List<WishList> getAllWishlists() {
        return listRepository.findAll();
    }
	
	public void removeBookFromList(int id) {
		listRepository.deleteById(id);
	}

}
