package gift.repository.product;

import gift.model.product.Product;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJdbcTemplateRepository implements ProductRepository {

    private static final String SQL_INSERT = "INSERT INTO product(name, price, image_url) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, price, image_url FROM product WHERE id = ?";
    private static final String SQL_SELECT_ALL = "SELECT id, name, price, image_url FROM product";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM product WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
    private static final String SQL_SELECT_PAGING = "SELECT id, name, price, image_url FROM product LIMIT ? OFFSET ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM product";


    private final JdbcTemplate jdbcTemplate;

    public ProductJdbcTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Optional<Product> findById(Long id) {
        return null;
    }

    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }

    public List<Product> findPaging(int page, int size) {
        return null;
    }

    @Override
    public Product save(Product entity) {
        return null;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> findAllOrderByPrice(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> findByNameContaining(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> findByCategoryId(String searchValue, Pageable pageable) {
        return null;
    }

}
