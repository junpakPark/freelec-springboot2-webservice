package com.jojoldu.book.springboot.domain.posts;

import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RowMapper<Posts> postsRowMapper = (resultSet, rowNum) -> {
        Posts posts = new Posts(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getString("content"),
                resultSet.getString("author"),
                resultSet.getTimestamp("modified_date").toLocalDateTime());
        return posts;
    };

    @Autowired
    public PostsRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

        return (long) keyHolder.getKeys().get("id");
    }

    public List<Posts> findAllDesc() {
        String sql = "SELECT * FROM Posts ORDER BY id DESC";

        return jdbcTemplate.query(sql, postsRowMapper);
    }

    public Long update(final Long id, final PostsUpdateRequestDto requestDto) {
        final String sql = "UPDATE posts SET title = ?, content = ? WHERE id = ?";
        return (long) jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getContent(), id);
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM Posts where id =?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAll() {
        final String sql = "DELETE FROM Posts";
        jdbcTemplate.update(sql);
    }

    public Optional<Posts> findById(final Long id) {
        final String sql = "SELECT * FROM posts where id = ?";
        try {
            final Posts posts = jdbcTemplate.queryForObject(sql, postsRowMapper, id);
            return Optional.ofNullable(posts);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
