package gift.service;

import gift.dto.option.OptionDTO;
import gift.dto.product.*;
import gift.exceptions.CustomException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, OptionRepository optionRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
        this.categoryService = categoryService;
    }

    public ProductsPageResponseDTO getAllProductsByCategoryId(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        ProductsPageResponseDTO productsPageResponseDTO = new ProductsPageResponseDTO(productPage);

        return productsPageResponseDTO;
    }

    public ProductResponseDTO getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(CustomException::productNotFoundException);
        List<Option> options = optionRepository.findAllByProductId(productId);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO(product.getId(),
                                                                       product.getName(),
                                                                       product.getPrice(),
                                                                       product.getImageUrl(),
                                                                       product.getCategory().getId(),
                                                                       options);

        return productResponseDTO;
    }

    public ProductAddResponseDTO createProduct(ProductAddRequestDTO productAddRequestDTO) {
        Category category = categoryService.getCategoryById(productAddRequestDTO.categoryId());
        Product product = new Product(productAddRequestDTO.name(),
                                      productAddRequestDTO.price(),
                                      productAddRequestDTO.imageUrl(),
                                      category);
        productRepository.save(product);

        String name = productAddRequestDTO.option().name();
        Long quantity = productAddRequestDTO.option().quantity();
        ProductAddResponseDTO productAddResponseDTO = new ProductAddResponseDTO(product.getId(),
                                                                                product.getName(),
                                                                                product.getPrice(),
                                                                                product.getImageUrl(),
                                                                                product.getCategory().getId(),
                                                                                new OptionDTO(name, quantity));

        return productAddResponseDTO;

    }

    public ProductModifyResponseDTO updateProduct(Long id, ProductModifyRequestDTO productModifyRequestDTO) {
        Category category = categoryService.getCategoryById(productModifyRequestDTO.categoryId());
        Product product = productRepository.findById(id).orElseThrow(CustomException::productNotFoundException);

        product.setName(productModifyRequestDTO.name());
        product.setPrice(productModifyRequestDTO.price());
        product.setImageUrl(productModifyRequestDTO.imageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return new ProductModifyResponseDTO(savedProduct.getId(),
                                            savedProduct.getName(),
                                            savedProduct.getPrice(),
                                            savedProduct.getImageUrl(),
                                            savedProduct.getCategory().getId());
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(CustomException::productNotFoundException);
        productRepository.deleteById(product.getId());
    }
}
