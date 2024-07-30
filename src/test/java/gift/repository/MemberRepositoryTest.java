package gift.repository;

import gift.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("멤버 레포지토리 단위테스트")
class MemberRepositoryTest {

    private static final String EMAIL = "zzoe2346@git.com";
    private static final String PASSWORD = "12345678";
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일과 비밀번호로 멤버 찾기(for Login)")
    void findMemberByEmailAndPassword() {
        //Given
        Member member = new Member(EMAIL, PASSWORD);
        memberRepository.save(member);

        //When
        Optional<Member> foundMember = memberRepository.findByEmailAndPassword(EMAIL, PASSWORD);

        //Then
        assertThat(foundMember).isPresent()
                .hasValueSatisfying(m -> {
                    assertThat(m.getEmail()).isEqualTo(EMAIL);
                    assertThat(m.getId()).isPositive();
                });
    }

    @Test
    @DisplayName("이메일로 멤버 찾기")
    void findByEmail() {
        //Given
        Member member = new Member(EMAIL, PASSWORD);
        Long savedMemberId = memberRepository.save(member).getId();

        //When
        Optional<Member> foundMember = memberRepository.findByEmail(EMAIL);

        //Then
        assertThat(foundMember).isPresent()
                .hasValueSatisfying(m ->
                        assertThat(m.getId()).isEqualTo(savedMemberId)
                );
    }

    @Nested
    @DisplayName("멤버 엔티티 테스트")
    class EntityTest {
        @Test
        @DisplayName("이메일 중복 예외처리")
        void emailDuplicate() {
            Member member = new Member("a@naver.com", "1234");
            memberRepository.save(member);

            Member sameEmailMember = new Member("a@naver.com", "1");

            assertThatThrownBy(() -> memberRepository.save(sameEmailMember))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
    }
}
