package hr.fer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.entity.Answer;
import hr.fer.entity.Question;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
	List<Answer> findAllByQuestion(Question question);
}