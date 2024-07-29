package gift.product;

import gift.category.Category;
import gift.category.CategoryRepository;
import gift.option.Option;
import gift.option.OptionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<ProductResponse> getProductPages(int pageNum, int size, String sortBy, String sortDirection) {
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Order.asc(sortBy)));
        if (Objects.equals(sortDirection, "desc")) {
            pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Order.desc(sortBy)));
        }

        return productRepository.findAll(pageable).map(ProductResponse::new);
    }

    public Product findByID(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Product insertNewProduct(ProductRequest newProduct, OptionRequest optionRequest) {
        Long categoryID = newProduct.getCategoryID();
        Category category = categoryRepository.findById(categoryID).orElseThrow();

        Product product = new Product(newProduct.getName(), newProduct.getPrice(), newProduct.getImageUrl(), category);
        category.addProduct(product);
        Option option = new Option(optionRequest, product);
        product.addOptions(option);

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest updateProduct) {
        Product product = findByID(id);
        product.setName(updateProduct.getName());
        product.setPrice(updateProduct.getPrice());
        product.setImageUrl(updateProduct.getImageUrl());

        product.getCategory().removeProduct(product);
        Category category = categoryRepository.findById(updateProduct.getCategoryID()).orElseThrow();
        product.setCategory(category);
        category.addProduct(product);

        return product;
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        product.getCategory().removeProduct(product);
        productRepository.deleteById(id);
    }

    public List<ProductResponse> findAllProducts() {
        return productRepository.findAll().stream().map(ProductResponse::new).toList();
    }
}
