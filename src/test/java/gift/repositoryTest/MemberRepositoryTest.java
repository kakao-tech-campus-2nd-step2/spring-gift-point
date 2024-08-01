package gift.repositoryTest;

import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository members;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void save() {
        Member expected = new Member("wjdghtjd06@kakao.com", "1234");
        Member savedMember = members.save(expected);
        testEntityManager.clear();

        Member actual = members.findById(savedMember.getId())
            .orElseThrow();

        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        Assertions.assertThat(actual.getPassword()).isEqualTo(expected.getPassword());


        Assertions.assertThat(actual.getRole()).isEqualTo("user");
    }

    @Test
    void findByEmail() {
        Member expected = new Member("wjdghtjd06@kakao.com", "1234");
        members.save(expected);

        var actual = members.findByEmail(expected.getEmail());
        Assertions.assertThat(actual.get()).isNotNull().isEqualTo(expected);
    }

    @Test
    void findAllMembers() {
        Member member1 = new Member("wjdghtjd06@kakao.com", "1234");
        Member member2 = new Member("ghdrlfehd12@kakao.com", "1234");

        members.save(member1);
        members.save(member2);

        var actual = members.findAll();

        Assertions.assertThat(actual).hasSize(2);
        Assertions.assertThat(actual.get(0)).isEqualTo(member1);
        Assertions.assertThat(actual.get(1)).isEqualTo(member2);
    }

    @Test
    void updateMember() {
        Member member = new Member("wjdghtjd06@kakao.com", "1234");
        members.save(member);

        member.updateMember("ghdrlfehd1234@kakao.com", "1234");

        Assertions.assertThat(member.getEmail()).isEqualTo("ghdrlfehd1234@kakao.com");
        Assertions.assertThat(member.getPassword()).isEqualTo("1234");
    }

    @Test
    void deleteMember() {
        Member member = new Member("wjdghtjd06@kakao.com", "1234");
        members.save(member);

        members.delete(member);
        Assertions.assertThat(members.findById(member.getId())).isNotPresent();
    }
}
