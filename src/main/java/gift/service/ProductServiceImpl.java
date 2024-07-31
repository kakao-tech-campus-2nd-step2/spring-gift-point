package gift.service;

import gift.dto.Request.ProductRequestDto;
import gift.dto.Response.CategoryResponseDto;
import gift.dto.Response.ProductResponseDto;
import gift.model.Category;
import gift.model.Product;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductResponseDto> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::convertToResponseDto);
    }

    @Override
    public Page<ProductResponseDto> getProductsByCategoryId(Pageable pageable, int categoryId) {
        return productRepository.findByCategory(pageable, categoryId).map(this::convertToResponseDto);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        return productRepository.findById(id).map(this::convertToResponseDto).orElse(null);
    }

    @Override
    public void saveProduct(ProductRequestDto productDTO) {
        Product product = convertToEntity(productDTO);
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Long id, ProductRequestDto productDTO) {
        Product product = productRepository.findById(id).orElseThrow();
        updateEntityFromDto(product, productDTO);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductResponseDto convertToResponseDto(Product product) {
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            new CategoryResponseDto(
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getColor(),
                product.getCategory().getImageUrl(),
                product.getCategory().getDescription()
            )
        );
    }

    private Product convertToEntity(ProductRequestDto productDTO) {
        Category category = new Category();
        category.setId(productDTO.categoryId());

        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setImageUrl(productDTO.imageUrl());
        product.setCategory(category);

        return product;
    }

    private void updateEntityFromDto(Product product, ProductRequestDto productDTO) {
        Category category = new Category();
        category.setId(productDTO.categoryId());

        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setImageUrl(productDTO.imageUrl());
        product.setCategory(category);
    }
}
