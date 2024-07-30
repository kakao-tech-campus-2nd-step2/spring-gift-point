package gift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        initDB();
    }

    void initDB() {
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS products(id LONG, name VARCHAR(255), price NUMERIC, imageUrl VARCHAR(255))");
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS members(memberId LONG, email VARCHAR(255), password VARCHAR(255), role VARCHAR(32))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS wishes(memberId LONG, productId LONG)");
    }

}
