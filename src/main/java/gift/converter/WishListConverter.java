package gift.converter;

import gift.dto.WishListDTO;
import gift.model.WishList;
public class WishListConverter {

    public static WishListDTO convertToDTO(WishList wishList) {
        return new WishListDTO(
            wishList.getId(),
            UserConverter.convertToDTO(wishList.getUser()),
            ProductConverter.convertToDTO(wishList.getProduct()),
            wishList.getCreatedDate()
        );
    }

    public static WishList convertToEntity(WishListDTO wishListDTO) {
        // createdDate는 엔티티 생성자에서 자동으로 설정됨
        return new WishList(
            wishListDTO.getId(),
            UserConverter.convertToEntity(wishListDTO.getUser()),
            ProductConverter.convertToEntity(wishListDTO.getProduct())
        );
    }
}