package com.jojoldu.book.springboot.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PostsRepository postsRepository;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@BeforeEach
	public void setUp() {
		mvc = MockMvcBuilders
			.webAppContextSetup(context)
			.apply(springSecurity())
			.build();
	}

	@AfterEach
	public void tearDown() {
		postsRepository.deleteAll();
	}

	@Test
	@WithMockUser(roles = "USER")
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
		mvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(status().isOk());
		final List<Posts> all = postsRepository.findAllDesc();

		//then
		assertThat(all.get(0).getTitle()).isEqualTo(title);
		assertThat(all.get(0).getContent()).isEqualTo(content);

		// final ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
		// assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		// assertThat(responseEntity.getBody()).isGreaterThan(0L);
	}

	@Test
	@WithMockUser(roles = "USER")
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
		mvc.perform(put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(status().isOk());

		final List<Posts> all = postsRepository.findAllDesc();

		//then
		assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
		assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

		// final ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
		// 	Long.class);
		// assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		// assertThat(responseEntity.getBody()).isGreaterThan(0L);
	}
}
