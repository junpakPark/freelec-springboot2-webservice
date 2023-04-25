package com.jojoldu.book.springboot.domain.user;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	GUEST("ROLE_GUEST", "손님"),
	USER("ROLE_USER", "일반 사용자");

	private final String key;
	private final String title;

	public static Role fromKey(String key) {
		return Arrays.stream(values())
			.filter(role -> role.key.equals(key))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("해당 등급은 없습니다. role = " + key));
	}

}
