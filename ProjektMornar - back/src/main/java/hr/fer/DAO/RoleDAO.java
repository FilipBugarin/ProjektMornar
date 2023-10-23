package hr.fer.DAO;

import hr.fer.entity.auth.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RoleDAO {
	
	@Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
	
	public Role getRoleById(long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("id", id);
    	
    	return jdbcTemplate.queryForObject(
    			"select * from roles r where r.id = :id", parameters, new RoleRowMapper());
	}

	public class RoleRowMapper implements RowMapper<Role> {

		@Override
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			RoleSetExtractor extractor = new RoleSetExtractor();
			return extractor.extractData(rs);
		}

	}

	public class RoleSetExtractor implements ResultSetExtractor<Role> {

		@Override
		public Role extractData(ResultSet rs) throws SQLException, DataAccessException {

			Role role = new Role();

			role.setId(rs.getLong("id"));
			role.setName(rs.getString("name"));

			return role;
		}

	}
}
