package gift.service;


import gift.dto.ProductResponseDTO;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> findByName(String name){
        return productRepository.findByName(name);
    }

    public void saveProduct(Product product) {
        if(findByName(product.getName()).isPresent()){
            throw new CustomException(ErrorCode.PRODUCT_NAME_DUPLICATED);
        }
        productRepository.save(product);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).
            orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }


    public void updateProduct(Product product, Long id) {

        Product update = getProductById(id);
        update.setName(product.getName());
        update.setPrice(product.getPrice());
        update.setImageUrl(product.getImageUrl());
        update.setCategory(product.getCategory());
        productRepository.save(update);


    }

    public void deleteProduct(Long id) {
        getProductById(id);
        productRepository.deleteById(id);
    }


    public Page<Product> getProductPage(Pageable pageable,Long categoryId) {
        if (categoryId == null) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findAllByCategoryId(pageable,categoryId);
    }


}
