package com.jojoldu.book.springboot.service.posts;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(final PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity());
    }

    @Transactional
    public Long update(final Long id, final PostsUpdateRequestDto requestDto) {
        final Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("해당하는 게시글이 없습니다. id = %d", id)));

        return postsRepository.update(id, requestDto);
    }

    @Transactional
    public PostsResponseDto findById(final Long id) {
        final Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("해당하는 게시글이 없습니다. id = %d", id)));

        return new PostsResponseDto(entity);
    }


}
