package gift.dto.request;

import java.util.Set;

import gift.user.domain.Role;

public record UserDetails(
	Long userId,
	Set<Role> roles
) {
	public static UserDetails of(Long userId, Set<Role> roles) {
		return new UserDetails(userId, roles);
	}
}
