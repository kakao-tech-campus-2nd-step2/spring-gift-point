package gift.repository;

import gift.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testCreateAndFindMemberByEmail() {
        // 중복되지 않는 이메일을 사용하여 회원 생성 및 저장
        Member member = new Member("Test@example.com", "password");
        memberRepository.save(member);

        // 이메일로 회원 찾기
        Optional<Member> found = memberRepository.findByEmail("Test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("Test@example.com");
        assertThat(found.get().getPassword()).isEqualTo("password");
    }

    @Test
    public void testFindMemberById() {
        // 중복되지 않는 이메일을 사용하여 회원 생성 및 저장
        Member member = new Member("Test@example.com", "password");
        memberRepository.save(member);

        // ID로 회원 찾기
        Optional<Member> found = memberRepository.findByEmail("Test@example.com");
        assertThat(found).isPresent();

        Optional<Member> foundById = memberRepository.findById(found.get().getId());
        assertThat(foundById).isPresent();
        assertThat(foundById.get().getEmail()).isEqualTo("Test@example.com");
        assertThat(foundById.get().getPassword()).isEqualTo("password");
    }

    @Test
    public void testDeleteMember() {
        // 중복되지 않는 이메일을 사용하여 회원 생성 및 저장
        Member member = new Member("Test@example.com", "password");
        memberRepository.save(member);

        // 이메일로 회원 찾기
        Optional<Member> found = memberRepository.findByEmail("Test@example.com");
        assertThat(found).isPresent();

        // 회원 삭제
        memberRepository.delete(found.get());

        // 삭제된 회원 확인
        Optional<Member> deleted = memberRepository.findById(found.get().getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    public void testSaveMemberWithDuplicateEmailShouldThrowException() {
        // 중복된 이메일로 회원 생성
        Member member1 = new Member("duplicate@example.com", "password1");
        memberRepository.save(member1);

        Member member2 = new Member("duplicate@example.com", "password2");

        // 중복된 이메일로 회원을 저장할 경우 예외 발생하는지 확인
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
