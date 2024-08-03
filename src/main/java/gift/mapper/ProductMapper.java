package gift.mapper;

import gift.DTO.ProductDTO;
import gift.entity.ProductEntity;
import gift.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ProductMapper 클래스는 ProductEntity와 ProductDTO 간의 변환을 담당합니다.
 */
@Component
public class ProductMapper {

    @Autowired
    private CategoryService categoryService;

    /**
     * ProductEntity를 ProductDTO로 변환하는 메서드
     *
     * @param productEntity 변환할 ProductEntity 객체
     * @return 변환된 ProductDTO 객체
     */
    public ProductDTO toProductDTO(ProductEntity productEntity) {
        return new ProductDTO(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productEntity.getCategoryEntity().getId()
                );
    }

    /**
     * ProductDTO를 ProductEntity로 변환하는 메서드
     *
     * @param productDTO 변환할 ProductDTO 객체
     * @param idRequired ID 필요 여부
     * @return 변환된 ProductEntity 객체
     */
    public ProductEntity toProductEntity(ProductDTO productDTO, boolean idRequired) {
        var productEntity = new ProductEntity();
        if (idRequired) {
            productEntity.setId(productDTO.id());
        }
        productEntity.setName(productDTO.name());
        productEntity.setPrice(productDTO.price());
        categoryService.getCategoryById(productDTO.categoryId());
        productEntity.setImageUrl(productDTO.imageUrl());
        return productEntity;
    }

    /**
     * ProductDTO를 ProductEntity로 변환하는 메서드
     *
     * @param productDTO 변환할 ProductDTO 객체
     * @return 변환된 ProductEntity 객체
     */
    public ProductEntity toProductEntity(ProductDTO productDTO) {
        return toProductEntity(productDTO, true);
    }
}
