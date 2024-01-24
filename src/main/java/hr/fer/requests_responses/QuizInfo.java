package hr.fer.requests_responses;

import hr.fer.entity.QuizCategory;
import hr.fer.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizInfo {
    private String quizName;
    private String quizDescription;
    private QuizCategory quizCategory;
    private User createdBy;
    private long numOfAttempts;
    private long numOfFinished;
    private List<PreviousAttemptQuiz> previousAttempts;
    private QuizOwnerStatistics quizOwnerStatistics;
}
