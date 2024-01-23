package hr.fer.service;

import hr.fer.entity.*;
import hr.fer.repository.AnswerRepository;
import hr.fer.repository.QuestionRepository;
import hr.fer.repository.QuizCategoryRepository;
import hr.fer.repository.QuizRepository;
import hr.fer.requests_responses.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Autowired
    private QuizCategoryRepository quizCategoryRepository;

    public List<Quiz> getMasterQuizzes() {
        return quizRepository.findAllByMasterQuiz(true);
    }

    public boolean createQuiz(Quiz quiz) {
        quiz.setMasterQuiz(true);
        quiz.setFinished(false);
        
        int i = 0;
        for (Question q : quiz.getQuestionList()) {
        	q.setPosition(i++);
            q.setQuiz(quiz);
            int j = 0;
            for (Answer a : q.getAnswerList()) {
            	a.setPosition(j++);
                a.setQuestion(q);
            }
        }
        Quiz savedQuiz = quizRepository.save(quiz);
        return savedQuiz.getId() != null;
    }

    public Long createQuizCopy(Long masterQuizId, User takenBy) {
        Quiz masterQuizToCopy = quizRepository.getById(masterQuizId);
        Quiz newQuiz = new Quiz(masterQuizToCopy);
        newQuiz.setId(null);
        newQuiz.setMasterQuiz(false);
        newQuiz.setTakenBy(takenBy);
        List<Question> newQuestionList = new ArrayList<>();
        for (Question q : newQuiz.getQuestionList()) {
            Question newQ = new Question(q);
            newQ.setId(null);
            newQ.setQuiz(newQuiz);
            newQuestionList.add(newQ);
            List<Answer> newAnswerList = new ArrayList<>();
            for (Answer a : q.getAnswerList()) {
                Answer newA = new Answer(a);
                newA.setId(null);
                newA.setQuestion(newQ);
                newAnswerList.add(newA);
            }
            newQ.setAnswerList(newAnswerList);
        }
        newQuiz.setQuestionList(newQuestionList);
        newQuiz.setMasterQuizObject(masterQuizToCopy);
        return quizRepository.save(newQuiz).getId();
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
            existingQuiz.setFinished(quiz.isFinished());

            //Delete questions or answers that no longer belong
            List<Question> oldQuestions = questionRepository.findAllByQuiz(existingQuiz);
            for (Question question : oldQuestions) {
                boolean questionExistsInUpdated = quiz.getQuestionList().stream().anyMatch((q) -> Objects.equals(q.getId(), question.getId()));
                if (!questionExistsInUpdated) {
                    question.setQuiz(null);
                    continue;
                }

                Optional<Question> newQuestion = quiz.getQuestionList().stream().filter((q) -> Objects.equals(q.getId(), question.getId())).findFirst();
                //Problem if not found
                if (newQuestion.isEmpty()) return false;

                List<Answer> oldAnswers = answerRepository.findAllByQuestion(question);
                List<Answer> newAnswers = newQuestion.get().getAnswerList();
                for (Answer answer : oldAnswers) {
                    boolean answerExistsInUpdated = newAnswers.stream().anyMatch((a) -> Objects.equals(a.getId(), answer.getId()));
                    if (!answerExistsInUpdated) {
                        answer.setQuestion(null);
                    }
                }
            }

            int i = 0;
            for (Question q : quiz.getQuestionList()) {
            	q.setPosition(i++);
                q.setQuiz(existingQuiz);
                int j = 0;
                for (Answer a : q.getAnswerList()) {
                	a.setPosition(j++);
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
        if (quizOpt.isEmpty()) return null;

        Quiz quiz = quizOpt.get();
        SolvedQuizStats stats = new SolvedQuizStats();
        List<SolvedQuizQuestion> solvedQuestions = new ArrayList<>();

        stats.setQuizName(quiz.getQuizName());
        stats.setId(quiz.getId());

        //Calculate score and fill solvedQuestions list
        List<Question> questions = questionRepository.findAllByQuiz(quiz);
        double correctQuestions = 0;
        for (Question question : questions) {
            List<Answer> answers = answerRepository.findAllByQuestion(question);

            SolvedQuizQuestion solvedQuestion = new SolvedQuizQuestion();
            solvedQuestion.setQuestionString(question.getQuestionString());
            solvedQuestion.setCorrect(false);
            solvedQuestion.setCorrectAnswers(answers.stream().filter(Answer::isCorrect).map(Answer::getAnswerText).collect(Collectors.toList()));
            solvedQuestion.setSelectedAnswers(answers.stream().filter(Answer::isSelected).map(Answer::getAnswerText).collect(Collectors.toList()));

            //All answers that are correct must be selected for the question to be correct
            boolean correct = answers.stream().filter(Answer::isCorrect).allMatch(Answer::isSelected);
            if (correct) {
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

    public List<QuizCategory> getAllQuizCategories() {
        return quizCategoryRepository.findAll();
    }

    public boolean addQuizCategory(QuizCategory category) {
        quizCategoryRepository.save(category);
        return true;
    }

    public QuizInfo getQuizInfo(Long masterQuizId) {
        Optional<Quiz> optQuiz = quizRepository.findById(masterQuizId);
        if (optQuiz.isPresent()) {
            Quiz q = optQuiz.get();
            QuizInfo quizInfo = new QuizInfo();
            quizInfo.setQuizName(q.getQuizName());
            quizInfo.setQuizDescription(q.getDescription());
            quizInfo.setCreatedBy(q.getCreatedBy());
            quizInfo.setQuizCategory(q.getCategory());
            quizInfo.setNumOfAttempts(quizRepository.countQuizzesWithMasterCopy(masterQuizId));
            quizInfo.setNumOfFinished(quizRepository.countFinishedQuizzesWithMasterCopy(masterQuizId));
            return quizInfo;
        } else {
            return null;
        }
    }

    public List<Quiz> getMasterQuizzesBySearchParam(String searchParam) {
        return quizRepository.searchMasterQuizzesByNameOrDescription(searchParam);
    }
    
    public MyQuizInfo getMyQuizInfo(Long id) {
    	Optional<Quiz> quizOpt = quizRepository.findById(id);
        if (quizOpt.isEmpty()) return null;

        Quiz quiz = quizOpt.get();
        List<Question> masterQuestions = questionRepository.findAllByQuiz(quiz);
        MyQuizInfo quizInfo = new MyQuizInfo();
        quizInfo.setQuizName(quiz.getQuizName());
        quizInfo.setQuizDescription(quiz.getDescription());
        quizInfo.setNumOfAttempts(quizRepository.countQuizzesWithMasterCopy(id));
        quizInfo.setNumOfFinished(quizRepository.countFinishedQuizzesWithMasterCopy(id));
        
        long numberOfPeopleTaken = quizRepository.getQuizzesFromMaster(id).stream().map(q -> q.getTakenBy().getId()).distinct().count();
        quizInfo.setNumberOfPeopleAttempted(numberOfPeopleTaken);
        
        //Assumes every quiz solved has all the questions, and all questions are stored in the same order
        List<Integer> correctPerQuestion = new ArrayList<>();
        for(int i = 0; i < masterQuestions.size(); i++) {
        	correctPerQuestion.add(0);
        }
        List<Quiz> solvedQuizzes = quizRepository.getQuizzesFromMaster(id);
        
        for(Quiz q : solvedQuizzes) {
        	List<Question> questions = questionRepository.findAllByQuiz(q);
        	for(int i = 0; i < questions.size(); i++) {
        		List<Answer> answers = answerRepository.findAllByQuestion(questions.get(i));
        		//All answers that are correct must be selected for the question to be correct
                boolean correct = answers.stream().filter(Answer::isCorrect).allMatch(Answer::isSelected);
                if (correct) {
                	correctPerQuestion.set(i, correctPerQuestion.get(i) + 1);
                }
        	}
        }
        
        //Set scores for questions
        List<QuestionScore> scorePerQuestion = new ArrayList<>();
        for(int i = 0; i < masterQuestions.size(); i++) {
        	QuestionScore score = new QuestionScore();
        	score.setTotal(solvedQuizzes.size());
        	score.setCorrect(correctPerQuestion.get(i));
        	score.setIncorrect(score.getTotal() - score.getCorrect());
        	score.setScore(score.getCorrect() / (double) score.getTotal());
        	score.setMasterQuestionId(masterQuestions.get(i).getId());
        	score.setQuestionString(masterQuestions.get(i).getQuestionString());
        	scorePerQuestion.add(score);
        }
        
        Optional<QuestionScore> worstQuestion = scorePerQuestion.stream()
                .min(Comparator.comparingDouble(score -> score.getScore()));
        
        quizInfo.setScorePerQuestion(scorePerQuestion);
        quizInfo.setWorstQuestionAnswered(worstQuestion.isPresent() ? worstQuestion.get() : null);
        
        return quizInfo;
    }

    public boolean updateQuizAnswer(BasicIdObject idObject) {
        Optional<Answer> answerOptional = answerRepository.findById(idObject.getId());
        if(answerOptional.isPresent()){
            Answer a = answerOptional.get();
            a.setSelected(true);
            answerRepository.save(a);
            return true;
        }
        return false;
    }

    public Quiz getQuizById(Long quizId) {
        Optional<Quiz> q = quizRepository.findById(quizId);
        return q.orElse(null);
    }
}
