package gift.service;

import gift.domain.Category;
import gift.domain.Option;
import gift.dto.OptionDto;
import gift.dto.ProductRegisterRequestDto;
import gift.domain.Product;
import gift.dto.ProductResponseDto;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Transactional(readOnly=true)
    public ProductResponseDto getProductById(long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 상품 없음: " + id));;
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    @Transactional
    public Long addProduct(ProductRegisterRequestDto productDto){
        Category category = categoryRepository.findById(productDto.getCategoryId())
            .orElseThrow(() -> new NoSuchElementException("해당 id의 카테고리 없음: " + productDto.getCategoryId()));
        Product newProduct = new Product(productDto.getName(),productDto.getPrice(),productDto.getImageUrl(), category);

//        // 상품에는 항상 하나 이상의 옵션이 있어야 한다.
//        Option defaultOption = new Option("default option", 1, newProduct);
//        newProduct.addOption(defaultOption);

        Product savedProduct = productRepository.save(newProduct);
        return savedProduct.getId();
    }

    @Transactional
    public Long updateProduct(long id, ProductRegisterRequestDto productDto){
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 상품 없음: " + id));
        Category category = categoryRepository.findById(productDto.getCategoryId())
            .orElseThrow(() -> new NoSuchElementException("해당 id의 카테고리 없음: " + productDto.getCategoryId()));

        existingProduct.setName(productDto.getName());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setImageUrl(productDto.getImageUrl());
        existingProduct.updateCategory(category);

        Product savedProduct = productRepository.save(existingProduct);
        return savedProduct.getId();
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly=true)
    public Page<ProductResponseDto> getPagedProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductResponseDto::convertToDto);
    }

    public Page<ProductResponseDto> getPagedProductsByCategory(Pageable pageable, Long categoryId) {
        Page<Product> productPage = productRepository.findByCategoryId(pageable, categoryId);
        return productPage.map(ProductResponseDto::convertToDto);
    }

    @Transactional(readOnly=true)
    public List<OptionDto> getOptionsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 상품 없음: " + productId));
        List<Option> options = product.getOptions();

        return options.stream()
            .map(OptionDto::convertToDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public OptionDto addOption(Long productId , OptionDto optionDto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 상품 없음: " + productId));
        Option newOption = new Option(optionDto.name(), optionDto.quantity(), product);
        product.addOption(newOption);
        Product addedOptionProduct = productRepository.save(product);

        // 새로 저장된 Option을 찾기
        Option addedOption = addedOptionProduct.getOptionByName(optionDto.name());

        return new OptionDto(addedOption.getId(), addedOption.getName(), addedOption.getQuantity(), addedOptionProduct.getId());
    }

    @Transactional
    public OptionDto updateOption(Long productId, Long optionId, OptionDto optionDto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 상품 없음: " + productId));
        Option updatedOption = new Option(optionId, optionDto.name(), optionDto.quantity(), product);
        product.updateOption(updatedOption);

        productRepository.save(product);

        return new OptionDto(updatedOption.getId(), updatedOption.getName(), updatedOption.getQuantity(), updatedOption.getId());
    }

    @Transactional
    public List<OptionDto> deleteOption(Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 상품 없음: " + productId));

        product.deleteOption(optionId);
        productRepository.save(product);

        return product.getOptions().stream()
            .map(OptionDto::convertToDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public void decreaseOptionQuantity(Long productId, Long optionId, int amount) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 상품 없음: " + productId));
        product.subtractOptionQuantity(optionId, amount);
        productRepository.save(product);
    }
}
