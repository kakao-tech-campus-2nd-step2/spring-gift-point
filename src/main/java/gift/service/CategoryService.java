package gift.service;

import gift.domain.Category.Category;
import gift.domain.Category.CategoryRequest;
import gift.domain.Category.CategoryResponse;
import gift.domain.Menu.Menu;
import gift.repository.CategoryRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JwtService jwtService;

    public CategoryService(CategoryRepository categoryRepository,JwtService jwtService) {
        this.categoryRepository = categoryRepository;
        this.jwtService = jwtService;
    }

    public void create(CategoryRequest categoryRequest) throws BadRequestException {
        String token = jwtService.getJWT();
        jwtService.validateJWT(token);
        if(categoryRepository.existsByName(categoryRequest.name())){
            throw new BadRequestException("이미 존재하는 카테고리명입니다.");
        }
        categoryRepository.save(mapCategoryRequsetToCategory(categoryRequest));
    }

    public void update(CategoryRequest categoryRequest) {
        String token = jwtService.getJWT();
        jwtService.validateJWT(token);
        categoryRepository.save(mapCategoryRequsetToCategory(categoryRequest));
    }

    public void deleteById(Long id) {
        String token = jwtService.getJWT();
        jwtService.validateJWT(token);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 카테고리가 존재하지 않습니다."));
        List<Menu> menuList = category.getMenus();

        Category defaultCategory = categoryRepository.save(new Category("기본카테고리"));

        for (Menu menu : menuList) {
            menu.setCategory(defaultCategory);
        }
        categoryRepository.deleteById(id);
    }

    public List<CategoryResponse> getCategotyList() {
        String token = jwtService.getJWT();
        jwtService.validateJWT(token);
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapCategoryToCategoryResponse)
                .collect(Collectors.toList());
    }
    public Category mapCategoryRequsetToCategory(CategoryRequest categoryRequest) {
        return new Category(null, categoryRequest.name(), categoryRequest.description(),categoryRequest.color(),categoryRequest.imageUrl(), new LinkedList<Menu>());
    }

    public CategoryResponse mapCategoryToCategoryResponse(Category category){
        return new CategoryResponse(category.getId(),category.getName(),category.getDescription(), category.getColor(),category.getImageUrl());
    }
}
