package com.jojoldu.book.springboot.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

//    @Test
//    void loadMainPage() {
//        // when
//        final String body = this.restTemplate.getForObject("/", String.class);
//
//        // then
//        Assertions.assertThat(body).contains("스프링부트로 시작하는 웹 서비스");
//    }
}
