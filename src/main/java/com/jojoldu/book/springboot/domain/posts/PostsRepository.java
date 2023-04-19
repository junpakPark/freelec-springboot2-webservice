package com.jojoldu.book.springboot.domain.posts;

import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class PostsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostsRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Posts> postsRowMapper = (resultSet, rowNum) -> {
        Posts posts = new Posts(
                resultSet.getString("title"),
                resultSet.getString("content"),
                resultSet.getString("author"));
        return posts;
    };

    public long save(final Posts posts) {
        final String sql = "INSERT INTO posts (title, content, author) VALUES (?, ?, ?)";

        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, posts.getTitle());
            ps.setString(2, posts.getContent());
            ps.setString(3, posts.getAuthor());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Posts> findAll() {
        String sql = "SELECT * FROM Posts ORDER BY id DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final String title = rs.getString("title");
            final String content = rs.getString("content");
            final String author = rs.getString("author");

            return new Posts(title, content, author);
        });
    }

    public Long update(final Long id, final PostsUpdateRequestDto requestDto) {
        final String sql = "UPDATE posts SET title = ?, content = ? WHERE id = ?";
        return (long) jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getContent(), id);
    }

    public void deleteAll() {
        final String sql = "DELETE FROM Posts";
        jdbcTemplate.update(sql);
    }

    public Optional<Posts> findById(final Long id) {
        final String sql = "SELECT title, content, author FROM posts where id = ?";
        try {
            final Posts posts = jdbcTemplate.queryForObject(sql, postsRowMapper, id);
            return Optional.ofNullable(posts);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
