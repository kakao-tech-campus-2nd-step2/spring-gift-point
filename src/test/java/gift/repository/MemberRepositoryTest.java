package gift.repository;

import gift.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository members;

    @DisplayName("member 저장")
    @Test
    void save(){
        Member expected = new Member("test.gamil.com", "test1234");
        Member actual = members.save(expected);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("email로 객체 찾아 반환")
    @Test
    void getMemberByEmail(){
        Member expected = new Member("test.gamil.com", "test1234");
        members.save(expected);

        Member actual = members.findByEmail("test.gamil.com").orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }
}
