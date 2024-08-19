package com.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.entity.User;
import com.library.entity.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer>{
	List<WishList> findAll();
	 List<WishList> findByUser(User user);
}
