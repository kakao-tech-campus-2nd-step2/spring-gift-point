package gift.repository;

import gift.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/sql/truncateIdentity.sql")
class MemberJpaDaoTest {

    @Autowired
    private MemberJpaDao memberJpaDao;

    @Test
    @DisplayName("회원가입 테스트")
    void save() {
        Member member = memberJpaDao.save(new Member("sgoh", "pass"));
        Assertions.assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("이메일로 회원 조회 테스트")
    void findByEmail() {
        Member member1 = memberJpaDao.save(new Member("sgoh", "pass"));
        Member member2 = memberJpaDao.findByEmail("sgoh").get();

        Assertions.assertThat(member1).isEqualTo(member2);
    }
}