package hr.fer.requests_responses;

import hr.fer.entity.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class QuizWithMasterQuizId {
    private Quiz quiz;
    private Long masterQuizId;
}
