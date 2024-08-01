package gift.Service;

import gift.DTO.ProductDTO;
import gift.Entity.ProductEntity;
import gift.Repository.ProductRepository;
import gift.Mapper.ProductServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceMapper productServiceMapper;

    public List<ProductDTO> findAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productServiceMapper.convertToProductDTOs(productEntities);
    }

    public Optional<ProductDTO> findProductById(Long id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        return productEntity.map(productServiceMapper::convertToDTO);
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productServiceMapper.convertToEntity(productDTO);
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return productServiceMapper.convertToDTO(savedProductEntity);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Optional<ProductEntity> existingProductOption = productRepository.findById(id);
        if (existingProductOption.isPresent()) {
            ProductEntity existingProduct = existingProductOption.get();
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setImageUrl(productDTO.getImageUrl());
            existingProduct.setCategory(productServiceMapper.convertToCategoryEntity(productDTO.getCategory()));
            existingProduct.setWishes(productServiceMapper.convertToWishEntities(productDTO.getWishes()));
            existingProduct.setOptions(productServiceMapper.convertToOptionEntities(productDTO.getOptions()));
            ProductEntity updatedProductEntity = productRepository.save(existingProduct);
            return productServiceMapper.convertToDTO(updatedProductEntity);
        } else {
            throw new RuntimeException("변경하려는 상품이 존재하지 않습니다.");
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Page<ProductDTO> getProducts(Pageable pageable) {
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        return productPage.map(productServiceMapper::convertToDTO);
    }
}
