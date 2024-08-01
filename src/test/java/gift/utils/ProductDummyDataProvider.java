package gift.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class ProductDummyDataProvider {

    private final JdbcTemplate jdbcTemplate;

    public ProductDummyDataProvider(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void run(int quantity) {
        doRun(quantity);
    }

    private void doRun(int quantity) {
        String sql = "insert into product (name, price, category_id, created_at, created_by, updated_at, updated_by) values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, getBatchPreparedStatementSetter(quantity));
    }

    private static BatchPreparedStatementSetter getBatchPreparedStatementSetter(int quantity) {
        return new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, "product" + i);
                ps.setInt(2, 1000 * i);
                ps.setLong(3, 1L);
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                ps.setLong(5, 1L);
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ps.setLong(7, 1L);
            }

            @Override
            public int getBatchSize() {
                return quantity;
            }
        };
    }
}