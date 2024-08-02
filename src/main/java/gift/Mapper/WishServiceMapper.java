package gift.Mapper;

import gift.DTO.WishDTO;
import gift.Entity.WishEntity;
import gift.Entity.ProductEntity;
import gift.Entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WishServiceMapper {

    public WishDTO convertToDTO(WishEntity wishEntity) {
        return new WishDTO(
                wishEntity.getId(),
                wishEntity.getUser().getId(),
                wishEntity.getProduct().getId(),
                wishEntity.getProductName()
        );
    }

    public List<WishDTO> convertToWishDTOs(List<WishEntity> wishEntities) {
        return wishEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public WishEntity convertToEntity(WishDTO wishDTO) {
        WishEntity wishEntity = new WishEntity();
        wishEntity.setId(wishDTO.getId());
        wishEntity.setProduct(new ProductEntity(wishDTO.getProductId()));
        wishEntity.setUser(new UserEntity(wishDTO.getUserId()));
        // 다른 필요한 필드 설정
        return wishEntity;
    }

    public ProductEntity convertToProductEntity(Long productId) {
        return new ProductEntity(productId);
    }

    public UserEntity convertToUserEntity(Long userId) {
        return new UserEntity(userId);
    }

    public ResponseEntity<WishDTO> toResponseEntity(Optional<WishDTO> wishDTOOptional) {
        return wishDTOOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
