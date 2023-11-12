package hr.fer.controller;

import hr.fer.entity.Quiz;
import hr.fer.request_response.CreateQuizCopy;
import hr.fer.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("create_copy")
    public ResponseEntity<Boolean> createQuizCopy(@RequestBody CreateQuizCopy quizCopy){
        if(quizService.createQuizCopy(quizCopy)){
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }

    }

    @GetMapping("copies")
    public ResponseEntity<List<Quiz>> getCopies(){
        return ResponseEntity.ok(quizService.getCopies());
    }
}
