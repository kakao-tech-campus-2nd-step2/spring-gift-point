package gift.service;

import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public ProductDTO getProductById(long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        Product product = new Product(null, productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), category, null);
        Product savedProduct = productRepository.save(product);
        final Product finalSavedProduct = savedProduct;
        List<Option> options = productDTO.getOptions().stream()
                .map(optionDTO -> new Option(optionDTO.getName(), optionDTO.getQuantity(), finalSavedProduct))
                .collect(Collectors.toList());
        optionRepository.saveAll(options);
        savedProduct = new Product(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice(), savedProduct.getImageUrl(), savedProduct.getCategory(), options);
        savedProduct.validateOptions();
        savedProduct = productRepository.save(savedProduct);
        return convertToDTO(savedProduct);
    }


    @Transactional
    public ProductDTO updateProduct(long id, ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        List<Option> options = productDTO.getOptions().stream()
                .map(optionDTO -> new Option(optionDTO.getName(), optionDTO.getQuantity(), null))
                .collect(Collectors.toList());
        Product product = new Product(id, productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), category, options);
        options.forEach(option -> option.assignProduct(product));
        product.validateOptions();
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Transactional
    public void deleteProductById(long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setCategoryName(product.getCategory().getName());
        productDTO.setOptions(product.getOptions().stream()
                .map(this::convertOptionToDTO)
                .collect(Collectors.toList()));
        return productDTO;
    }

    public Product convertToEntity(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        List<Option> options = productDTO.getOptions().stream()
                .map(optionDTO -> new Option(optionDTO.getName(), optionDTO.getQuantity(), null))
                .collect(Collectors.toList());
        Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), category, options);
        options.forEach(option -> option.assignProduct(product));
        return product;
    }

    private OptionDTO convertOptionToDTO(Option option) {
        return new OptionDTO(option.getId(), option.getName(), option.getQuantity());
    }
}
