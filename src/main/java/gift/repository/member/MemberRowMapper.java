package gift.repository.member;

import gift.model.member.Member;
import gift.model.member.Provider;
import gift.model.member.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Member(
            resultSet.getLong("id"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            Role.valueOf(resultSet.getString("role")),
            Provider.valueOf(resultSet.getString("provider"))
        );
    }

}
