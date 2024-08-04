package gift.product.service;

import gift.category.domain.Category;
import gift.category.repository.CategoryRepository;
import gift.product.domain.ProductRequest;
import gift.product.domain.ProductResponse;
import gift.product.option.domain.Option;
import gift.product.option.domain.OptionDTO;
import gift.product.option.service.OptionService;
import gift.product.domain.Product;
import gift.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository,
        OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionService = optionService;
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(this::convertToResponse);
    }

    public Page<ProductResponse> getAllProductsByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.getProductsByCategoryId(categoryId, pageable)
            .map(this::convertToResponse);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductResponseById(Long id) {
        return productRepository.findById(id)
            .map(this::convertToResponse);
    }

    public Optional<ProductRequest> getProductRequestById(Long id) {
        return productRepository.findById(id)
            .map(this::convertToRequest);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public ProductResponse createProduct(@Valid ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("Category id " + productRequest.getCategoryId() + "가 없습니다."));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setCategory(category);
        if(productRequest.getOptionDTOList().size()>0){
            Option tempOption = new Option("tempOption", 1L);
            product.addOption(tempOption);
        }
        product = productRepository.save(product);

        product.getOptionList().clear();
        final Long productId = product.getId();
        productRequest.getOptionDTOList().forEach(optionDTO -> optionDTO.setProductId(productId));

        final Product finalProduct = product;
        productRequest.getOptionDTOList().forEach(optionDTO -> {
            Option option = optionService.convertToEntity(optionDTO);
            option.setProduct(finalProduct);
            finalProduct.addOption(option);
        });

        product = productRepository.save(finalProduct);

        return convertToResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, @Valid ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("Category id " + productRequest.getCategoryId() + "가 없습니다."));

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product id " + id + "가 없습니다."));

        productRequest.getOptionDTOList().forEach(optionDTO -> optionDTO.setProductId(id));

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setCategory(category);

        final Product finalProduct = product;
        List<Option> existingOptions = product.getOptionList();

        productRequest.getOptionDTOList().forEach(optionDTO -> {
            Option existingOption = existingOptions.stream()
                .filter(option -> option.getId().equals(optionDTO.getId()))
                .findFirst()
                .orElse(null);

            if (existingOption != null) {
                optionService.updateOption(id, existingOption.getId(), optionDTO);
            } else {
                Option newOption = optionService.convertToEntity(optionDTO);
                newOption.setProduct(finalProduct);
                finalProduct.addOption(newOption);
            }
        });

        product = productRepository.save(product);
        return convertToResponse(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductRequest convertToRequest(Product product){
        List<OptionDTO> optionDTOList = optionService.findAllByProductId(product.getId());
        return new ProductRequest(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            optionDTOList
        );
    }
    public ProductResponse convertToResponse(Product product){
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl());
    }

    private Product convertToEntity(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("Category id " + productRequest.getCategoryId() + "가 없습니다."));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setCategory(category);

        productRequest.getOptionDTOList().forEach(optionDTO -> {
            Option option = optionService.convertToEntity(optionDTO);
            product.addOption(option);
        });

        return product;
    }
}
