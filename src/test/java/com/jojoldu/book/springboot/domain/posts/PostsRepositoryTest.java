package com.jojoldu.book.springboot.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PostsRepository postsRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE Posts IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE Posts\n" +
                "(\n" +
                "    id            BIGINT       NOT NULL AUTO_INCREMENT,\n" +
                "    title         VARCHAR(500) NOT NULL,\n" +
                "    content       TEXT         NOT NULL,\n" +
                "    author        VARCHAR(255),\n" +
                "    created_date  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "    modified_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "    PRIMARY KEY (id)\n" +
                ")");
        postsRepository = new PostsRepository(jdbcTemplate);
    }

    @AfterEach
    public void cleanUp() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("junpak.park@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAllDesc();

        //then
        final Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }
}
