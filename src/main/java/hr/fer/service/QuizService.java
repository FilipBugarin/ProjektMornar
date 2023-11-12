package hr.fer.service;

import hr.fer.entity.Answer;
import hr.fer.entity.Question;
import hr.fer.entity.Quiz;
import hr.fer.entity.User;
import hr.fer.repository.QuizRepository;
import hr.fer.repository.UserRepository;
import hr.fer.request_response.CreateQuizCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Quiz> getMasterQuizzes(){
        return quizRepository.findAllByMasterQuiz(true);
    }

    public List<Quiz> getCopies(){
        return quizRepository.findAllByMasterQuiz(false);
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

    public boolean createQuizCopy(CreateQuizCopy quiz) {
        Optional<Quiz> masterQuiz = quizRepository.findById(quiz.getId());
        if(masterQuiz.isEmpty()) return false;

        HashMap<Long, Long> questionAnswer = new HashMap<Long, Long>();
        for(CreateQuizCopy.Answer a: quiz.getAnswerList()){
            questionAnswer.put(a.getQuestionId(), a.getAnswerId());
        }

        Optional<User> user = userRepository.findById(quiz.getUserId());
        if(user.isEmpty()) return false;

        Quiz quizCopy = new Quiz(masterQuiz.get(), user.get());

        //set which answers the user selected
        for(Question q : quizCopy.getQuestionList()){
            Long chosenAnswerId = questionAnswer.get(q.getId());
            q.setId(null);

            for(Answer a: q.getAnswerList()){
                if(a.getId().equals(chosenAnswerId)){
                    a.setSelected(true);
                }
                a.setId(null);
            }
        }

        Quiz savedQuiz = quizRepository.save(quizCopy);
        return savedQuiz.getId() != null;
    }
}
