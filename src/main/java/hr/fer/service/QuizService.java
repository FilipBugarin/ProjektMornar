package hr.fer.service;

import hr.fer.entity.Answer;
import hr.fer.entity.Question;
import hr.fer.entity.Quiz;
import hr.fer.entity.User;
import hr.fer.repository.AnswerRepository;
import hr.fer.repository.QuestionRepository;
import hr.fer.repository.QuizRepository;
import hr.fer.requests_responses.SolvedQuizQuestion;
import hr.fer.requests_responses.SolvedQuizStats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerRepository answerRepository;

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

    public Quiz createQuizCopy(Long masterQuizId, User takenBy) {
        Quiz masterQuizToCopy = quizRepository.getById(masterQuizId);
        Quiz newQuiz = new Quiz(masterQuizToCopy);
        newQuiz.setId(null);
        newQuiz.setMasterQuiz(false);
        newQuiz.setTakenBy(takenBy);
        List<Question> newQuestionList = new ArrayList<>();
        for(Question q:newQuiz.getQuestionList()){
            Question newQ = new Question(q);
            newQ.setId(null);
            newQ.setQuiz(newQuiz);
            newQuestionList.add(newQ);
            List<Answer> newAnswerList = new ArrayList<>();
            for(Answer a:q.getAnswerList()){
                Answer newA = new Answer(a);
                newA.setId(null);
                newA.setQuestion(newQ);
                newAnswerList.add(newA);
            }
            newQ.setAnswerList(newAnswerList);
        }
        newQuiz.setQuestionList(newQuestionList);
        return quizRepository.save(newQuiz);
    }

    public boolean updateQuiz(Quiz quiz) {
    	Optional<Quiz> optQuiz = quizRepository.findById(quiz.getId());
        if (optQuiz.isPresent()) {
            Quiz existingQuiz = optQuiz.get();
            existingQuiz.setCreatedBy(quiz.getCreatedBy());
            existingQuiz.setDescription(quiz.getDescription());
            existingQuiz.setMasterQuiz(quiz.isMasterQuiz());
            existingQuiz.setPrivateQuiz(quiz.isPrivateQuiz());
            existingQuiz.setQuestionList(quiz.getQuestionList());
            
            //Delete questions or answers that no longer belong
            List<Question> oldQuestions = questionRepository.findAllByQuiz(existingQuiz);
            for(Question question : oldQuestions) {
            	boolean questionExistsInUpdated = quiz.getQuestionList().stream().anyMatch((q) -> q.getId() == question.getId());
            	if(!questionExistsInUpdated) {
            		question.setQuiz(null);
            		continue;
            	}
            	
            	Optional<Question> newQuestion = quiz.getQuestionList().stream().filter((q) -> q.getId() == question.getId()).findFirst();
            	//Problem if not found
            	if(!newQuestion.isPresent()) return false;
            	
            	List<Answer> oldAnswers = answerRepository.findAllByQuestion(question);
            	List<Answer> newAnswers = newQuestion.get().getAnswerList();
            	for(Answer answer : oldAnswers) {
            		boolean answerExistsInUpdated = newAnswers.stream().anyMatch((a) -> a.getId() == answer.getId());
            		if(!answerExistsInUpdated) {
            			answer.setQuestion(null);
            		}
            	}
            }
            
            for (Question q : quiz.getQuestionList()) {
                q.setQuiz(existingQuiz);
                for (Answer a : q.getAnswerList()) {
                    a.setQuestion(q);
                }
            }
            existingQuiz.setQuizName(quiz.getQuizName());
            existingQuiz.setRandomOrder(quiz.isRandomOrder());
            existingQuiz.setTakenBy(quiz.getTakenBy());

            quizRepository.save(existingQuiz);

            return true;
        } else {
            return false;
        }
    }

    public List<Quiz> getQuizzesByuser(User user) {
        return quizRepository.findAllByTakenBy(user);
    }
    
    public SolvedQuizStats getSolvedQuizStats(Long id) {
    	
    	Optional<Quiz> quizOpt = quizRepository.findById(id);
    	if(!quizOpt.isPresent()) return null;
    	
    	Quiz quiz = quizOpt.get();
    	SolvedQuizStats stats = new SolvedQuizStats();
    	List<SolvedQuizQuestion> solvedQuestions = new ArrayList<>();
    	
    	stats.setQuizName(quiz.getQuizName());
    	
    	//Calculate score and fill solvedQuestions list
    	List<Question> questions = questionRepository.findAllByQuiz(quiz);
		double correctQuestions = 0;
		for(Question question : questions) {
			List<Answer> answers = answerRepository.findAllByQuestion(question);
			
			SolvedQuizQuestion solvedQuestion = new SolvedQuizQuestion();
			solvedQuestion.setQuestionString(question.getQuestionString());
			solvedQuestion.setCorrect(false);
			solvedQuestion.setCorrectAnswers(answers.stream().filter((a) -> a.isCorrect()).map((a) -> a.getAnswerText()).collect(Collectors.toList()));
			solvedQuestion.setSelectedAnswers(answers.stream().filter((a) -> a.isSelected()).map((a) -> a.getAnswerText()).collect(Collectors.toList()));
			
			//All answers that are correct must be selected for the question to be correct
			boolean correct = answers.stream().filter((a) -> a.isCorrect()).allMatch((a) -> a.isSelected());
			if(correct) {
				correctQuestions++;
				solvedQuestion.setCorrect(true);
			}
			
			solvedQuestions.add(solvedQuestion);
		}
		double score = correctQuestions / questions.size();
		stats.setQuestions(solvedQuestions);
		stats.setScore(score);
    	
    	return stats;
    }
}
