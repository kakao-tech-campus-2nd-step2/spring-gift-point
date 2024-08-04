package gift.ServiceTest.FakeRepository;

import gift.Model.Entity.Category;
import gift.Model.Value.Name;
import gift.Repository.CategoryRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.function.Function;


public class FakeCategoryRepository implements CategoryRepository {
    private final Map<Long, Category> categoryMap = new HashMap<>();
    private Long id = 1L;

    public FakeCategoryRepository(){
        categoryMap.put(id++, new Category("카테고리 test", "#123456", "imageUrl", "카테고리 설명"));
    }

    @Override
    public Category save(Category category){
        categoryMap.put(id++, category);
        return category;
    }

    @Override
    public List<Category> findAll(){
        return new ArrayList<>(categoryMap.values());
    }

    @Override
    public Optional<Category> findByName(Name name) {
        return categoryMap.values()
                .stream()
                .filter(it -> it.getName().getValue().equals(name.getValue()))
                .findFirst();
    }

    @Override
    public Optional<Category> findById(Long id){
        return Optional.ofNullable(categoryMap.get(id));
    }

    public void deleteById(Long id) {
        categoryMap.remove(id);
    }

    @Override
    public List<Category> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public <S extends Category> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void delete(Category entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Category> entities) {

    }

    @Override
    public void deleteAll() {

    }


    @Override
    public void flush() {

    }

    @Override
    public <S extends Category> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Category> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Category> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Category getOne(Long aLong) {
        return null;
    }

    @Override
    public Category getById(Long aLong) {
        return null;
    }

    @Override
    public Category getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Category> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Category> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Category> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Category> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Category> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Category> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Category, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Category> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return null;
    }
}
