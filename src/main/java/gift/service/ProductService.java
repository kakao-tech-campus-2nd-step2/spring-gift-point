package gift.service;

import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.findByCategory_Id(categoryId, pageable);
    }

    @Transactional
    public Product saveProductWithOptions(Product product, List<Option> options) {
        Product savedProduct = productRepository.save(product);

        for(Option option : options) {
            Option newOption = new Option.Builder()
                    .name(option.getName())
                    .quantity(option.getQuantity())
                    .product(savedProduct)
                    .build();
            savedProduct.getOptions().add(newOption);
        }
        return productRepository.save(savedProduct);
    }

    @Transactional
    public Product updateProduct(Long productId, String name, Integer price, String imageUrl) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new CustomException.EntityNotFoundException("Product not found"));

        product.update(price, name, imageUrl, product.getCategory());

        return productRepository.save(product);
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

