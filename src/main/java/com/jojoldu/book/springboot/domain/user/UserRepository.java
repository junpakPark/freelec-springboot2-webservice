package com.jojoldu.book.springboot.domain.user;

import java.sql.PreparedStatement;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	private final JdbcTemplate jdbcTemplate;
	private RowMapper<User> userRowMapper = (rs, rowNum) -> {
		User user = new User(
			rs.getLong("id"),
			rs.getString("name"),
			rs.getString("email"),
			rs.getString("picture"),
			Role.fromKey(rs.getString("role"))
		);
		return user;
	};

	@Autowired
	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public User save(final User user) {
		final String sql = "INSERT INTO user (name, email, picture, role) VALUES (?, ?, ?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPicture());
			ps.setString(4, user.getRoleKey());
			return ps;
		}, keyHolder);

		user.setId((long)keyHolder.getKeys().get("id"));

		return user;
	}

	public User update(final User user, final String name, final String picture) {
		final String sql = "UPDATE user SET name = ?, picture = ? WHERE id = ?";
		jdbcTemplate.update(sql, name, picture, user.getId());
		user.setName(name);
		user.setPicture(picture);
		return user;
	}

	public void deleteById(final Long id) {
		final String sql = "DELETE FROM User where id =?";
		jdbcTemplate.update(sql, id);
	}

	public void deleteAll() {
		final String sql = "DELETE FROM User";
		jdbcTemplate.update(sql);
	}

	public Optional<User> findUserById(final Long id) {
		final String sql = "SELECT * FROM User WHERE id = ?";

		try {
			final User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
			return Optional.ofNullable(user);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<User> findUserByEmail(final String email) {
		final String sql = "SELECT * FROM User WHERE email = ?";

		try {
			final User user = jdbcTemplate.queryForObject(sql, userRowMapper, email);
			return Optional.ofNullable(user);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

}
