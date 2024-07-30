package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.OptionListRequest;
import gift.main.dto.ProductAllRequest;
import gift.main.dto.ProductRequest;
import gift.main.dto.ProductResponse;
import gift.main.entity.Category;
import gift.main.entity.Product;
import gift.main.entity.WishProduct;
import gift.main.repository.CategoryRepository;
import gift.main.repository.ProductRepository;
import gift.main.repository.WishProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final static int PAGE_SIZE = 20;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WishProductRepository wishProductRepository;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository,
                          UserRepository userRepository,
                          CategoryRepository categoryRepository,
                          WishProductRepository wishProductRepository,
                          OptionService optionService) {


        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.wishProductRepository = wishProductRepository;
        this.optionService = optionService;
    }

    /*
    페이지를 제공하는 부분도 어디까지가 역할일지
     */
    public Page<ProductResponse> getProductPage(int pageNum, int categoryId) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        return productRepository.findAllByCategoryId(pageable, (long) categoryId)
                .map(ProductResponse::new);
    }

    @Transactional
    public void registerProduct(ProductAllRequest productAllRequest) {
        ProductRequest productRequest = new ProductRequest(productAllRequest);
        Category category = categoryRepository.findById((long) productAllRequest.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

        Product product = new Product(productRequest, category);
        Product saveProduct = productRepository.save(product);

        OptionListRequest optionListRequest = new OptionListRequest(productAllRequest);
        optionService.registerOptionsFirstTime(saveProduct, optionListRequest);
    }

    @Transactional
    public void deleteProduct(long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        productRepository.deleteById(id);

        List<WishProduct> wishProducts = wishProductRepository.findAllByProductId(id);
        wishProductRepository.deleteAll(wishProducts);
    }

    @Transactional
    public void updateProduct(long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        Category category = categoryRepository.findById((long) productRequest.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
        product.updateValue(productRequest, category);
    }


    public ProductResponse getProduct(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        return new ProductResponse(product);
    }


}

