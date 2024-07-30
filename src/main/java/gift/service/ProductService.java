package gift.service;

import gift.domain.Category;
import gift.domain.CategoryName;
import gift.domain.Option;
import gift.domain.Product;
import gift.dto.ProductDTO;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.util.AlphanumericComparator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionService = optionService;
    }

    public List<ProductDTO> findAllProducts() {
        return productRepository.findAll().stream()
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }

    public Page<ProductDTO> findAllProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);;
        List<ProductDTO> productsDTO = productsPage.stream()
            .map(ProductDTO::from)
            .collect(Collectors.toList());
        return new PageImpl<>(productsDTO, pageable, productsPage.getTotalElements());
    }

    private List<ProductDTO> getSortedProducts() {
        List<Product> products = productRepository.findAll();
        products.sort(Comparator.comparing(Product::getName, new AlphanumericComparator()));
        return products.stream()
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }

    private Page<ProductDTO> paginate(List<ProductDTO> products, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<ProductDTO> paginatedList = getPaginatedList(products, startItem, pageSize);

        return new PageImpl<>(paginatedList, pageable, products.size());
    }

    private List<ProductDTO> getPaginatedList(List<ProductDTO> products, int startItem, int pageSize) {
        if (products.size() <= startItem) {
            return Collections.emptyList();
        }

        int toIndex = Math.min(startItem + pageSize, products.size());
        return products.subList(startItem, toIndex);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findByName(productDTO.getCategoryName())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product product = productDTO.toEntity(category);
        Product savedProduct = productRepository.save(product);

        List<Option> options = productDTO.getOptions().stream()
            .map(optionDTO -> optionDTO.toEntity(savedProduct))
            .collect(Collectors.toList());
        optionService.validateProductOptions(options);
        Product productWithOptions = savedProduct.toBuilder().options(options).build();
        Product finalProduct = productRepository.save(productWithOptions);
        return ProductDTO.from(finalProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Category category = categoryRepository.findByName(productDTO.getCategoryName())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Product updatedProduct = existingProduct.toBuilder()
            .name(productDTO.getName())
            .price(productDTO.getPrice())
            .imageUrl(productDTO.getImageUrl())
            .description(productDTO.getDescription())
            .category(category)
            .build();

        if (productDTO.getOptions() == null || productDTO.getOptions().isEmpty()) {
            throw new IllegalArgumentException("A product must have at least one option.");
        }

        List<Option> options = productDTO.getOptions().stream()
            .map(optionDTO -> optionDTO.toEntity(updatedProduct))
            .collect(Collectors.toList());

        Product productWithOptions = updatedProduct.toBuilder().options(options).build();
        optionService.validateProductOptions(productWithOptions.getOptions());
        Product finalProduct = productRepository.save(productWithOptions);
        return ProductDTO.from(finalProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        return ProductDTO.from(product);
    }

    public List<ProductDTO> findProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }

    public Page<ProductDTO> findProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productsPage = productRepository.findByCategoryId(categoryId, pageable);
        List<ProductDTO> productsDTO = productsPage.stream()
            .map(ProductDTO::from)
            .collect(Collectors.toList());
        return new PageImpl<>(productsDTO, pageable, productsPage.getTotalElements());
    }

    @Transactional
    public ProductDTO updateProductCategory(Long productId, CategoryName categoryName) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Category category = categoryRepository.findByName(categoryName)
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Product updatedProduct = product.toBuilder()
            .category(category)
            .build();
        productRepository.save(updatedProduct);
        return ProductDTO.from(updatedProduct);
    }
}
