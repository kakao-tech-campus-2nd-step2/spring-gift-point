package gift.service;

import gift.entity.CategoryEntity;
import gift.entity.ProductEntity;
import gift.dto.ProductDTO;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> response = productRepository.findAll();
        return response.stream()
            .map(ProductEntity::toDTO)
            .collect(Collectors.toList());
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findAll(pageable);
        return productEntities.map(ProductEntity::toDTO);
    }

    // Read(단일 상품) - getProduct()
    public Optional<ProductDTO> getProduct(Long id) {
        return productRepository.findById(id)
            .map(ProductEntity::toDTO);
    }

    // Create(생성) - addProduct()
    @Transactional
    public void createProduct(ProductDTO productDTO) {
        CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다."));

        ProductEntity productEntity = new ProductEntity(
            productDTO.getName(),
            productDTO.getPrice(),
            productDTO.getImageUrl(),
            category
        );
        productRepository.save(productEntity);
    }

    // Update(수정) - updateProduct()
    @Transactional
    public void editProduct(Long id, ProductDTO productDTO) {
        ProductEntity existingProductEntity = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 상품을 찾을 수 없습니다"));

        CategoryEntity categoryEntity = categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 카테고리를 찾을 수 없습니다."));

        ProductEntity updatedProductEntity = new ProductEntity(
            existingProductEntity.getId(), // 기존 ID 유지
            productDTO.getName(),
            productDTO.getPrice(),
            productDTO.getImageUrl(),
            categoryEntity,
            existingProductEntity.getOptions() // 기존 옵션 유지
        );
        productRepository.save(updatedProductEntity);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("해당 ID의 상품을 찾을 수 없습니다");
        }

        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("상품 삭제 중 오류가 발생했습니다");
        }
    }
}