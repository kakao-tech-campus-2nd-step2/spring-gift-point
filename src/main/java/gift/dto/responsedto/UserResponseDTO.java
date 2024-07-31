package gift.dto.responsedto;

import gift.domain.User;

public record UserResponseDTO (Long id, String email){
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getId(), user.getEmail());
    }
}
