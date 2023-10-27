package hr.fer.security.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

	private String name;
    private String username;
    private String email;
    private String password;
	
}
