package gift.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.dto.MemberDTO;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional(readOnly = true)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional
    void testSaveMember() {
        MemberDTO memberDTO = new MemberDTO("kbm", "kbm@kbm.com", "kbm");
        Member savedMember = memberService.saveMember(memberDTO);

        assertAll(
            () -> assertNotNull(savedMember.getId()),
            () -> assertEquals("kbm", savedMember.getName()),
            () -> assertEquals("kbm@kbm.com", savedMember.getEmail()),
            () -> assertEquals("kbm", savedMember.getPassword()),
            () -> assertEquals("user", savedMember.getRole())
        );
    }

    @Test
    void testFindMemberByEmail() {
        Member member = new Member(null, "kbm", "kbm@kbm.com", "kbm", "user");
        memberRepository.save(member);

        Member foundMember = memberService.findMemberByEmail("kbm@kbm.com");

        assertAll(
            () -> assertNotNull(foundMember),
            () -> assertEquals("kbm", foundMember.getName()),
            () -> assertEquals("kbm@kbm.com", foundMember.getEmail()),
            () -> assertEquals("kbm", foundMember.getPassword()),
            () -> assertEquals("user", foundMember.getRole())
        );

    }
}