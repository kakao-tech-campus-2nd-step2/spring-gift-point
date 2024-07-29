package gift.product.service;

import gift.category.domain.Category;
import gift.category.repository.CategoryRepository;
import gift.category.service.CategoryService;
import gift.option.domain.Option;
import gift.option.domain.OptionDTO;
import gift.option.repository.OptionRepository;
import gift.option.service.OptionService;
import gift.product.domain.Product;
import gift.product.domain.ProductDTO;
import gift.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository,
        OptionRepository optionRepository, OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
        this.optionService = optionService;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(this::convertToDTO);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToDTO)
            .toList();
    }

    public Optional<ProductDTO> getProductDTOById(Long id) {
        return productRepository.findById(id)
            .map(this::convertToDTO);
    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById((id));
    }

    public void createProduct(@Valid ProductDTO productDTO) {
        Product product = convertToEntityWithoutOptions(productDTO);
        product = productRepository.save(product);

        Long productId = product.getId();
        productDTO.getOptionDTOList().forEach(optionDTO -> optionDTO.setProductId(productId));

        updateProduct(product.getId(), productDTO);
        optionService.saveAll(productDTO.getOptionDTOList());
    }

    public void updateProduct(Long id, @Valid ProductDTO productDTO) {
        if (productRepository.existsById(id)) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category id " + productDTO.getCategoryId() + "가 없습니다."));

            Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product id " + id + "가 없습니다."));

            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setImageUrl(productDTO.getImageUrl());
            product.setCategory(category);

            List<Option> optionList = productDTO.getOptionDTOList().stream()
                .map(optionService::convertToEntity)
                .peek(option -> option.setProduct(product))
                .toList();

            product.getOptionList().clear();
            product.getOptionList().addAll(optionList);

            productRepository.save(product);
        }
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
        Category category = categoryRepository.findById(productDTO.getCategoryId()).get();

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);

        List<Option> optionList = productDTO.getOptionDTOList().stream()
            .map(optionService::convertToEntity)
            .toList();
        product.setOptionList(optionList);
        return product;
    }
    private Product convertToEntityWithoutOptions(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("Category id " + productDTO.getCategoryId() + "가 없습니다."));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);

        List<Option> optionList = new ArrayList<>();
        optionList.add(new Option("temp", 1L));
        product.setOptionList(optionList);
        return product;
    }
    private Option convertToOptionEntity(OptionDTO optionDTO, Long productId) {
        Option option = new Option(optionDTO.getName(), optionDTO.getQuantity());

        Product product = new Product();
        product.setId(productId);
        option.setProduct(product);

        return option;
    }
}
