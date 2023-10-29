package hr.fer.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class JwtAuthenticationResponse {
	
	private String accessToken;
	private Long id;
	private String name;
	private String email;
    
}
