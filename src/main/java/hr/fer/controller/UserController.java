package hr.fer.controller;

import hr.fer.entity.User;
import hr.fer.security.CurrentUser;
import hr.fer.security.CustomUserDetailsService;
import hr.fer.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.requests_responses.UserStatsResponse;
import hr.fer.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@GetMapping("getMe")
	public ResponseEntity<User> getMe(@CurrentUser UserPrincipal user){
		return ResponseEntity.ok(customUserDetailsService.getUserById(user.getId()));
	}
	
	@GetMapping("stats/{id}")
    public ResponseEntity<UserStatsResponse> getAllQuizzes(@PathVariable("id") Long id){
		UserStatsResponse stats = userService.getUserStats(id);
		if(stats != null) {
			return ResponseEntity.ok(stats);
		} else {
			return ResponseEntity.internalServerError().build();
		}
    }

}
