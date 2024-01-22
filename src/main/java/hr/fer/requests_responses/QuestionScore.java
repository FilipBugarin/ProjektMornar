package hr.fer.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionScore {
	private String questionString;
	private long masterQuestionId;
	private int correct;
	private int incorrect;
	private int total;
	private double score;
}
