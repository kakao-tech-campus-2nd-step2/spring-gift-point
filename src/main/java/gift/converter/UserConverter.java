package gift.converter;

import gift.dto.UserDTO;
import gift.model.User;

public class UserConverter {

    public static UserDTO convertToDTO(User user) {
        return new UserDTO(user.getEmail(), user.getPassword());
    }

    public static User convertToEntity(UserDTO userDTO) {
        return new User(null, userDTO.getEmail(), userDTO.getPassword());
    }
}