package hr.fer.requests_responses;

import hr.fer.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizInfo {
    private String quizName;
    private String quizDescription;
    private User createdBy;
    private long numOfAttempts;
    private long numOfFinished;
}
