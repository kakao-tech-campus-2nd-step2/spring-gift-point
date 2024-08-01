package gift.Service;

import gift.Entity.Product;
import gift.Mapper.Mapper;
import gift.Model.ProductDto;
import gift.Repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductJpaRepository productJpaRepository;
    private final Mapper mapper;

    @Autowired
    public ProductService(ProductJpaRepository productJpaRepository, Mapper mapper) {
        this.productJpaRepository = productJpaRepository;
        this.mapper = mapper;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productJpaRepository.findAll();
        List<ProductDto> productsToDto = products.stream()
                .map(mapper::productToDto)
                .collect(Collectors.toList());
        return productsToDto;
    }

    public Page<ProductDto> getAllProductsByPage(Pageable pageable) {
        Page<Product> products = productJpaRepository.findAll(pageable);
        return products.map(mapper::productToDto);

    }

    public Optional<ProductDto> getProductById(Long id) {
        Optional<Product> product = productJpaRepository.findById(id);
        return product.map(mapper::productToDto);

    }

    public void saveProduct(ProductDto productDto) {
        Product product = mapper.productDtoToEntity(productDto);
        productJpaRepository.save(product);

    }

    public Product updateProduct(ProductDto productDtoDetails) {
        Product product = mapper.productDtoToEntity(productDtoDetails);
        return productJpaRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productJpaRepository.deleteById(id);
    }
}
