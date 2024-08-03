package gift.mapper;

import gift.DTO.MemberDTO;
import gift.DTO.WishListDTO;
import gift.entity.WishListEntity;
import gift.service.MemberService;
import gift.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WishListMapper 클래스는 WishListEntity와 WishListDTO 간의 변환을 담당합니다.
 */
@Component
public class WishListMapper {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    ProductService productService;

    @Autowired
    MemberService memberService;


    /**
     * WishListDTO를 WishListEntity로 변환하는 메서드
     *
     * @param wishListDTO 변환할 WishListDTO 객체
     * @return 변환된 WishListEntity 객체
     */
    public WishListEntity toWishListEntity(WishListDTO wishListDTO) {
        var wishListEntity = new WishListEntity();
        wishListEntity.setId(wishListDTO.id());
        wishListEntity.setProductEntity(productMapper.toProductEntity(wishListDTO.productDTO()));
        wishListEntity.setMemberEntity(memberMapper.toMemberEntity(wishListDTO.memberDTO()));
        return wishListEntity;
    }

    /**
     * WishListEntity를 WishListDTO로 변환하는 메서드
     *
     * @param wishListEntity 변환할 WishListEntity 객체
     * @return 변환된 WishListDTO 객체
     */
    public WishListDTO toWishListDTO(WishListEntity wishListEntity) {
        return new WishListDTO(
                wishListEntity.getId(),
                productMapper.toProductDTO(wishListEntity.getProductEntity()),
                memberMapper.toMemberDTO(wishListEntity.getMemberEntity())
        );
    }

    /**
     * ProductId와 MemberDTO를 이용하여 WishListEntity를 생성하는 메서드
     *
     * @param productId 상품 ID
     * @param memberDTO 사용자 정보
     * @return 생성된 WishListEntity 객체
     */
    public WishListEntity toWishListEntity(long productId, MemberDTO memberDTO) {
        var productDTO = productService.getProduct(productId);
        var wishListDTO = new WishListDTO(productDTO, memberDTO);
        return toWishListEntity(wishListDTO);
    }
}
