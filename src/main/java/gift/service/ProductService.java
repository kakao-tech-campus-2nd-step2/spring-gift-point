package gift.service;

import gift.dto.option.OptionRequestDTO;
import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.dto.product.ProductResponseWithDetailsDto;
import gift.entity.*;
import gift.exception.ResourceNotFoundException;
import gift.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductWishlistRepository productWishlistRepository;
    private final CategoryService categoryService;
    private final ProductOptionRepository productOptionRepository;
    private final OptionRepository optionRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final OptionService optionService;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductWishlistRepository productWishlistRepository, CategoryService categoryService, ProductOptionRepository productOptionRepository, OptionRepository optionRepository, UserService userService, UserRepository userRepository, CategoryRepository categoryRepository, OptionService optionService) {
        this.productRepository = productRepository;
        this.productWishlistRepository = productWishlistRepository;
        this.categoryService = categoryService;
        this.productOptionRepository = productOptionRepository;
        this.optionRepository = optionRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.optionService = optionService;
    }

    public ProductResponseWithDetailsDto findOne(Long id) {
        Product product = findById(id);
        Category category = categoryService.findById(product.getCategoryId());
        List<Option> options = productOptionRepository
                .findByProductId(id)
                .stream().map(op -> op.getOption())
                .collect(Collectors.toList());
        return new ProductResponseWithDetailsDto(product, category, options);
    }

    public List<ProductResponseDto> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).getContent()
                .stream()
                .map(product -> new ProductResponseDto(product))
                .collect(Collectors.toList());
    }

    // category
    public List<ProductResponseDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findAllByCategoryId(categoryId, pageable).getContent()
                .stream()
                .map(product -> new ProductResponseDto(product))
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto save(ProductRequestDto productRequestDto, String email) {
        User user = userService.findOne(email);

        Product product = productRepository.save(new Product(productRequestDto, user));
        Option defaultOption = optionRepository.findById(-1L)
                .orElseThrow(() -> new IllegalArgumentException("Option not found with id: -1L"));
        categoryRepository.findById(productRequestDto.getCategory_id())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + productRequestDto.getCategory_id()));

        productOptionRepository.save(new ProductOption(product, defaultOption, defaultOption.getName()));

        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto update(Long id, ProductRequestDto productRequestDto, String email) {
        Product product = findById(id);
        if (product.getCategoryId() != productRequestDto.getCategory_id()) {
            Category category = categoryService.findById(productRequestDto.getCategory_id());
            productRequestDto.setCategory_id(category.getId());
        }

        product.updateProduct(productRequestDto);

        Product updatedProduct = productRepository.save(product);
        return new ProductResponseDto(updatedProduct);
    }

    @Transactional
    public void delete(Long id, String email) {
        Product product = findById(id);
        product.setUser(null);

        productRepository.delete(product);
    }

    // option
    public List<Option> getOptions(Long id) {
        List<ProductOption> productOptions = productOptionRepository.findByProductId(id);
        return productOptions.stream()
                .map(productOption -> productOption.getOption())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Option> addProductOption(Long productId, List<OptionRequestDTO> optionRequestDTOS, String email) {
        Product product = findById(productId);
        List<Option> res = new ArrayList<>();

        for (OptionRequestDTO optionRequestDTO : optionRequestDTOS) {
            Option option = optionService.save(optionRequestDTO, email);
            productOptionRepository.save(new ProductOption(product, option, option.getName()));
            res.add(option);
        }

        return res;
    }

    @Transactional
    public void editProductOption(Long productId, Long optionId, OptionRequestDTO optionRequestDTO, String email) {
        Product product = findById(productId);
        optionService.update(optionId, optionRequestDTO, email);
    }

    @Transactional
    public void deleteProductOption(Long productId, Long optionId, String email) {
        // 옵션이 하나 이상인지 확인 로직
        if (productOptionRepository.findByProductId(productId).size() <= 1) {
            throw new IllegalArgumentException("Option should have at least one product option");
        }

        productOptionRepository.deleteByProductIdAndOptionId(productId, optionId);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
}
