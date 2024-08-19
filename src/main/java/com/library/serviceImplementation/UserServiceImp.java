package com.library.serviceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.dto.UserDto;
import com.library.entity.User;
import com.library.repository.UserRepository;
import com.library.service.UserService;

@Service
public class UserServiceImp implements UserService{
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User save(UserDto userDto) {
		User user=new User(userDto.getFullName(),userDto.getEmail(),passwordEncoder.encode(userDto.getPassword()),userDto.getRole());
		return repository.save(user);
	}
	
	@Override
	public boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }
	
	@Override
	public List<User> getAllUsers() {
        return repository.findAll();
    }


}
