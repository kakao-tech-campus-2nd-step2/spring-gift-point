package gift.Mapper;

import gift.DTO.UserDTO;
import gift.DTO.WishDTO;
import gift.Entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserServiceMapper {

//    public UserDTO convertToDTO(UserEntity userEntity) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(userEntity.getId());
//        userDTO.setEmail(userEntity.getEmail());
//        userDTO.setPassword(userEntity.getPassword());
//        userDTO.setWishes(userEntity.getWishes() != null ? userEntity.getWishes().stream()
//                .map(wishEntity -> new WishDTO(
//                        wishEntity.getId(),
//                        wishEntity.getUser().getId(),
//                        wishEntity.getProduct().getId(),
//                        wishEntity.getProductName()))
//                .collect(Collectors.toList()) : null);
//        return userDTO;
//    }
//
//    public List<UserDTO> convertToUserDTOs(List<UserEntity> userEntities) {
//        return userEntities.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

//    public UserEntity convertToEntity(UserDTO userDTO) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(userDTO.getId());
//        userEntity.setEmail(userDTO.getEmail());
//        userEntity.setPassword(userDTO.getPassword());
//        return userEntity;
//    }

    public ResponseEntity<UserDTO> toResponseEntity(Optional<UserDTO> userDTOOptional) {
        return userDTOOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
