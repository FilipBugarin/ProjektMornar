package hr.fer.controller;

import hr.fer.entity.Quiz;
import hr.fer.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("list")
    public ResponseEntity<List<Quiz>> getAllQuizzes(){
        return ResponseEntity.ok(quizService.getMasterQuizzes());
    }

    @PostMapping("create")
    public ResponseEntity<Boolean> createQuiz(@RequestBody Quiz quiz){
        if(quizService.createQuiz(quiz)){
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }

    }
    
    @PutMapping("update")
    public ResponseEntity<Void> updateQuiz(@RequestBody Quiz quiz){
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Location", "/api/quiz/" + quiz.getId());
        if(quizService.updateQuiz(quiz)){
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } else {
        	return new ResponseEntity<>(headers, HttpStatus.OK);
        }

    }
}
