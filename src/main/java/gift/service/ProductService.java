package gift.service;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.OptionDTO;
import gift.dto.product.AddProductResponse;
import gift.dto.product.ProductDto;
import gift.exception.NoSuchProductException;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final OptionService optionService;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService,
        OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    public Page<ProductDto> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(product -> product.toDto());
    }

    public ProductDto getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new)
            .toDto();
    }

    public AddProductResponse addProduct(ProductDto productDto, List<OptionDTO> optionDTOs) {
        Category category = categoryService.getCategory(productDto.categoryId()).toEntity();
        Product product = productRepository.save(productDto.toEntity(category));
        for (OptionDTO optionDTO : optionDTOs) {
            optionService.addOption(product.getId(), optionDTO);
        }
        return product.toAddProductResponse();
    }

    public ProductDto updateProduct(long id, ProductDto productDTO) {
        getProduct(id);
        Category category = categoryService.getCategory(productDTO.categoryId()).toEntity();
        Product product = new Product(id, productDTO.name(), productDTO.price(), productDTO.imageUrl(), category);
        return productRepository.save(product).toDto();
    }

    public ProductDto deleteProduct(long id) {
        Product deletedProduct = productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new);
        productRepository.delete(deletedProduct);
        return deletedProduct.toDto();
    }
}
