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

    public static UserDTO convertToDTO1(User user) {
        return new UserDTO(user.getEmail(), user.getPassword(), user.getPoint());
    }

    public static User convertToEntity1(UserDTO userDTO) {
        return new User(null, userDTO.getEmail(), userDTO.getPassword(), userDTO.getPoint());
    }
}