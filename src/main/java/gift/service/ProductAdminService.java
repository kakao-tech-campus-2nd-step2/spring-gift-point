package gift.service;

import gift.domain.AppUser;
import gift.domain.Category;
import gift.domain.Product;
import gift.dto.product.CreateProductAdminRequest;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductAdminService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final OptionService optionService;

    public ProductAdminService(ProductRepository productRepository, CategoryService categoryService,
                               UserService userService, ProductService productService, OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.optionService = optionService;
    }

    @Transactional
    public void addProduct(CreateProductAdminRequest createProductAdminRequest) {
        Category category = categoryService.getCategory(createProductAdminRequest.categoryId());

        AppUser seller = userService.findUser(createProductAdminRequest.sellerId());
        Product product = new Product(createProductAdminRequest.name(), createProductAdminRequest.price(),
                createProductAdminRequest.imageUrl(), seller, category);

        optionService.addOptionList(product, createProductAdminRequest.options());
        productRepository.save(product);
    }

    @Transactional
    public void updateCategory(Long productId, Long categoryId) {
        Product product = productService.findProduct(productId);
        Category newCategory = categoryService.getCategory(categoryId);
        product.updateCategory(newCategory);
        productRepository.save(product);
    }
}
