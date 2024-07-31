package gift.service;

import gift.entity.Category;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findById(Long ProductId) {
        return productRepository.findById(ProductId).orElseThrow(()-> new CustomException.EntityNotFoundException("Product not found"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long ProductId) {
        if(!productRepository.existsById(ProductId)) {
            throw new CustomException.EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(ProductId);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException.EntityNotFoundException("Category not found"));
    }
}


