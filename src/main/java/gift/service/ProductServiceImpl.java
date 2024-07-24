package gift.service;

import gift.database.JpaCategoryRepository;
import gift.database.JpaProductRepository;
import gift.dto.ProductDTO;
import gift.exceptionAdvisor.exceptions.CategoryNoSuchException;
import gift.exceptionAdvisor.exceptions.ProductNoSuchException;
import gift.exceptionAdvisor.exceptions.ProductServiceException;
import gift.model.Category;
import gift.model.Product;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private JpaProductRepository jpaProductRepository;

    private JpaCategoryRepository jpaCategoryRepository;

    public ProductServiceImpl(JpaProductRepository jpaProductRepository,
        JpaCategoryRepository jpaCategoryRepository) {
        this.jpaProductRepository = jpaProductRepository;
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    @Override
    public List<ProductDTO> readAll() {
        return jpaProductRepository.findAll().stream().map(
            product -> new ProductDTO(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl(),product.getCategoryId())).toList();
    }

    @Override
    public void create(ProductDTO dto) {
        checkKakao(dto.getName());
        Product product = new Product(null, dto.getName(), dto.getPrice(), dto.getImageUrl());
        jpaProductRepository.save(product);

        Category category = checkCategory(dto.getCategoryId());
        product.updateCategory(category);
    }


    @Override
    public void update(long id, ProductDTO dto) {
        jpaProductRepository.findById(id).orElseThrow(ProductNoSuchException::new);

        Product product = new Product(id, dto.getName(), dto.getPrice(), dto.getImageUrl());
        Category category = checkCategory(dto.getCategoryId());

        product.updateCategory(category);
        jpaProductRepository.save(product);
    }

    @Override
    public void delete(long id) {
        jpaProductRepository.deleteById(id);
    }

    private void checkKakao(String productName) {
        if (productName.contains("카카오")) {
            throw new ProductServiceException("카카오 문구는 md협의 이후 사용할 수 있습니다.",
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<ProductDTO> readProduct(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return jpaProductRepository.findAll(pageable).stream().map(product ->
            new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()))
            .toList();
    }

    // private //

    private Product getProduct(long id) {
        var prod = jpaProductRepository.findById(id).orElseThrow(ProductNoSuchException::new);
        checkKakao(prod.getName());
        return prod;
    }

    private Category checkCategory(long id) {
        return jpaCategoryRepository.findById(id).orElseThrow(CategoryNoSuchException::new);
    }
}
