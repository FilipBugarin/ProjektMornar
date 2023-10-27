package hr.fer.security.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class editUserRequest {

	private String newName;
	private String newUsername;
	private String newEmail;
	private String newPassword;
	
}
