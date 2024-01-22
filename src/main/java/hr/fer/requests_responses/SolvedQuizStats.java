package hr.fer.requests_responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolvedQuizStats {
	private long id;
	private String quizName;
	private double score;
	private List<SolvedQuizQuestion> questions;
}
