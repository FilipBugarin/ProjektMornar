package hr.fer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.entity.Question;
import hr.fer.entity.Quiz;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findAllByQuiz(Quiz quiz);
}