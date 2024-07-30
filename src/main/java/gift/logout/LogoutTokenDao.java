package gift.logout;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LogoutTokenDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public LogoutTokenDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertToken(String token) {
        String sql = """
            INSERT INTO blacklist (token)
            VALUES (?)
            """;

        jdbcTemplate.update(sql, token);
    }

    public boolean findToken(String token) {
        String sql = """
            SELECT token
            FROM blacklist
            WHERE token = ?
            """;

        List<String> isToken = jdbcTemplate.query(
            sql,
            (rs, rowNum) -> rs.getString("token"),
            token
        );

        return isToken.isEmpty();
    }
}
