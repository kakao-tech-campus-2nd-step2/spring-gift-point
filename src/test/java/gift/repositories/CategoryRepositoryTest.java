//package gift.repositories;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import gift.domain.Category;
//import jakarta.transaction.Transactional;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//@DataJpaTest
//@Transactional
//class CategoryRepositoryTest {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    private Category category;
//
//    @Test
//    @DisplayName("카테고리 저장 테스트")
//    void testSaveCategory() {
//        Category category = new Category("Test Category", "Blue", "abcd", "Test");
//        Category savedCategory = categoryRepository.save(category);
//        assertNotNull(savedCategory.getId());
//        assertEquals("Test Category", savedCategory.getName());
//    }
//
//    @Test
//    @DisplayName("ID로 카테고리 조회 테스트")
//    void testFindById() {
//        Category category = new Category("Test Category", "Blue", "abcd", "Test");
//        categoryRepository.save(category);
//        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
//        assertTrue(foundCategory.isPresent());
//        assertEquals(category.getId(), foundCategory.get().getId());
//    }
//
//    @Test
//    @DisplayName("모든 카테고리 조회 테스트")
//    void testFindAll() {
//        Category category = new Category("Test Category", "Blue", "abcd", "Test");
//        categoryRepository.save(category);
//        List<Category> categories = categoryRepository.findAll();
//        assertNotNull(categories);
//        assertTrue(categories.iterator().hasNext());
//    }
//
//    @Test
//    @DisplayName("카테고리 삭제 테스트")
//    void testDeleteById() {
//        Category category = new Category("Test Category", "Blue", "abcd", "Test1");
//        categoryRepository.save(category);
//        categoryRepository.deleteById(category.getId());
//        Optional<Category> deletedCategory = categoryRepository.findById(category.getId());
//        assertFalse(deletedCategory.isPresent());
//    }
//}