package hr.fer.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuizOwnerStatistics {

    private Integer numberOfQuizAttempts;
    private Integer numberOfPeopleAttemptingQuiz;
    private MostFailedQuestion questionThatpeopleFailedTheMost;
    private List<QuestionSolveStatistics> questionSolveStatistics;

}
