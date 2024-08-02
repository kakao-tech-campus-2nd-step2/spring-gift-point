package gift.service;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.OneProductResponceDTO;
import gift.dto.ProductRequestDTO;
import gift.dto.ProductResponseDTO;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<ProductResponseDTO> getProducts(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort[1]), sort[0]));
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponseDTO> productResponseDTOList = productPage.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(productResponseDTOList, pageable, productPage.getTotalElements());
    }

//    public ProductResponseDTO getProductById(Long id) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));
//        return convertToResponseDTO(product);
//    }

    @Transactional(readOnly = true)
    public OneProductResponceDTO getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return new OneProductResponceDTO(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
        } else {
            throw new IllegalArgumentException("Product not found");
        }
    }

    public void createProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id: " + productRequestDTO.getCategoryId()));

        Product product = new Product(productRequestDTO.getName(),productRequestDTO.getPrice(),productRequestDTO.getImageUrl(),category);
        productRepository.save(product);
    }

    public void updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));

        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id: " + productRequestDTO.getCategoryId()));

        existingProduct.updateName(productRequestDTO.getName());
        existingProduct.updatePrice(productRequestDTO.getPrice());
        existingProduct.updateImageUrl(productRequestDTO.getImageUrl());
        existingProduct.updateCategory(category);

        productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getName(),
                product.getCategory().getId()
        );
    }

    @Transactional(readOnly = true)
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
    }

    public Page<OneProductResponceDTO> getProductsPage(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<OneProductResponceDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable).map(this::convertToDTO);
    }

    public OneProductResponceDTO convertToDTO(Product product) {
        return new OneProductResponceDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getId()
        );
    }
}
