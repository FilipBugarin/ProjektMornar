package hr.fer.DAO;

import hr.fer.entity.auth.Role;
import hr.fer.entity.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class UserDAO {
	
	@Autowired
	RoleDAO roleDTO;
	
	@Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

	public User findByUsernameOrEmail(String usernameOrEmail) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("usernameOrEmail", usernameOrEmail);
		User user = jdbcTemplate.queryForObject(
				"select "
				+ "	  u.id,"
				+ "	  u.\"name\","
				+ "	  u.username,"
				+ "	  u.email,"
				+ "	  u.\"password\","
				+ "	  u.role_id,"
				+ "	  u.profileImage "
				+ "	  from users u  "
				+ "	  where u.username like :usernameOrEmail or u.email like :usernameOrEmail",parameters,new UserMapper());
		setRole(user);
		return user;
	}

	public User findById(Long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("id", id);
    	User user = jdbcTemplate.queryForObject(
				"select "
				+ "	  u.id,"
				+ "	  u.\"name\","
				+ "	  u.username,"
				+ "	  u.email,"
				+ "	  u.\"password\","
				+ "	  u.role_id, "
				+ "	  u.profileImage "
				+ "	  from users u join roles r on u.role_id = r.id "
				+ "	  where u.id = :id",parameters,new UserMapper());
    	setRole(user);
		return user;
	}

	public User findByUserName(String username) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("username", username);
		User user = jdbcTemplate.queryForObject(
				"select "
				+ "	  u.id,"
				+ "	  u.\"name\","
				+ "	  u.username,"
				+ "	  u.email,"
				+ "	  u.\"password\","
				+ "	  u.role_id, "
				+ "	  u.profileImage "
				+ "	  from users u join roles r on u.role_id = r.id "
				+ "	  where u.username = :username",parameters,new UserMapper());
		setRole(user);
		return user;
	}

	public User findByEmail(String email) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("email", email);
		User user = jdbcTemplate.queryForObject(
				"select "
				+ "	  u.id,"
				+ "	  u.\"name\","
				+ "	  u.username,"
				+ "	  u.email,"
				+ "	  u.\"password\","
				+ "	  u.role_id, "
				+ "   u.profileImage "
				+ "	  from users u join roles r on u.role_id = r.id "
				+ "	  where u.email = :email",parameters,new UserMapper());
		setRole(user);
		return user;
	}

	public void insertUser(User u) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("name", u.getName());
		parameters.addValue("username", u.getUsername());
		parameters.addValue("email", u.getEmail());
		parameters.addValue("password", u.getPassword());
		parameters.addValue("roleId", u.getRole().getId());
		
		jdbcTemplate.update("insert into users(name,username,email,\"password\",role_id) values(:name,:username,:email,:password,:roleId);", parameters);		
	}
	
	private void setRole(User u) {
		Role role = roleDTO.getRoleById(u.getRole().getId());
		u.setRole(role);
	}

	public boolean editUser(User u) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("userId", u.getId());
		parameters.addValue("name", u.getName());
		parameters.addValue("username", u.getUsername());
		parameters.addValue("email", u.getEmail());
		parameters.addValue("password", u.getPassword());
		parameters.addValue("profileImage", u.getProfileImageUrl());
		parameters.addValue("now", new Timestamp(System.currentTimeMillis()));
		
		return jdbcTemplate.update("update users set "
				+ "\"name\" = :name, "
				+ "username = :username, "
				+ "email = :email, "
				+ "profileimage = :profileImage, "
				+ "\"password\" = :password, "
				+ "updated_at = :now "
				+ "where id = :userId", parameters) >= 1;
	}

	public class UserMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserSetExtractor extractor = new UserSetExtractor();
			return extractor.extractData(rs);
		}

	}

	public class UserSetExtractor implements ResultSetExtractor<User> {

		@Override
		public User extractData(ResultSet rs) throws SQLException, DataAccessException {

			User user = new User();

			user.setId(rs.getLong("id"));
			user.setName(rs.getString("name"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));

			long roleId = rs.getLong("role_id");
			Role userRole = new Role();
			userRole.setId(roleId);

			user.setRole(userRole);
			user.setProfileImageUrl(rs.getString("profileImage"));

			return user;
		}

	}
}
