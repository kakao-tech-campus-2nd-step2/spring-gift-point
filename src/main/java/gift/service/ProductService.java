package gift.service;

import gift.converter.NameConverter;
import gift.converter.OptionConverter;
import gift.converter.ProductConverter;
import gift.dto.PageRequestDTO;
import gift.dto.ProductDTO;
import gift.model.Option;
import gift.model.OptionName;
import gift.model.OptionQuantity;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Page<ProductDTO> findAllProducts(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.toPageRequest();
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductConverter::convertToDTO);
    }

    public Long addProduct(ProductDTO productDTO) {
        categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        Product product = ProductConverter.convertToEntity(productDTO);

        // 기본 옵션 추가
        Option defaultOption = new Option(null, "기본 옵션", 1, product);
        product.addOption(defaultOption);

        productRepository.save(product);
        return product.getId();
    }

    public Optional<ProductDTO> findProductById(Long id) {
        return productRepository.findById(id)
            .map(ProductConverter::convertToDTO);
    }

    @Transactional
    public void updateProductOptions(Long productId, List<Long> optionIds) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        List<Option> options = optionRepository.findAllById(optionIds);

        // 기존 옵션 제거
        product.clearOptions();

        // 새로운 옵션 리스트 추가
        for (Option option : options) {
            option.assignProduct(product); // Option 객체에 Product 설정
            product.addOption(option);
        }

        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        // 기존 옵션 제거
        existingProduct.clearOptions();

        // 새로운 옵션 리스트 생성 및 할당
        List<Option> options = productDTO.getOptions().stream()
            .map(optionDTO -> {
                Option option = OptionConverter.convertToEntity(optionDTO);
                option.assignProduct(existingProduct); // 기존 Product 객체에 옵션 할당
                return option;
            }).collect(Collectors.toList());

        // 기존 Product 객체 업데이트
        existingProduct.update(
            productDTO.getName(),
            productDTO.getPrice(),
            productDTO.getImageUrl(),
            productDTO.getCategoryId()
        );

        // 옵션 추가
        options.forEach(existingProduct::addOption);

        productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public String getProductNameById(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        return product.getName();
    }
}