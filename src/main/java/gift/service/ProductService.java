package gift.service;

import gift.dto.OptionRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductWithOptionRequest;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.ServiceException;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Page<ProductResponseDto> findAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponseDto> productResponseDtoList = productPage.stream()
            .map(ProductResponseDto::new)
            .collect(Collectors.toList());

        return new PageImpl<>(productResponseDtoList, pageable, productPage.getTotalElements());
    }

    public Page<ProductResponseDto> findAllByCategoryId(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByCategoryId(categoryId, pageable);

        List<ProductResponseDto> productResponseDtoList = productPage.stream()
            .map(ProductResponseDto::new)
            .collect(Collectors.toList());

        return new PageImpl<>(productResponseDtoList, pageable, productPage.getTotalElements());
    }

    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ServiceException("존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND));
        return new ProductResponseDto(product);
    }

    public void save(ProductWithOptionRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ServiceException("카테고리 정보가 없습니다.", HttpStatus.NOT_FOUND));

        Product product = new Product(request.getName(), request.getPrice(), request.getImageUrl(),
            category);

        Product createdProduct = productRepository.save(product);

        List<OptionRequestDto> optionList = request.getOptions();
        optionList.forEach(option ->
            optionRepository.save(
                new Option(option.getName(), option.getQuantity(), createdProduct)));
    }

    public void update(Long id, ProductRequestDto request) {
        productRepository.findById(id)
            .orElseThrow(() -> new ServiceException("존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ServiceException("존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND));

        Product product = new Product(id, request.getName(), request.getPrice(),
            request.getImageUrl(),
            category);

        productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.findById(id)
            .orElseThrow(() -> new ServiceException("존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND));
        productRepository.deleteById(id);
    }
}