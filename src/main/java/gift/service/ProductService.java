package gift.service;

import gift.DTO.Product.ProductRequest;
import gift.DTO.Product.ProductResponse;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(
            ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }
    /*
     * 상품을 오름차순으로 정렬하는 로직
     */
    public Page<ProductResponse> readAllProductASC(int page, int size, String field){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc(field));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        Page<Product> products = productRepository.findAll(pageable);

        return products.map(ProductResponse::new);
    }
    /*
     * 상품을 내림차순으로 조회하는 로직
     */
    public Page<ProductResponse> readAllProductDESC(int page, int size, String field){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc(field));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        Page<Product> products = productRepository.findAll(pageable);

        return products.map(ProductResponse::new);
    }
    /*
     * id를 기준으로 한 상품을 조회
     */
    public ProductResponse readOneProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(NoSuchFieldError::new);
        return new ProductResponse(product);
    }
    /*
     * 상품을 생성하는 로직
     */
    @Transactional
    public ProductResponse save(ProductRequest productRequest){
        Category category = categoryRepository.findByName(productRequest.getCategoryName());
        Product productEntity = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                category
        );
        productRepository.save(productEntity);

        Option basicOption = new Option(productRequest.getBasicOption(), 1L);
        productEntity.addOption(basicOption);
        optionRepository.save(basicOption);

        return new ProductResponse(productEntity);
    }
    /*
     * 상품을 삭제하는 로직
     */
    @Transactional
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
    /*
     * 상품을 갱신하는 로직
     */
    @Transactional
    public void updateProduct(ProductRequest productRequest, Long id){
        Product savedProduct = productRepository.findById(id).orElseThrow(NoSuchFieldError::new);
        Category category = categoryRepository.findByName(productRequest.getCategoryName());

        savedProduct.updateEntity(
                productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl(), category
        );;
    }
}
