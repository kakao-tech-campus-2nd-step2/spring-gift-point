package gift.service;


import gift.dto.ProductDto;
import gift.exception.ValueAlreadyExistsException;
import gift.exception.ValueNotFoundException;
import gift.model.product.Category;
import gift.model.product.Product;
import gift.model.product.ProductName;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void addNewProduct(ProductDto productDto){
        if (productRepository.existsByName(new ProductName(productDto.name()))) {
            throw new ValueAlreadyExistsException("ProductName already exists in Database");
        }
        Category category = findElseSaveCategory(productDto.categoryName());
        Product product = new Product(category,new ProductName(productDto.name()),productDto.price(),productDto.imageUrl());

        productRepository.save(product);
    }

    public void updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ValueNotFoundException("Product not exists in Database"));
        Category category = findElseSaveCategory(productDto.categoryName());

        Product newProduct = new Product(category,new ProductName(productDto.name()),productDto.price(),productDto.imageUrl());
        product.updateProduct(newProduct);
        productRepository.save(product);
    }

    public Product selectProduct(Long id) {
        return productRepository.findById(id).get();
    }

    public Page<Product> getAllProducts(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public void DeleteProduct(Long id){
        productRepository.deleteById(id);
    }

    private Category findElseSaveCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> categoryRepository.save(new Category(categoryName)));
    }
}
