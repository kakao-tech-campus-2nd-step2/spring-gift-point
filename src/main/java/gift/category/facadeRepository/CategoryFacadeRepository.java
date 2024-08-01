package gift.category.facadeRepository;

import gift.category.entity.Category;
import gift.exceptionAdvisor.exceptions.GiftBadRequestException;
import gift.exceptionAdvisor.exceptions.GiftNotFoundException;
import gift.repository.JpaCategoryRepository;
import gift.repository.JpaProductRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CategoryFacadeRepository {

    private final JpaCategoryRepository jpaCategoryRepository;

    private final JpaProductRepository jpaProductRepository;

    public CategoryFacadeRepository(JpaCategoryRepository jpaCategoryRepository,
        JpaProductRepository jpaProductRepository) {
        this.jpaCategoryRepository = jpaCategoryRepository;
        this.jpaProductRepository = jpaProductRepository;
    }

    public Category get(Long id) {
        return jpaCategoryRepository.findById(id)
            .orElseThrow(() -> new GiftNotFoundException("카테고리가 존재하지 않습니다."));
    }

    public Category save(Category category) {

        if (category.getId() == null && !category.getProductList().isEmpty()) {
            throw new GiftNotFoundException("카테고리가 존재하지 않습니다. 카테고리를 먼저 생성해주세요");
        }

        category.getProductList().stream()
            .map(product -> jpaProductRepository.existsById(product.getId())).forEach(exists -> {
                if (!exists) {
                    throw new GiftNotFoundException("상품이 존재하지 않습니다.");
                }
            }); //TODO : 한번에 묶어서 실행하는 방법은 없는가?

        return jpaCategoryRepository.save(category);
    }

    public void delete(Long id) {
        Category category = jpaCategoryRepository.findById(id)
            .orElseThrow(() -> new GiftNotFoundException("카테고리가 존재하지 않습니다."));

        if (!category.getProductList().isEmpty()) {
            throw new GiftBadRequestException("카테고리에 상품이 존재합니다.");
        }

        jpaCategoryRepository.delete(category);
    }

    public List<Category> findAll() {
        return jpaCategoryRepository.findAll();
    }
}
