package hr.fer.repository;

import hr.fer.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findAllByMasterQuiz(boolean isMasterQuiz);
}
