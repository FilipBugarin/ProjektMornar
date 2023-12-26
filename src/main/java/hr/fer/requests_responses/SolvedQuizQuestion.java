package hr.fer.requests_responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolvedQuizQuestion {
	private String questionString;
	private boolean correct;
	private List<String> selectedAnswers;
	private List<String> correctAnswers;
}
