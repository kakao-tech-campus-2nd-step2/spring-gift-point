package gift.product.service;

import gift.category.domain.Category;
import gift.category.repository.CategoryRepository;
import gift.product.option.domain.Option;
import gift.product.option.domain.OptionDTO;
import gift.product.option.service.OptionService;
import gift.product.domain.Product;
import gift.product.domain.ProductDTO;
import gift.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository,
        OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionService = optionService;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(this::convertToDTO);
    }

    public Page<ProductDTO> getAllProductsByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.getProductsByCategoryId(categoryId, pageable)
            .map(this::convertToDTO);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductDTOById(Long id) {
        return productRepository.findById(id)
            .map(this::convertToDTO);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public ProductDTO createProduct(@Valid ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("Category id " + productDTO.getCategoryId() + "가 없습니다."));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);
        if(productDTO.getOptionDTOList().size()>0){
            Option tempOption = new Option("tempOption", 1L);
            product.addOption(tempOption);
        }
        product = productRepository.save(product);

        product.getOptionList().clear();
        final Long productId = product.getId();
        productDTO.getOptionDTOList().forEach(optionDTO -> optionDTO.setProductId(productId));

        final Product finalProduct = product;
        productDTO.getOptionDTOList().forEach(optionDTO -> {
            Option option = optionService.convertToEntity(optionDTO);
            option.setProduct(finalProduct);
            finalProduct.addOption(option);
        });

        product = productRepository.save(finalProduct);

        return convertToDTO(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, @Valid ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("Category id " + productDTO.getCategoryId() + "가 없습니다."));

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product id " + id + "가 없습니다."));

        productDTO.getOptionDTOList().forEach(optionDTO -> optionDTO.setProductId(id));

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);

        final Product finalProduct = product;
        List<Option> existingOptions = product.getOptionList();

        productDTO.getOptionDTOList().forEach(optionDTO -> {
            Option existingOption = existingOptions.stream()
                .filter(option -> option.getName().equals(optionDTO.getName()))
                .findFirst()
                .orElse(null);

            if (existingOption != null) {
                optionService.updateOption(id, existingOption.getId(), optionDTO);
            } else {
                Option newOption = optionService.convertToEntity(optionDTO);
                newOption.setProduct(finalProduct);
                finalProduct.addOption(newOption);
            }
        });

        product = productRepository.save(product);
        return convertToDTO(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductDTO convertToDTO(Product product) {
        List<OptionDTO> optionDTOList = optionService.findAllByProductId(product.getId());
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            optionDTOList
        );
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("Category id " + productDTO.getCategoryId() + "가 없습니다."));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);

        productDTO.getOptionDTOList().forEach(optionDTO -> {
            Option option = optionService.convertToEntity(optionDTO);
            product.addOption(option);
        });

        return product;
    }
}
