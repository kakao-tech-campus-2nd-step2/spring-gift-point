package gift.service;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.dto.requestdto.OptionCreateRequestDTO;
import gift.dto.requestdto.ProductCreateRequestDTO;
import gift.dto.requestdto.ProductRequestDTO;
import gift.dto.responsedto.ProductResponseDTO;
import gift.repository.JpaCategoryRepository;
import gift.repository.JpaOptionRepository;
import gift.repository.JpaProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ProductService {
    private final JpaProductRepository jpaProductRepository;
    private final JpaCategoryRepository jpaCategoryRepository;
    private final JpaOptionRepository jpaOptionRepository;

    public ProductService(JpaProductRepository jpaProductRepository,
        JpaCategoryRepository jpaCategoryRepository, JpaOptionRepository jpaOptionRepository) {
        this.jpaProductRepository = jpaProductRepository;
        this.jpaCategoryRepository = jpaCategoryRepository;
        this.jpaOptionRepository = jpaOptionRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {

        return jpaProductRepository.findAll()
            .stream()
            .map(ProductResponseDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts(int page, int size, String criteria) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(criteria));

        return jpaProductRepository.findAll(pageable)
            .stream()
            .map(ProductResponseDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getOneProduct(Long productId) {
        Product product = getProduct(productId);
        return ProductResponseDTO.from(product);
    }

    public Long addProduct(ProductCreateRequestDTO productCreateRequestDTO) {
        ProductRequestDTO productRequestDTO = productCreateRequestDTO.productRequestDTO();
        OptionCreateRequestDTO optionCreateRequestDTO = productCreateRequestDTO.optionCreateRequestDTO();

        Category category = getCategory(productRequestDTO);

        Product product = productRequestDTO.toEntity(category);
        Option option = optionCreateRequestDTO.toEntity(product);

        jpaOptionRepository.save(option);
        return jpaProductRepository.save(product).getId();
    }

    public Long updateProduct(Long productId, ProductRequestDTO productRequestDTO) {
        Product product = getProduct(productId);
        Category category = getCategory(productRequestDTO);

        product.update(productRequestDTO.name(), productRequestDTO.price(),
            productRequestDTO.imageUrl(), category);
        return product.getId();
    }

    public Long deleteProduct(Long productId) {
        Product product = getProduct(productId);
        jpaProductRepository.delete(product);
        return product.getId();
    }

    private Product getProduct(Long productId) {
        return jpaProductRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
    }

    private Category getCategory(ProductRequestDTO productRequestDTO) {
        return jpaCategoryRepository.findById(productRequestDTO.categoryId())
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
    }
}
