package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.*;
import gift.main.entity.Category;
import gift.main.entity.Product;
import gift.main.entity.User;
import gift.main.entity.WishProduct;
import gift.main.repository.CategoryRepository;
import gift.main.repository.ProductRepository;
import gift.main.repository.UserRepository;
import gift.main.repository.WishProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WishProductRepository wishProductRepository;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository,
                          UserRepository userRepository,
                          CategoryRepository categoryRepository,
                          WishProductRepository wishProductRepository,
                          OptionService optionService) {


        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.wishProductRepository = wishProductRepository;
        this.optionService = optionService;
    }

    public Page<ProductResponce> getProductPage(Pageable pageable) {
        Page<ProductResponce> productPage = productRepository.findAll(pageable)
                .map(ProductResponce::new);
        return productPage;
    }

    @Transactional
    public void registerProduct(ProductAllRequest productAllRequest, UserVo user) {
        ProductRequest productRequest = new ProductRequest(productAllRequest);
        User seller = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Category category = categoryRepository.findByUniNumber(productRequest.categoryUniNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

        Product product = new Product(productRequest, seller, category);
        Product saveProduct = productRepository.save(product);

        OptionListRequest optionListRequest = new OptionListRequest(productAllRequest.optionRequests());
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
        Category category = categoryRepository.findByUniNumber(productRequest.categoryUniNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
        product.updateValue(productRequest, category);
    }


    public ProductResponce getProduct(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        return new ProductResponce(product);
    }


}

