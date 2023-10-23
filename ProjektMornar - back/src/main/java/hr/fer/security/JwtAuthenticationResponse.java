package hr.fer.security;

public class JwtAuthenticationResponse {
	
	private String accessToken;
	private Long id;
	private String role;
	private String name;
	private String email;
	private String profileImageUrl;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

	public JwtAuthenticationResponse(String accessToken, Long id, String role, String name, String email, String profileImageUrl) {
		this.accessToken = accessToken;
		this.id = id;
		this.role = role;
		this.name = name;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
	}

	public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	
	
    
}
