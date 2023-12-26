package hr.fer.repository;

import hr.fer.entity.Quiz;
import hr.fer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
	List<Quiz> findAllByMasterQuiz(boolean isMasterQuiz);
    List<Quiz> findAllByCreatedBy(User user);
    List<Quiz> findAllByTakenBy(User user);
}
