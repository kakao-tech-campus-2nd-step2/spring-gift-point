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
public class CategoryDummyDataProvider {

    private final JdbcTemplate jdbcTemplate;

    public CategoryDummyDataProvider(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void run(int quantity) {
        doRun(quantity);
    }

    private void doRun(int quantity) {
        String sql = "insert into category (name, description, image_url, color, created_at, created_by, updated_at, updated_by) values (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, getBatchPreparedStatementSetter(quantity));
    }

    private static BatchPreparedStatementSetter getBatchPreparedStatementSetter(int quantity) {
        return new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, "Category" + i);
                ps.setString(2, "Description" + i);
                ps.setString(3, "https://via.placeholder.com/" + i);
                ps.setString(4, "#FF2829");
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                ps.setLong(6, 1L);
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                ps.setLong(8, 1L);
            }

            @Override
            public int getBatchSize() {
                return quantity;
            }
        };
    }
}