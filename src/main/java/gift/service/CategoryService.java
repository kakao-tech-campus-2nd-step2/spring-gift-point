package gift.service;

import gift.domain.Category;
import gift.domain.Category.CreateCategory;
import gift.domain.Category.DetailCategory;
import gift.domain.Category.SimpleCategory;
import gift.domain.Category.UpdateCategory;
import gift.entity.CategoryEntity;
import gift.mapper.CategoryMapper;
import gift.repository.CategoryRepository;
import gift.util.errorException.BaseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Page<SimpleCategory> getCategoryList(Category.getList req) {
        return categoryMapper.toSimpleList(categoryRepository.findAll(req.toPageable()));
    }

    public DetailCategory getCategory(long id) {
        CategoryEntity category = categoryRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."));

        return categoryMapper.toDetail(category);
    }

    public Long createCategory(CreateCategory create) {
        if (categoryRepository.findByName(create.getName()).isPresent()) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "해당 이름의 카테고리가 이미 존재합니다.");
        }

        CategoryEntity category = categoryMapper.toEntity(create);
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public Long updateCategory(long id, UpdateCategory update) {
        if (categoryRepository.findByName(update.getName()).isPresent()) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "해당 이름의 카테고리가 이미 존재합니다.");
        }

        CategoryEntity category = categoryRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."));

        categoryMapper.toUpdate(update, category);
        return category.getId();
    }

    public Long deleteCategory(long id) {
        CategoryEntity category = categoryRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."));

        if (category.getProductEntities().size() != 0) {
            throw new BaseHandler(HttpStatus.FORBIDDEN, "상품이 없는 카테고리만 삭제가 가능합니다.");
        }

        categoryRepository.delete(category);
        return category.getId();
    }
}
