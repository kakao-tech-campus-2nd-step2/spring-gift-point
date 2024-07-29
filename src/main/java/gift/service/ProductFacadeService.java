package gift.service;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
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

    public List<Option> getAllProductOption(Long id) {
        return optionService.getOptionByProductId(id);
    }

    public void updateProduct(Product product, Long id) {
        productService.updateProduct(product, id);
    }

    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }


    public Page<Product> getProductPage(int page) {
        return productService.getProductPage(page);
    }

    public Category findCategoryById(Long id) {
        return categoryService.findById(id);
    }

    public List<Category> findAllCategory() {
        return categoryService.findAll();
    }

}
