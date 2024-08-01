package gift.service.product;

import gift.domain.category.Category;
import gift.domain.category.CategoryRepository;
import gift.domain.option.Option;
import gift.domain.option.OptionRepository;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import gift.mapper.OptionMappper;
import gift.mapper.ProductMapper;
import gift.web.dto.OptionDto;
import gift.web.dto.ProductDto;
import gift.web.exception.CategoryNotFoundException;
import gift.web.exception.ProductNotFoundException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Service단에서는 DTO를 Entity로 변환해서 Repository로 넘겨주고, Entity를 DTO로 변환해서 Controller에서 넘겨주면 되나?
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;
    private final OptionMappper optionMappper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper,
        CategoryRepository categoryRepository, OptionRepository optionRepository,
        OptionMappper optionMappper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
        this.optionMappper = optionMappper;
    }

    public Page<ProductDto> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(productMapper::toDto);
    }

    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
            .map(productMapper::toDto)
            .orElseThrow(() -> new ProductNotFoundException("제품이 없슴다."));
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.categoryId())
            .orElseThrow(() -> new CategoryNotFoundException("카테고리가 없슴다"));

        Product product = productMapper.toEntity(productDto, category);

        product = productRepository.save(product);

        for (OptionDto optionDto : productDto.optionDtoList()) {
            Option option = optionMappper.toEntity(optionDto, product);
            optionRepository.save(option);
            product.addOption(option);
        }

        return productMapper.toDto(product);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.categoryId())
            .orElseThrow(() -> new CategoryNotFoundException("카테코리가 없슴다"));

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("제품이 없슴다."));

        product.updateProduct(
            productDto.name(),
            productDto.price(),
            productDto.imageUrl(),
            category
        );

        return productMapper.toDto(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("제품이 없슴다."));
        productRepository.delete(product);
    }
}
