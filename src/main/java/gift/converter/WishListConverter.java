package gift.converter;

import gift.dto.WishListDTO;
import gift.model.WishList;
public class WishListConverter {

    public static WishListDTO convertToDTO(WishList wishList) {
        return new WishListDTO(
            wishList.getId(),
            UserConverter.convertToDTO(wishList.getUser()),
            ProductConverter.convertToDTO(wishList.getProduct())
        );
    }

    public static WishList convertToEntity(WishListDTO wishListDTO) {
        return new WishList(
            wishListDTO.getId(),
            UserConverter.convertToEntity(wishListDTO.getUser()),
            ProductConverter.convertToEntity(wishListDTO.getProduct())
        );
    }
}