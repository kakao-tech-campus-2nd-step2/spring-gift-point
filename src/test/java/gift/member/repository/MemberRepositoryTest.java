package gift.member.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.member.MemberFixture;
import gift.member.entity.Member;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("회원 리파지토리 테스트")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        // 임의의 회원 3명
        List<Member> members = List.of(
                MemberFixture.createMember("test1@test.com", "1234"),
                MemberFixture.createMember("test2@test.com", "1234"),
                MemberFixture.createMember("test3@test.com", "1234")
        );

        memberRepository.saveAll(members);
    }

    @Test
    @DisplayName("회원 생성")
    void addMember() {
        //given
        Member member = MemberFixture.createMember("abc123@test.com", "1234");

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertAll(
                () -> assertNotNull(savedMember.getId()),
                () -> assertEquals(member.getEmail(), savedMember.getEmail()),
                () -> assertEquals(member.getPassword(), savedMember.getPassword())
        );
    }

    @Test
    @DisplayName("회원 조회")
    void findMember() {

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertAll(
                () -> assertEquals(3, members.size()),
                () -> assertEquals("test1@test.com", members.get(0).getEmail()),
                () -> assertEquals("test2@test.com", members.get(1).getEmail()),
                () -> assertEquals("test3@test.com", members.get(2).getEmail())
        );

        // 존재하지 않는 회원 조회
        assertFalse(memberRepository.existsByEmail("noExists@test.com"));
    }

    @Test
    @DisplayName("회원 수정")
    void updateMember() {
        // given
        Member member = memberRepository.findByEmail("test1@test.com").orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // when
        member.update(MemberFixture.createMemberReqDto("newEmail@test.com", "4321"));

        // then
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        assertAll(
                () -> assertEquals("newEmail@test.com", updatedMember.getEmail()),
                () -> assertEquals("4321", updatedMember.getPassword())
        );
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMember() {
        // when
        memberRepository.deleteById(1L);

        // then
        assertThrows(Exception.class,
                () -> memberRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."))
        );
    }
}
