package gift.service;


import gift.dto.Request.ProductRequestDto;
import gift.dto.Response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductResponseDto> getProductsByCategoryId(Pageable pageable, int categoryId);
    ProductResponseDto getProductById(Long id);
    void saveProduct(ProductRequestDto productDTO);
    void updateProduct(Long id, ProductRequestDto productDTO);
    void deleteProduct(Long id);
    Page<ProductResponseDto> getProducts(Pageable pageable);
}
