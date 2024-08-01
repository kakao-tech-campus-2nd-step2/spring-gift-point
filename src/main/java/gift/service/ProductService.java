package gift.service;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.dto.ProductDTO;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final OptionRepository optionRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository,
        WishlistRepository wishlistRepository, OptionRepository optionRepository,
        CategoryService categoryService) {
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
        this.optionRepository = optionRepository;
        this.categoryService = categoryService;
    }

    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findProductsById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveProduct(ProductDTO productDTO) {
        Category category = categoryService.findCategoryById(productDTO.categoryId());
        Product product = toEntity(productDTO, null, category);
        productRepository.save(product);
        Option temporaryOption = new Option(null, "임시옵션", 1L, product);
        optionRepository.save(temporaryOption);
    }

    @Transactional
    public void updateProduct(ProductDTO productDTO, Long id) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            Category category = categoryService.findCategoryById(productDTO.categoryId());
            existingProduct.updateProduct(productDTO.name(), productDTO.price(), category,
                productDTO.imageUrl());
            productRepository.save(existingProduct);
        }
    }

    @Transactional
    public void deleteProductAndWishlistAndOptions(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        wishlistRepository.deleteByProduct(product);
        List<Option> options = optionRepository.findAllByProductId(id);
        optionRepository.deleteAll(options);
        productRepository.delete(product);
    }

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getName(), String.valueOf(product.getPrice()),
            product.getCategory().getId(), product.getImageUrl());
    }

    public static Product toEntity(ProductDTO productDTO, Long id, Category category) {
        Product product = new Product(id, productDTO.name(), productDTO.price(),
            category, productDTO.imageUrl());
        return product;
    }
}
