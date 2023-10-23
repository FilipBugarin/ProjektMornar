package hr.fer.security;

import hr.fer.DAO.RoleDAO;
import hr.fer.DAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fer.entity.auth.Role;
import hr.fer.entity.auth.User;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	UserDAO userDTO;
	
	@Autowired
	RoleDAO roleDTO;
	
	@Override
	@Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        
		User user;
		
		try {
			 user = userDTO.findByUsernameOrEmail(usernameOrEmail);
		}catch(EmptyResultDataAccessException ex) {
			throw new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail);
		}

        return UserPrincipal.create(user);
    }
	
	// This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
       
    	User user;
		
		try {
			user = userDTO.findById(id);
		}catch(EmptyResultDataAccessException ex) {
			throw new UsernameNotFoundException("User not found with id : " + id);
		}
		
		return UserPrincipal.create(user);
    }
    
    public User getUserById(Long id) {
		
		User user;
		
		try {
			user = userDTO.findById(id);
		}catch(EmptyResultDataAccessException ex) {
			throw new UsernameNotFoundException("User not found with id : " + id);
		}
		
		return user;
		
	}
    
    public boolean userExistsByUserName(String username) {
    	
    	User user;
		
		try {
			user = userDTO.findByUserName(username);
			if(user!=null)
				return true;
		}catch(EmptyResultDataAccessException ex) {
			return false;
		}
    	
    	return false;
    }

	public boolean existsByEmail(String email) {
		
		User user;
		
		try {
			user = userDTO.findByEmail(email);
			if(user!=null)
				return true;
		}catch(EmptyResultDataAccessException ex) {
			return false;
		}
    	
    	return false;
	}
	
	public User createUser(User u) {
		userDTO.insertUser(u);
		return u;
	}

	public Role getRoleById(Long roleId) {		
		return roleDTO.getRoleById(roleId);
	}

	public boolean editUser(User user) {
		return userDTO.editUser(user);
	}

	

	

}
