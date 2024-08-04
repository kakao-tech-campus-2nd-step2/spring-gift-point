package gift.repository.wish;

import gift.model.wish.Wish;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class WishJdbcRepository {

    private static final String SQL_INSERT = "INSERT INTO wish (member_id, product_id, count) VALUES (?, ?, ?)";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM wish WHERE id = ?";
    private static final String SQL_UPDATE_COUNT = "UPDATE wish SET count = ? WHERE member_id = ? AND product_id = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM wish WHERE member_id = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM wish WHERE id = ?";
    private static final String SQL_SELECT_BY_PRODUCT_ID_AND_MEMBER_ID = "SELECT * FROM wish WHERE product_id = ? AND member_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Wish> wishRowMapper = new WishRowMapper();

    public WishJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Wish wish) {
        jdbcTemplate.update(SQL_INSERT, wish.getMember().getId(), wish.getProduct().getId(),
            wish.getCount());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }

    public void updateCount(Long memberId, Long productId, Long count) {
        jdbcTemplate.update(SQL_UPDATE_COUNT, count, memberId, productId);
    }

    public List<Wish> findAll(Long memberId) {
        return jdbcTemplate.query(SQL_SELECT_ALL, wishRowMapper, memberId);
    }

    public Optional<Wish> findById(Long id) {
        try {
            Wish wish = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, wishRowMapper, id);
            return Optional.of(wish);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Wish> findByProductIdAndUserId(Long productId, Long memberId) {
        try {
            Wish wish = jdbcTemplate.queryForObject(SQL_SELECT_BY_PRODUCT_ID_AND_MEMBER_ID,
                wishRowMapper, productId, memberId);
            return Optional.of(wish);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
