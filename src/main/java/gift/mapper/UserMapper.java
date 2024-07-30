package gift.mapper;

import gift.dto.UserResponseDto;
import gift.entity.User;

public class UserMapper {
    public static UserResponseDto toUserResponseDTO(User user) {
        return new UserResponseDto(user.getId(), user.getEmail());
    }
}
