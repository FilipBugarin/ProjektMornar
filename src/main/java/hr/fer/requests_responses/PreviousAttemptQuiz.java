package hr.fer.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PreviousAttemptQuiz {

    private Long quizId;
    private boolean isFinished;
    private Timestamp finishedTime;
    private String percentageCorrect;
    private Integer numbOfCorrect;
    private Integer numbOfIncorrect;

}
