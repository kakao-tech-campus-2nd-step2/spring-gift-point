package gift.service;

import gift.dto.ProductDTO;
import gift.model.Product;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public Page<ProductDTO> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(product -> ProductDTO.convertToDTO(product, categoryService));
    }

    @Override
    public Page<ProductDTO> getProductsByCategoryId(Pageable pageable, int categoryId) {
        return productRepository.findByCategory(pageable, categoryId).map(product -> ProductDTO.convertToDTO(product, categoryService));
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(product -> ProductDTO.convertToDTO(product, categoryService)).orElse(null);
    }

    @Override
    public void saveProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow();
        product.updateFromDTO(productDTO);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
