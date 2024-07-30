package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import gift.product.dto.ProductDTO;
import gift.product.exception.InvalidIdException;
import gift.product.model.Option;
import gift.product.model.Product;
import gift.product.repository.CategoryRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import gift.product.validation.ProductValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductValidation productValidation;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public ProductService(
        ProductRepository productRepository,
        ProductValidation productValidation,
        CategoryRepository categoryRepository,
        OptionRepository optionRepository
    ) {
        this.productRepository = productRepository;
        this.productValidation = productValidation;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Product registerProduct(ProductDTO productDTO) {
        System.out.println("[ProductService] registerProduct()");
        productValidation.registerValidation(productDTO);
        Product product = productRepository.save(productDTO.convertToDomain(categoryRepository));
        registerDefaultOption(product);
        return product;
    }

    private void registerDefaultOption(Product product) {
        Option option = new Option(
            product.getName(),
            0,
            product
        );
        optionRepository.save(option);
    }

    public Product updateProduct(Long id, ProductDTO productDTO) {
        System.out.println("[ProductService] updateProduct()");
        productValidation.updateValidation(id, productDTO);
        return productRepository.save(productDTO.convertToDomain(id, categoryRepository));
    }

    public void deleteProduct(Long id) {
        productValidation.deleteValidation(id);
        if(optionRepository.existsByProductId(id))
            optionRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    public ProductDTO getDTOById(Long id) {
        System.out.println("[ProductService] getDTOById()");
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        return product.convertToDTO();
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByName(keyword, pageable);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
    }
}
