package gift.service;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.exception.CategoryNotFoundException;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import gift.value.ProductName;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImgUrl(),
                        product.getCategory().getId(),
                        product.getOptions().stream()
                                .map(option -> new OptionDto(option.getId(), option.getName(), option.getQuantity()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(prod -> new ProductDto(
                prod.getId(),
                prod.getName(),
                prod.getPrice(),
                prod.getImgUrl(),
                prod.getCategory().getId(),
                prod.getOptions().stream()
                        .map(option -> new OptionDto(option.getId(), option.getName(), option.getQuantity()))
                        .collect(Collectors.toList())
        ));
    }

    public Long save(ProductDto productDto) {
        ProductName productName = new ProductName(productDto.getName()); // 값 객체를 사용하여 이름 검증
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("카테고리가 없습니다."));
        Product product = new Product(productName.getName(), productDto.getPrice(), productDto.getImgUrl(), category);
        product = productRepository.save(product);
        return product.getId();
    }

    public void update(Long id, ProductDto productDto) {
        ProductName productName = new ProductName(productDto.getName()); // 값 객체를 사용하여 이름 검증
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("카테고리가 없습니다."));
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("id로 상품을 찾을 수 없습니다." + id));
        product.update(productName.getName(), productDto.getPrice(), productDto.getImgUrl(), category);
        productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<OptionDto> getProductOptions(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 id로 상품이 존재하지 않습니다.: " + productId));

        return product.getOptions().stream()
                .map(option -> new OptionDto(option.getId(), option.getName(), option.getQuantity()))
                .collect(Collectors.toList());
    }

    public void addOptionToProduct(Long productId, OptionDto optionDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 id로 상품이 존재하지 않습니다.: " + productId));

        Option option = new Option(optionDto.getName(), product, optionDto.getQuantity()); // 순서 수정
        product.addOption(option);
        optionRepository.save(option);
    }

    public void subtractOptionQuantity(Long productId, Long optionId, int quantity) {
        Option option = optionRepository.findByIdAndProductId(optionId, productId)
                .orElseThrow(() -> new RuntimeException("해당 id로 옵션이 존재하지 않습니다.: " + optionId + " 상품 id : " + productId));
        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }
}