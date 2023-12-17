package hr.fer.service;

import hr.fer.entity.Answer;
import hr.fer.entity.Question;
import hr.fer.entity.Quiz;
import hr.fer.entity.User;
import hr.fer.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            for (Question q : quiz.getQuestionList()) {
                q.setQuiz(quiz);
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
}
