package gift.dto.request;

import java.util.Set;

import gift.user.domain.Role;
import gift.user.domain.User;

public record SignupRequest(
	String email,
	String password
) {
	// toEntity() 메소드를 추가
	public User toEntity() {
		return User.of(email, password, Set.of(Role.USER));
	}
}
