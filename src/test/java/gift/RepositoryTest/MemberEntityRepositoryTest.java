package gift.RepositoryTest;

import gift.Model.Role;
import gift.Model.Entity.MemberEntity;
import gift.Repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class MemberEntityRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save(){
        MemberEntity expected = new MemberEntity("test","1234", Role.ADMIN);
        MemberEntity actual = memberRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void findByName() {
        String expectedEmail = "teeeest";
        String expectedPassword = "1234";
        Role expectedRole = Role.ADMIN;
        memberRepository.save(new MemberEntity(expectedEmail, expectedPassword, expectedRole));
        String actual = memberRepository.findByEmail(expectedEmail).get().getEmail();
        assertThat(actual).isEqualTo(expectedEmail);
    }
}
