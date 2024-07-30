package gift.service;

import gift.dto.request.ProductRequestDTO;
import gift.dto.response.ProductResponseDTO;
import gift.entity.Category;
import gift.entity.Product;
import gift.exception.categoryException.CategoryNotFoundException;
import gift.exception.productException.ProductNotFoundException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Description("Product get method")
    public ProductResponseDTO getProduct(Long productId) {
        Product product = getProductEntity(productId);
        return toDto(product);
    }

    @Description("Product get method")
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Description("Product add method")
    public void addProduct(ProductRequestDTO productRequestDTO){
        Long categoryId = productRequestDTO.categoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        Product product = toEntity(productRequestDTO, category);
        productRepository.save(product);
    }

    @Description("Product remove method")
    public void removeProduct(Long productId) {
        Product product = getProductEntity(productId);
        productRepository.delete(product);
    }

    @Description("Product update method")
    public void updateProduct(Long productId, ProductRequestDTO productRequestDTO) {
        Product product = getProductEntity(productId);
        product.updateProduct(productRequestDTO);
        productRepository.save(product);
    }

    private Product getProductEntity(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private ProductResponseDTO toDto(Product product){
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    private Product toEntity(ProductRequestDTO dto, Category category) {
        return new Product(
                dto.name(),
                dto.price(),
                dto.imageUrl(),
                category
        );
    }

}
