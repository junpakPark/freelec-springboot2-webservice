package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

//    private JdbcTemplate jdbcTemplate; // SqlInitializationAutoConfiguration
//    @BeforeEach
//    void setUp() {
//        jdbcTemplate.execute("DROP TABLE Posts IF EXISTS");
//        jdbcTemplate.execute("CREATE TABLE Posts\n" +
//                "(\n" +
//                "    id            BIGINT       NOT NULL AUTO_INCREMENT,\n" +
//                "    title         VARCHAR(500) NOT NULL,\n" +
//                "    content       TEXT         NOT NULL,\n" +
//                "    author        VARCHAR(255),\n" +
//                "    created_date  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
//                "    modified_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
//                "    PRIMARY KEY (id)\n" +
//                ")");
//        postsRepository = new PostsRepository(jdbcTemplate);
//    }

    @AfterEach
    public void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    void savePosts() throws Exception {
        //given
        String title = "title";
        String content = "content";
        final PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        final String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        final ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        final List<Posts> all = postsRepository.findAll();

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    void updatePosts() throws Exception {
        //given
        final long updatedId = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        final String expectedTitle = "title2";
        final String expectedContent = "content2";

        final PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        final String url = "http://localhost:" + port + "/api/v1/posts/" + updatedId;
        final HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        final ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        final List<Posts> all = postsRepository.findAll();

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}
