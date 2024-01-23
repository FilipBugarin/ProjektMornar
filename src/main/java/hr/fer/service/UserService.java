package hr.fer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.entity.Answer;
import hr.fer.entity.Question;
import hr.fer.entity.Quiz;
import hr.fer.entity.User;
import hr.fer.repository.AnswerRepository;
import hr.fer.repository.QuestionRepository;
import hr.fer.repository.QuizRepository;
import hr.fer.repository.UserRepository;
import hr.fer.requests_responses.UserStatsResponse;

@Service
public class UserService {

	@Autowired
    private QuizRepository quizRepository;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private QuestionRepository questionRepository;
	
	@Autowired
    private AnswerRepository answerRepository;
	
	
	
	public UserStatsResponse getUserStats(Long id) {
		
		Optional<User> optUser = userRepository.findById(id);
		
		if(!optUser.isPresent()) {
			return null;
		}
		
		User user = optUser.get();
		
		List<Quiz> createdQuizzes = quizRepository.findAllByCreatedBy(user);
		List<Quiz> takenQuizzes = quizRepository.findAllByTakenBy(user);
		List<Quiz> completedQuizzes = getCompletedQuizzes(takenQuizzes);
		List<Double> quizScores = getQuizScores(completedQuizzes);
		double averageScore = getAverageQuizScore(quizScores);
		double highestScore = getHighestQuizScore(quizScores);
		
		UserStatsResponse stats = new UserStatsResponse();
		
		stats.setUsername(user.getUsername());
		stats.setQuizzesCreated(createdQuizzes.size());
		stats.setQuizzesTaken(takenQuizzes.size());
		stats.setQuizzesCompleted(completedQuizzes.size());
		stats.setAverageScore(averageScore);
		stats.setHighestScore(highestScore);
		
		return stats;
	}
	
	private List<Quiz> getCompletedQuizzes(List<Quiz> quizzes) {
		
		return quizzes.stream().filter(Quiz::isFinished).collect(Collectors.toList());
	}
	
	private List<Double> getQuizScores(List<Quiz> completedQuizzes) {
		
		List<Double> scores = new ArrayList<>();
		
		for(Quiz quiz : completedQuizzes) {
			List<Question> questions = questionRepository.findAllByQuiz(quiz);
			double correctQuestions = 0;
			for(Question question : questions) {
				List<Answer> answers = answerRepository.findAllByQuestion(question);
				//All answers that are correct must be selected for the question to be correct
				boolean correct = answers.stream().filter((a) -> a.isCorrect()).allMatch((a) -> a.isSelected());
				if(correct) correctQuestions++;
			}
			double score = correctQuestions / questions.size();
			scores.add(score);
		}
		return scores;
	}
	
	private double getAverageQuizScore(List<Double> scores) {
		
		double average = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
		
		return average;
	}
	
	private double getHighestQuizScore(List<Double> scores) {
		
		double max = scores.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
		
		return max;
	}

}