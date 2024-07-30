package gift.repository.member;

import gift.domain.Member;
import gift.dto.request.MemberRequest;
import gift.exception.DuplicateMemberEmailException;
import gift.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static gift.domain.LoginType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(MemberService.class)  // MemberService를 컨텍스트에 추가
public class MemberSpringDataJpaRepositoryTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberSpringDataJpaRepository memberRepository;

    @BeforeEach
    public void setUp() {
        Member member = new Member("test@example.com", "password", NORMAL);
        memberRepository.save(member);
    }

    @Test
    public void testSaveMember() {
        Optional<Member> foundMember = memberRepository.findByEmailAndLoginType("test@example.com", NORMAL);
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testFindByEmail() {
        Optional<Member> foundMember = memberRepository.findByEmailAndLoginType("test@example.com", NORMAL);
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testSaveMember_이메일_중복() {
        MemberRequest memberRequest = new MemberRequest("test@example.com", "password");

        assertThatThrownBy(() -> memberService.register(memberRequest, NORMAL))
                .isInstanceOf(DuplicateMemberEmailException.class)
                .hasMessage("이미 등록된 이메일입니다.");
    }
}
