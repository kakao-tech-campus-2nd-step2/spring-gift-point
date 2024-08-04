package gift.repository.wish;

import gift.model.member.Member;
import gift.model.product.Product;
import gift.model.wish.Wish;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class WishRowMapper implements RowMapper<Wish> {

    @Override
    public Wish mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Wish(
            resultSet.getLong("id"),
            resultSet.getObject("member_id", Member.class),
            resultSet.getObject("product_id", Product.class),
            resultSet.getLong("count")
        );
    }
}
