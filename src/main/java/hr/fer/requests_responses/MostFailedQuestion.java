package hr.fer.requests_responses;

import hr.fer.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MostFailedQuestion {
    private Question question;
    private Integer numberOfIncorrectAnswers;
}
