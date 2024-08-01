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
public class MemberDummyDataProvider {

    private final JdbcTemplate jdbcTemplate;

    public MemberDummyDataProvider(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void run(int quantity) {
        doRun(quantity);
    }

    private void doRun(int quantity) {
        String sql = "insert into member (name, email, password, created_at, updated_at) values (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, getBatchPreparedStatementSetter(quantity));
    }

    private static BatchPreparedStatementSetter getBatchPreparedStatementSetter(int quantity) {
        return new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, "member" + i);
                ps.setString(2, "member" + i + "@gmail.com");
                ps.setString(3, "member" + i + "0");
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return quantity;
            }
        };
    }
}