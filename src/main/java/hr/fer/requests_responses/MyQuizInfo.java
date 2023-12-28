package hr.fer.requests_responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyQuizInfo {
	private String quizName;
    private String quizDescription;
    private long numOfAttempts;
    private long numOfFinished;
	private QuestionScore worstQuestionAnswered;
	private List<QuestionScore> scorePerQuestion;
}
