package com.library.service;

import java.util.List;

import com.library.dto.UserDto;
import com.library.entity.User;

public interface UserService {
	
	User save(UserDto userDto);
	boolean emailExists(String email);
	List<User> getAllUsers();
}