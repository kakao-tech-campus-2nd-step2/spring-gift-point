package gift.service;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.exception.NoSuchProductException;
import gift.repository.ProductRepository;
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

    public Page<ProductDTO> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(product -> product.toDTO());
    }

    public ProductDTO getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new)
            .toDTO();
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        Category category = categoryService.getCategory(productDTO.categoryId()).toEntity();
        Product product = productRepository.save(productDTO.toEntity(category));
        for (OptionDTO optionDTO : productDTO.optionDTOs()) {
            optionService.addOption(product.getId(), optionDTO);
        }
        return product.toDTO();
    }

    public ProductDTO updateProduct(long id, ProductDTO productDTO) {
        getProduct(id);
        Category category = categoryService.getCategory(productDTO.categoryId()).toEntity();
        Product product = new Product(id, productDTO.name(), productDTO.price(), productDTO.imageUrl(), category);
        return productRepository.save(product).toDTO();
    }

    public ProductDTO deleteProduct(long id) {
        Product deletedProduct = productRepository.findById(id)
            .orElseThrow(NoSuchProductException::new);
        productRepository.delete(deletedProduct);
        return deletedProduct.toDTO();
    }
}
