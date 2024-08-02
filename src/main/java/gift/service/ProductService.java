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
    public Page<ProductResponse> readAllProduct(int page, int size, String field, String sort, Long categoryId){
        List<Sort.Order> sorts = new ArrayList<>();
        if(sort.equals("asc")) {
            sorts.add(Sort.Order.asc(field));
            Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
            Page<Product> products = productRepository.findAllByCategoryId(pageable, categoryId);
            return products.map(ProductResponse::new);
        }
        sorts.add(Sort.Order.desc(field));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        Page<Product> products = productRepository.findAllByCategoryId(pageable, categoryId);
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

        Option basicOption = new Option(productRequest.getBasicOption(), 1, productEntity);
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
