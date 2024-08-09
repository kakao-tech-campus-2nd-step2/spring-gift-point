package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import gift.entity.Member;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void save() {
        Member expected = new Member("12345@12345.com", "1", "basic", "홍길동", "user", 0L);
        Member actual = memberRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("홍길동"),
                () -> assertThat(actual.getEmail()).isEqualTo("12345@12345.com"),
                () -> assertThat(actual.getPassword()).isEqualTo("1"),
                () -> assertThat(actual.getRole()).isEqualTo("user")
        );
    }

    @Test
    @DisplayName("모든 유저 불러오기")
    void findAll(){
        Member expected1 = memberRepository.save(new Member("12345@12345.com", "1", "basic", "홍길동", "user", 0L));
        Member expected2 = memberRepository.save(new Member("22345@12345.com", "2", "basic", "홍길동", "user", 0L));

        List<Member> actualList = memberRepository.findAll();

        assertThat(actualList).containsExactly(expected1, expected2);
    }

    @Test
    @DisplayName("조건에 맞는 유저 수 세기 (db 무결성 검증용)")
    void countByEmail(){
        memberRepository.save(new Member("12345@12345.com", "1", "basic", "홍길동", "user", 0L));
        memberRepository.save(new Member("22345@12345.com", "2", "basic", "홍길동", "user", 0L));
        assertThat(memberRepository.countByEmail("12345@12345.com")).isEqualTo(1);
    }

    @Test
    @DisplayName("이메일로 한명 찾기")
    void findOne(){
        memberRepository.save(new Member("12345@12345.com", "1", "basic", "홍길동", "user", 0L));

        assertThat(memberRepository.findByEmail("12345@12345.com")).isNotNull();
    }


}