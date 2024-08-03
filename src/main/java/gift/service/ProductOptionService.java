package gift.service;

import gift.DTO.ProductOptionDTO;
import gift.entity.ProductOptionEntity;
import gift.exception.ProductNotFoundException;
import gift.mapper.ProductOptionMapper;
import gift.repository.ProductOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductOptionService {

    @Autowired
    private ProductOptionMapper productOptionMapper;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductService productService;

    /**
     * 상품 옵션을 반환함
     *
     * @param id 옵션을 조회할 상품의 ID
     */
    @Transactional(readOnly = true)
    public List<ProductOptionDTO> getProductOptions(Long id) {
        var productEntity = productService.getProductEntity(id);
        return productEntity.getProductOptions().stream()
                .map(productOptionMapper::toProductOptionDTO).toList();
    }

    /**
     * 상품 옵션을 추가함
     *
     * @param productOptionDTO 추가할 상품 옵션 객체
     * @param productId        상품 옵션을 추가할 상품의 ID
     */
    @Transactional
    public ProductOptionDTO addProductOption(Long productId, ProductOptionDTO productOptionDTO) {
        var productEntity = productService.getProductEntity(productId);

        if (productEntity.getProductOptions().stream()
                .anyMatch(option -> option.getName().equals(productOptionDTO.name()))) {
            throw new ProductNotFoundException("이미 존재하는 옵션입니다.");
        }

        var productOptionEntity = productOptionMapper.toProductOptionEntity(productOptionDTO, false);
        productOptionRepository.save(productOptionEntity);

        return productOptionMapper.toProductOptionDTO(productOptionEntity);
    }

    /**
     * 상품 옵션을 삭제함
     *
     * @param id 삭제할 상품 옵션의 ID
     */
    @Transactional
    public void deleteProductOption(Long id) {
        if (!productService.isProdutExit(id)) {
            throw new ProductNotFoundException("옵션이 존재하지 않습니다");
        }
        productOptionRepository.deleteById(id);
    }

    /**
     * 상품 옵션을 갱신함
     *
     * @param id               갱신할 상품 옵션의 ID
     * @param productOptionDTO 갱신할 상품 옵션 객체
     */
    @Transactional
    public void updateProductOption(Long id, ProductOptionDTO productOptionDTO) {
        var productOptionEntity = productOptionRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("옵션이 존재하지 않습니다"));
        productOptionEntity.setName(productOptionDTO.name());
        productOptionEntity.setQuantity(productOptionDTO.quantity());
        productOptionRepository.save(productOptionEntity);
    }

    @Transactional(readOnly = true)
    public ProductOptionEntity getProductOptionEntity(Long id) {
        return productOptionRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("옵션이 존재하지 않습니다"));
    }

    @Transactional
    public void updateProductOptionQuantity(Long id, long l) {
        if (l < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        }
        var productOptionEntity = productOptionRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("옵션이 존재하지 않습니다"));
        productOptionEntity.setQuantity(l);
    }
}
