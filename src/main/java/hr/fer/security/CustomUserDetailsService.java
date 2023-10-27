package hr.fer.security;

import hr.fer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fer.entity.auth.User;

import javax.swing.text.html.Option;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user;

        try {
            user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        } catch (EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail);
        }

        return UserPrincipal.create(user);
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {

        User user;

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent())
            user = optionalUser.get();
        else
            throw new UsernameNotFoundException("User not found with id : " + id);

        return UserPrincipal.create(user);
    }

    public User getUserById(Long id) {

        User user;

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent())
            user = optionalUser.get();
        else
            throw new UsernameNotFoundException("User not found with id : " + id);


        return user;

    }

    public boolean userExistsByUserName(String username) {

        User user;

        try {
            user = userRepository.findByUsername(username);
            if (user != null)
                return true;
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }

        return false;
    }

    public boolean existsByEmail(String email) {

        User user;

        try {
            user = userRepository.findByEmail(email);
            if (user != null)
                return true;
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }

        return false;
    }

    public User createUser(User u) {
        userRepository.save(u);
        return u;
    }

    public User editUser(User user) {
        return userRepository.save(user);
    }


}
