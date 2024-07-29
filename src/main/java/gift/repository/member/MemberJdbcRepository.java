package gift.repository.member;

import gift.model.member.Member;
import gift.model.member.Role;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private static final String USER_INSERT = "INSERT INTO member (email, password, name, role, provider) VALUES (?, ?, ?,?,?)";
    private static final String USER_SELECT_BY_ID = "SELECT id, email, password, name, role FROM member WHERE id = ?";
    private static final String USER_SELECT_BY_EMAIL = "SELECT id, email, password, name, role FROM member WHERE email = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> userRowMapper = new MemberRowMapper();

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Member member) {
        jdbcTemplate.update(USER_INSERT, member.getEmail(), member.getPassword(), member.getName(),
            Role.USER.toString());
    }

    @Override
    public Optional<Member> findById(Long id) {
        try {
            Member member = jdbcTemplate.queryForObject(USER_SELECT_BY_ID,
                userRowMapper, id);
            return Optional.of(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Member save(Member member) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        try {
            Member member = jdbcTemplate.queryForObject(USER_SELECT_BY_EMAIL,
                userRowMapper, email);
            return Optional.of(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
