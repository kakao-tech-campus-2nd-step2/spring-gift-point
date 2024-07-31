package gift.Service;

import gift.DTO.ProductDTO;
import gift.Model.Category;
import gift.Model.Option;
import gift.Model.Product;
import gift.Repository.CategoryRepository;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Page<Product> findAll(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long productId){
        return productRepository.findProductById(productId);
    }

    public Product addProduct(ProductDTO productDTO){
        Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), productDTO.getCategory(), productDTO.getOptions());
        Option defaultOption = new Option(null, product,product.getName(),1);
        productRepository.save(product);
        product.getOptions().add(optionRepository.save(defaultOption));
        return productRepository.findProductById(product.getId());
    }

    public Product updateProduct(ProductDTO productDTO){
        Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), productDTO.getCategory(), productDTO.getOptions());
        return productRepository.save(product);
    }

    public Product deleteProduct(Long productId){
        Product deleteProduct = productRepository.findProductById(productId);
        optionRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
        return deleteProduct;
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
}
