package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.member.Member;
import gift.model.member.Provider;
import gift.model.member.Role;
import gift.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장")
    void save() {
        // given
        Member member = new Member(1L, "member1@asd.com", "asd", "asd", Role.USER, Provider.ORIGIN);
        // when
        memberRepository.save(member);
        // then
        assertAll(
            () -> assertThat(memberRepository.findById(1L).get().getEmail()).isEqualTo(
                "member1@asd.com"),
            () -> assertThat(memberRepository.findById(1L).get().getPassword()).isEqualTo(
                "asd"),
            () -> assertThat(memberRepository.findById(1L).get().getName()).isEqualTo(
                "asd"),
            () -> assertThat(memberRepository.findById(1L).get().getRole()).isEqualTo(
                Role.USER)
        );
    }

    @Test
    @DisplayName("회원 삭제")
    void delete() {
        // given
        Member member = new Member(1L, "member1@asd.com", "asd", "asd", Role.USER, Provider.ORIGIN);
        memberRepository.save(member);
        // when
        memberRepository.deleteById(1L);
        // then
        assertThat(memberRepository.findById(1L)).isEmpty();
    }

    @Test
    @DisplayName("회원 이메일 조회")
    void getByEmail() {
        // given
        Member member = new Member(1L, "member1@asd.com", "asd", "asd", Role.USER, Provider.ORIGIN);
        memberRepository.save(member);
        // when
        Member findMember = memberRepository.findByEmail("member1@asd.com").get();
        // then
        assertAll(
            () -> assertThat(findMember.getEmail()).isEqualTo("member1@asd.com"),
            () -> assertThat(findMember.getPassword()).isEqualTo("asd"),
            () -> assertThat(findMember.getName()).isEqualTo("asd")
        );
    }

}
