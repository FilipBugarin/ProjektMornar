package hr.fer.security.requests_responses;

public class editUserRequest {

	private String newName;
	private String newUsername;
	private String newProfileImage;

	private String newEmail;

	private String newPassword;
	
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public String getNewUsername() {
		return newUsername;
	}
	public void setNewUsername(String newUsername) {
		this.newUsername = newUsername;
	}
	public String getNewProfileImage() {
		return newProfileImage;
	}
	public void setNewProfileImage(String newProfileImage) {
		this.newProfileImage = newProfileImage;
	}
	public String getNewEmail() {
		return newEmail;
	}
	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
