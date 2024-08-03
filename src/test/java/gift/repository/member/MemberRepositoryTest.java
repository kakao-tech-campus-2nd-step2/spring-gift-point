//package gift.repository.member;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import gift.entity.MemberEntity;
//import gift.repository.MemberRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//@DataJpaTest
//public class MemberRepositoryTest {
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("멤버 생성 테스트")
//    public void memberCreateTest() {
//        MemberEntity member = new MemberEntity();
//        member.setEmail("test@example.com");
//        member.setPassword("password");
//        member.setDelete(false);
//        memberRepository.save(member);
//
//        boolean exists = memberRepository.existsByEmailAndPasswordAndDeleteFalse("test@example.com",
//            "password");
//
//        assertThat(exists).isTrue();
//    }
//
//    @Test
//    @DisplayName("멤버 삭제 테스트")
//    public void memberDeleteTest() {
//        MemberEntity member = new MemberEntity();
//        member.setEmail("test@example.com");
//        member.setPassword("password");
//        member.setDelete(false);
//        memberRepository.save(member);
//
//        MemberEntity foundMember = memberRepository.findByEmailAndPasswordAndDeleteFalse(
//            "test@example.com", "password");
//
//        assertThat(foundMember).isNotNull();
//        assertThat(foundMember.getEmail()).isEqualTo("test@example.com");
//        assertThat(foundMember.getPassword()).isEqualTo("password");
//    }
//
//    @Test
//    public void shouldNotFindMemberByEmailAndPasswordWhenDeleted() {
//        MemberEntity member = new MemberEntity();
//        member.setEmail("test@example.com");
//        member.setPassword("password");
//        member.setDelete(true);
//        memberRepository.save(member);
//
//        MemberEntity foundMember = memberRepository.findByEmailAndPasswordAndDeleteFalse(
//            "test@example.com", "password");
//
//        assertThat(foundMember).isNull();
//    }
//}
