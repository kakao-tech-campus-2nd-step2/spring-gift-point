package gift.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import gift.model.Member;
import gift.model.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    Member member = new Member("aaa123@2a.com", "1234");

    @Test
    @DisplayName("Member insert 테스트")
    void insert() {
        assertThat(member.getId()).isNull();
        Member normalMember = memberRepository.save(member);
        assertThat(normalMember.getId()).isNotNull();
        assertThat(normalMember.getRole()).isEqualTo(Role.ROLE_USER);

        Member adminMember = new Member("bbb123@b.com", "1234", Role.ROLE_ADMIN);
        Member savedAdminMember = memberRepository.save(adminMember);
        assertThat(savedAdminMember.getRole()).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    @DisplayName("Member findByEmail 테스트")
    void getMemberByEmail() {
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findByEmail(savedMember.getEmail()).get();

        assertThat(findMember).isEqualTo(savedMember);
    }


}