package gift.service;

import gift.entity.Product;
import gift.domain.ProductDTO;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Product> getAllProduct(int page, int size) {
        Pageable pageRequest = createPageRequestUsing(page, size);

        int start = (int) pageRequest.getOffset();
        int end = start + pageRequest.getPageSize();
        if (page > 0) { start += 1; }

        List<Product> pageContent = productRepository.findByProductIdBetween(start, end);
        return new PageImpl<>(pageContent, pageRequest, pageContent.size());
    }

    private Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }

    public Optional<Product> getProductById(int id) {
        var product = productRepository.findById(id);
        if (product == null) {
            return Optional.empty();
        }
        return Optional.of(product);
    }

    public Product addProduct(ProductDTO productDTO) {
        var category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(() -> new NoSuchElementException("Category not found"));
        var product = new Product(category, productDTO.price(), productDTO.name(), productDTO.imageUrl());
        return productRepository.save(product);
    }

    public Product updateProduct(int id, ProductDTO productDTO) {
        var category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(() -> new NoSuchElementException("Category not found"));
            Product product = new Product(id, category, productDTO.price(), productDTO.name(), productDTO.imageUrl());
            return productRepository.save(product);
    }

    public void deleteProduct(int id) {
        try {
            productRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new NoSuchElementException();
        }
    }
}
