package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductUpdateRequestDto;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.ProductException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionService optionService;
    private final CategoryRepository categoryRepository;


    public ProductService(ProductRepository productRepository, OptionService optionService,CategoryRepository categoryRepository) {
        this.productRepository=productRepository;
        this.optionService=optionService;
        this.categoryRepository=categoryRepository;

    }

    public List<ProductResponseDto> findAll() {
        return productRepository.findAll().stream()
            .map(this::productToDto)
            .collect(Collectors.toList());
    }

    public Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.findAllByCategoryId(categoryId, pageable);
    }

    public ProductResponseDto getProductResponseDtoById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductException("상품을 찾을 수 없습니다."));
        return product.toResponseDto();
    }


    private ProductResponseDto productToDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
    }

//    public List<Product> findAll() {
//        return productRepository.findAll();
//    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product addProduct(ProductRequestDto productRequestDto) {
        if (productRepository.existsByName(productRequestDto.getName())) {
            throw new ProductException("이미 존재하는 상품 이름입니다.");
        }

        Category category = categoryRepository.findById(productRequestDto.getCategoryId())
            .orElseThrow(() -> new ProductException("올바르지 않은 카테고리입니다."));

        Product product = productRequestDto.toEntity(category);
        List<Option> options = productRequestDto.getOptions().stream()
            .map(optionDto -> optionDto.toEntity(product))
            .collect(Collectors.toList());

        product.setOptions(options);

        for (Option option : options) {
            if (optionService.existsByNameAndProductId(option.getName(), product.getId())) {
                throw new ProductException("동일한 상품 내에 중복된 옵션이 있습니다.");
            }
        }

        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    @Transactional
    public Product updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto) {
        Product existProduct = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException("상품을 찾을 수 없습니다."));

        Category category = categoryRepository.findById(productUpdateRequestDto.getCategoryId())
            .orElseThrow(() -> new ProductException("올바르지 않은 카테고리입니다."));

        existProduct.setName(productUpdateRequestDto.getName());
        existProduct.setPrice(productUpdateRequestDto.getPrice());
        existProduct.setImageUrl(productUpdateRequestDto.getImageUrl());
        existProduct.setCategory(category);

        return productRepository.save(existProduct);
    }


    public Long deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            return -1L;
        }
        productRepository.deleteById(id);
        return id;
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

}
