package gift.user.repository.dto;

import gift.user.domain.Role;
import gift.user.domain.User;

import java.util.Set;

public record UserDto(
	Long id,
	String email,
	String password,
	Set<Role> roles
) {

	// DTO를 엔티티로 변환하는 메서드
	public User toEntity() {
		return User.of(
			this.email,
			this.password,
			this.roles
		);
	}
}
