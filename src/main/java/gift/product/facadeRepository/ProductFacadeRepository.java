package gift.product.facadeRepository;

import gift.product.entity.Product;
import gift.repository.JpaCategoryRepository;
import gift.repository.JpaGiftOptionRepository;
import gift.repository.JpaProductRepository;
import gift.exceptionAdvisor.exceptions.GiftBadRequestException;
import gift.exceptionAdvisor.exceptions.GiftNotFoundException;
import gift.category.entity.Category;
import gift.option.entity.GiftOption;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ProductFacadeRepository {

    private final JpaProductRepository jpaProductRepository;

    private final JpaCategoryRepository jpaCategoryRepository;

    private final JpaGiftOptionRepository jpaGiftOptionRepository;

    public ProductFacadeRepository(JpaProductRepository jpaProductRepository,
        JpaCategoryRepository jpaCategoryRepository,
        JpaGiftOptionRepository jpaGiftOptionRepository) {
        this.jpaProductRepository = jpaProductRepository;
        this.jpaCategoryRepository = jpaCategoryRepository;
        this.jpaGiftOptionRepository = jpaGiftOptionRepository;
    }

    public List<Product> findAll() {
        return jpaProductRepository.findAll();
    }

    public Product saveProduct(Product product) {

        List<GiftOption> giftOptions = product.getGiftOptionList();
        Category category = product.getCategory();

        if (giftOptions.isEmpty()) {
            throw new GiftBadRequestException("상품 옵션이 필요합니다.");
        }

        if (category == null) {
            throw new GiftBadRequestException("상품 카테고리가 필요합니다.");
        }

        category = jpaCategoryRepository.findById(category.getId())
            .orElseThrow(() -> new GiftNotFoundException("카테고리가 존재하지 않습니다."));

        category.addProduct(product);

        jpaGiftOptionRepository.saveAll(product.getGiftOptionList());
        return jpaProductRepository.save(product);
    }

    public Product getProduct(long id) {
        Product product = jpaProductRepository.findById(id)
            .orElseThrow(() -> new GiftNotFoundException("상품이 존재하지 않습니다."));

        return product;
    }

    public void deleteProduct(long id) {
        Product product = jpaProductRepository.findById(id)
            .orElseThrow(() -> new GiftNotFoundException("상품이 존재하지 않습니다."));

        List<GiftOption> giftOptionList = product.getGiftOptionList();
        Category category = product.getCategory();

        category.deleteProduct(product);

        jpaCategoryRepository.save(category);
        jpaGiftOptionRepository.deleteAll(giftOptionList);
        jpaProductRepository.delete(product);

    }

    public List<Page<Product>> findPageList(Pageable pageable) {
        return null;
    }
}
