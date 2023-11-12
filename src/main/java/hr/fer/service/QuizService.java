package hr.fer.service;

import hr.fer.entity.Answer;
import hr.fer.entity.Question;
import hr.fer.entity.Quiz;
import hr.fer.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public List<Quiz> getMasterQuizzes(){
        return quizRepository.findAllByMasterQuiz(true);
    }

    public boolean createQuiz(Quiz quiz) {
        quiz.setMasterQuiz(true);
        for(Question q:quiz.getQuestionList()){
            q.setQuiz(quiz);
            for(Answer a:q.getAnswerList()){
                a.setQuestion(q);
            }
        }
        Quiz savedQuiz = quizRepository.save(quiz);
        return savedQuiz.getId() != null;
    }
    
    /**
     * 
     * @param quiz
     * @return true if a new instance was created, false otherwise
     */
    public boolean updateQuiz(Quiz quiz) {
    	Optional<Quiz> optQuiz = quizRepository.findById(quiz.getId());
    	//Save if the quiz was not created before
    	if(!optQuiz.isPresent()) {
    		createQuiz(quiz);
    		return true;
    	}
    	
    	//Update values if quiz exists
    	Quiz existingQuiz = optQuiz.get();
    	existingQuiz.setCreatedBy(quiz.getCreatedBy());
    	existingQuiz.setDescription(quiz.getDescription());
    	existingQuiz.setMasterQuiz(quiz.isMasterQuiz());
    	existingQuiz.setPrivateQuiz(quiz.isPrivateQuiz());
    	existingQuiz.setQuestionList(quiz.getQuestionList());
    	for(Question q:quiz.getQuestionList()){
            q.setQuiz(quiz);
            for(Answer a:q.getAnswerList()){
                a.setQuestion(q);
            }
        }
    	existingQuiz.setQuizName(quiz.getQuizName());
    	existingQuiz.setRandomOrder(quiz.isRandomOrder());
    	existingQuiz.setTakenBy(quiz.getTakenBy());
    	
    	quizRepository.save(existingQuiz);
    	
        return false;
    }
}
