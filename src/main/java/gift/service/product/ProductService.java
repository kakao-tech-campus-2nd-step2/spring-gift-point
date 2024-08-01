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
import gift.web.dto.product.ProductPutRequestDto;
import gift.web.dto.product.ProductRequestDto;
import gift.web.dto.product.ProductResponseDto;
import gift.web.exception.notfound.CategoryNotFoundException;
import gift.web.exception.notfound.ProductNotFoundException;
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

    public Page<ProductResponseDto> getProducts(Long categoryId, Pageable pageable) {

        if (categoryId != null) {
            return productRepository.findAll(pageable).map(productMapper::toDto);
        }

        return productRepository.findAllByCategoryId(categoryId, pageable)
            .map(productMapper::toDto);
    }

    public ProductResponseDto getProductById(Long id) {
        return productRepository.findById(id)
            .map(productMapper::toDto)
            .orElseThrow(() -> new ProductNotFoundException());
    }

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Category category = categoryRepository.findById(productRequestDto.categoryId())
            .orElseThrow(() -> new CategoryNotFoundException());

        Product product = productMapper.toEntity(productRequestDto, category);

        product = productRepository.save(product);

        Option option = optionMappper.toEntity(productRequestDto.option(), product);
        optionRepository.save(option);
        product.addOption(option);

        return productMapper.toDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductPutRequestDto productPutRequestDto) {
        Category category = categoryRepository.findById(productPutRequestDto.categoryId())
            .orElseThrow(() -> new CategoryNotFoundException());

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException());

        product.updateProduct(
            productPutRequestDto.name(),
            productPutRequestDto.price(),
            productPutRequestDto.imageUrl(),
            category
        );

        return productMapper.toDto(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException());
        productRepository.delete(product);
    }
}
