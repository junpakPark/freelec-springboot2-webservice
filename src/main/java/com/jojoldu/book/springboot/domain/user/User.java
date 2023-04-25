package com.jojoldu.book.springboot.domain.user;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

	private Long id;
	private String name;
	private String email;
	private String picture;
	private Role role;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	@Builder
	public User(Long id, String name, String email, String picture, Role role) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.picture = picture;
		this.role = role;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}

	public User update(final String name, final String picture) {
		this.name = name;
		this.picture = picture;

		return this;
	}

}
