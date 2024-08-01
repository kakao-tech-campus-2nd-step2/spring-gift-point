package gift.service;


import gift.dto.productDto.ProductDto;
import gift.dto.productDto.ProductMapper;
import gift.dto.productDto.ProductResponseDto;
import gift.exception.ValueAlreadyExistsException;
import gift.exception.ValueNotFoundException;
import gift.model.product.Category;
import gift.model.product.Product;
import gift.model.product.ProductName;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponseDto addNewProduct(ProductDto productDto){
        if (productRepository.existsByName(new ProductName(productDto.name()))) {
            throw new ValueAlreadyExistsException("ProductName already exists in Database");
        }
        Category category = findCategory(productDto.categoryId());
        Product product = new Product(category, new ProductName(productDto.name()),productDto.price(),productDto.imageUrl());
        Product savedProduct = productRepository.save(product);

        return productMapper.toProductResponseDto(savedProduct);
    }

    public ProductResponseDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ValueNotFoundException("Product not exists in Database"));
        Category category = findCategory(productDto.categoryId());

        Product newProduct = new Product(category,new ProductName(productDto.name()),productDto.price(),productDto.imageUrl());
        product.updateProduct(newProduct);
        Product savedProduct = productRepository.save(product);

        return productMapper.toProductResponseDto(savedProduct);
    }

    public ProductResponseDto getProductById(Long id){
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ValueNotFoundException("Product not exists in Database"));
        return productMapper.toProductResponseDto(product);
    }

    public Product selectProduct(Long id) {
        return productRepository.findById(id).get();
    }

    public Page<Product> getAllProducts(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public void DeleteProduct(Long id){
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ValueNotFoundException("Product not exists in Database"));
        productRepository.delete(product);
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).
                orElseThrow(() -> new ValueNotFoundException("Category not exists in Database"));
    }
}
