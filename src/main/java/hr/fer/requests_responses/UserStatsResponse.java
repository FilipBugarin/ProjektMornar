package hr.fer.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatsResponse {
	private String username;
	private Integer quizzesCreated;
	private Integer quizzesTaken;
	private Integer quizzesCompleted;
	private Double averageScore;
	private Double highestScore;
}