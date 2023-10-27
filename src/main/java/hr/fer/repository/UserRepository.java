package hr.fer.repository;

import hr.fer.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameOrEmail(String username, String email);

    User findByUsername(String username);

    User findByEmail(String email);
}
