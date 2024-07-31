package gift.product.service;

import gift.core.PagedDto;
import gift.core.domain.product.ProductCategory;
import gift.core.domain.product.ProductCategoryRepository;
import gift.core.domain.product.ProductCategoryService;
import gift.core.domain.product.exception.CategoryAlreadExistsException;
import gift.core.domain.product.exception.CategoryNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public PagedDto<ProductCategory> findAll(Pageable pageable) {
        return productCategoryRepository.findAll(pageable);
    }

    @Override
    public void createCategory(ProductCategory category) {
        if (productCategoryRepository.findByName(category.name()).isPresent()) {
            throw new CategoryAlreadExistsException();
        }
        productCategoryRepository.save(category);
    }

    @Override
    public void updateCategory(ProductCategory category) {
        if (productCategoryRepository.findById(category.id()).isEmpty()) {
            throw new CategoryNotFoundException();
        }
        productCategoryRepository.save(category);
    }
}
