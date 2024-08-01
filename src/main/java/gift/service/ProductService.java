package gift.service;

import gift.dto.OptionSimpleRequestDTO;
import gift.dto.ProductCreateRequestDTO;
import gift.dto.ProductCreateResponseDTO;
import gift.dto.ProductRequestDTO;
import gift.dto.ProductResponseDTO;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OptionRepository optionRepository;

    // 모든 상품 조회
    public Page<ProductResponseDTO> getAllProducts(Long categoryId, Pageable pageable) { // 명세에 따른 수정: categoryId 추가
        Page<Product> products = productRepository.findByCategoryId(categoryId, pageable); // 명세에 따른 수정: findByCategoryId 추가
        return products.map(ProductResponseDTO::fromEntity);
    }

    // 특정 ID의 상품 조회
    public Optional<ProductResponseDTO> getProductById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        return productOpt.map(ProductResponseDTO::fromEntity);
    }

    // 상품 엔티티 조회
    public Product findProductEntityById(Long id) { // 명세에 따른 추가: findProductEntityById 메서드 추가
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품 정보를 찾을 수 없습니다."));
    }

    // 상품 생성
    public ProductCreateResponseDTO createProduct(ProductCreateRequestDTO productRequestDTO) {
        Category category = categoryService.findCategoryEntityByName(productRequestDTO.getCategory());
        Product product = new Product(productRequestDTO.getName(), productRequestDTO.getPrice(), productRequestDTO.getImageUrl(), category);
        product = productRepository.save(product);

        for (OptionSimpleRequestDTO optionDTO : productRequestDTO.getOptions()) {
            Option option = new Option(optionDTO.getName(), optionDTO.getQuantity(), product);
            optionRepository.save(option);
        }

        return new ProductCreateResponseDTO(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), category.getName());

    }

    // 상품 업데이트
    public Optional<ProductResponseDTO> updateProduct(Long id, ProductRequestDTO productRequest) {
        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            Category category = categoryService.findCategoryEntityById(productRequest.getCategoryId());
            existingProduct.setName(productRequest.getName());
            existingProduct.setImageUrl(productRequest.getImageUrl());
            existingProduct.setPrice(productRequest.getPrice());
            existingProduct.setCategory(category);
            productRepository.save(existingProduct);
            return Optional.of(ProductResponseDTO.fromEntity(existingProduct));
        }
        return Optional.empty();
    }

    // 상품 삭제
    public boolean deleteProduct(Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
