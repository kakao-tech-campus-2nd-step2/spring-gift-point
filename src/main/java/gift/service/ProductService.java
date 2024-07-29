package gift.service;

import gift.dto.OptionRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductWithOptionRequest;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.BusinessException;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<ProductResponseDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponseDto> productResponseDtoList = productPage.stream()
            .map(ProductResponseDto::new)
            .collect(Collectors.toList());

        return new PageImpl<>(productResponseDtoList, pageable, productPage.getTotalElements());
    }

    public ProductResponseDto findById(Long id) {
        Product product =  productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("해당 아이디에 대한 상품이 존재하지 않습니다."));
        return new ProductResponseDto(product);
    }

    public void save(ProductWithOptionRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new BusinessException("해당 id에 대한 카테고리 정보가 없습니다."));

        Optional<Product> existingProduct = productRepository.findByName(request.getName());
        if (existingProduct.isPresent()) {
            throw new BusinessException("해당 상품의 이름은 이미 등록되었습니다.");
        }
        Product product = new Product(request.getName(), request.getPrice(), request.getImageUrl(), category);

        Product createdProduct = productRepository.save(product);

        List<OptionRequestDto> optionList= request.getOptions();
        if (optionList == null || optionList.isEmpty()) {
            throw new BusinessException("옵션은 필수 입력사항입니다.");
        }
        optionList.forEach(option ->
            optionRepository.save(new Option(option.getName(), option.getQuantity(), createdProduct)));
    }

    public void update(Long id, ProductRequestDto request) {
        productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("해당 id에 대한 상품 정보가 없습니다."));

        Product product = new Product(id, request.getName(), request.getPrice(), request.getImageUrl(),
            request.getCategory());

        productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}