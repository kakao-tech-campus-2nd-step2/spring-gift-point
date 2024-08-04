package gift.RepositoryTest;

import gift.domain.MemberDomain.Member;
import gift.domain.WishListDomain.WishList;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        Member member1 = new Member("member1", "김민지","password1",new LinkedList<WishList>());
        Member member2 = new Member("member2", "김민서","password2",new LinkedList<WishList>());

        memberRepository.save(member1);
        memberRepository.save(member2);
    }

    @Test
    @DisplayName("아이디로 멤버 찾기")
    void testFindById() {
        //when
        Optional<Member> foundMember = memberRepository.findById("member1");

        //then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getPassword()).isEqualTo("password1");
    }

    @Test
    @DisplayName("멤버 FindById 테스트")
    void testFindById_NotFound() {
        //when
        Optional<Member> foundMember = memberRepository.findById("nonexistent_member");
        //then
        assertThat(foundMember).isNotPresent();
    }
    
    @Test
    @DisplayName("멤버 FindById 테스트")
    void testDeleteMember() {
        //when
        memberRepository.deleteById("member2");
        Optional<Member> deletedMember = memberRepository.findById("member2");
        //then
        assertThat(deletedMember).isNotPresent();
    }
}
