package com.smartgroup.smartcatalog.services;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartgroup.smartcatalog.dto.RoleDTO;
import com.smartgroup.smartcatalog.dto.UserDTO;
import com.smartgroup.smartcatalog.dto.UserInsertDTO;
import com.smartgroup.smartcatalog.dto.UserUpdateDTO;
import com.smartgroup.smartcatalog.entities.Role;
import com.smartgroup.smartcatalog.entities.User;
import com.smartgroup.smartcatalog.repositories.RoleRepository;
import com.smartgroup.smartcatalog.repositories.UserRepository;
import com.smartgroup.smartcatalog.services.exceptions.DatabaseException;
import com.smartgroup.smartcatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> usersPage = userRepository.findAll(pageable);
		return usersPage.map(user -> new UserDTO(user));
	}
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long userId) {
		java.util.Optional<User> object = userRepository.findById(userId);
		User user = object.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));
		
		return new UserDTO(user);
	}
	
	@Transactional
	public UserDTO insert(UserInsertDTO userInsertDTO) {
		User user = new User();
		
		copyUserDTOToUser(userInsertDTO, user);
		user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));
		
		user = userRepository.save(user);
		
		return new UserDTO(user);
	}
	
	@Transactional
	public UserDTO update(Long userId, UserUpdateDTO userUpdateDTO) {
		try {
			User user = userRepository.getOne(userId);
			
			copyUserDTOToUser(userUpdateDTO, user);
			
			user = userRepository.save(user);
			
			return new UserDTO(user);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + userId);
		}
	}
	
	public void delete(Long userId) {
		try {
			userRepository.deleteById(userId);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + userId);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyUserDTOToUser(UserDTO userDTO, User user) {
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		
		user.getRoles().clear();
		
		for(RoleDTO roleDTO : userDTO.getRoles()) {
			Role role = roleRepository.getOne(roleDTO.getId());
			user.getRoles().add(role);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		
		if(user == null) {
			logger.error("User not found: " + username);
			throw new UsernameNotFoundException("User not found: " + username);
		}
		logger.info("User found: " + username);
		return user;
	}
	
}
