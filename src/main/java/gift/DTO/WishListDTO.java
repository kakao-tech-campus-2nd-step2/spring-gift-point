package gift.DTO;

import jakarta.validation.constraints.NotNull;

public record WishListDTO(

        Long id,

        @NotNull
        Long productId,
        @NotNull
        Long userId,

        ProductDTO productDTO,

        MemberDTO memberDTO
) {

    public WishListDTO(ProductDTO productDTO, MemberDTO memberDTO) {
        this(null, productDTO, memberDTO);
    }

    public WishListDTO(Long id, ProductDTO productDTO, MemberDTO memberDTO) {
        this(id, productDTO.id(), memberDTO.getId(), productDTO, memberDTO);
    }
}