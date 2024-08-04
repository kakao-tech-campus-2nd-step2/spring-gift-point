package gift.service;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductFacadeService {

    private final ProductService productService;
    private final OptionService optionService;
    private final CategoryService categoryService;

    public ProductFacadeService(ProductService productService, OptionService optionService,
        CategoryService categoryService) {
        this.productService = productService;
        this.optionService = optionService;
        this.categoryService = categoryService;
    }

    public void addProduct(Product product, List<Option> options) {
        if (options.isEmpty()) {
            throw new CustomException(ErrorCode.PRODUCT_HAVE_NO_OPTION);
        }
        productService.saveProduct(product);
        for (Option option : options) {
            optionService.addOption(option);
        }
    }

    public void saveProduct(Product product) {
        productService.saveProduct(product);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public Product getProductById(Long id) {
        return productService.getProductById(id);
    }


    public void updateProduct(Product product, Long id) {
        productService.updateProduct(product, id);
    }

    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }


    public Page<Product> getProductPage(Pageable pageable, Long categoryId) {
        return productService.getProductPage(pageable, categoryId);
    }

    public Category findCategoryById(Long id) {
        return categoryService.findById(id);
    }

    public List<Category> findAllCategory() {
        return categoryService.findAll();
    }

}
