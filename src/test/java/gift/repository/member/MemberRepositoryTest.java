package gift.repository.member;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    private Member member1;
    private Member member2;
    private Member member3;

    @BeforeEach
    void setup() {
        member1 = new Member.Builder()
                .email("test1@pusan.ac.kr")
                .password("abc")
                .build();

        member2 = new Member.Builder()
                .email("test2@pusan.ac.kr")
                .password("abc")
                .build();

        member3 = new Member.Builder()
                .email("test3@pusan.ac.kr")
                .password("abc")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }

    @Test
    @DisplayName("저장 테스트")
    void 저장_테스트(){
        //given
        Member member = new Member.Builder()
                .email("test@pusan.ac.kr")
                .password("abc")
                .build();
        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertAll(
                () -> assertThat(savedMember.getId()).isNotNull(),
                () -> assertThat(savedMember.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(savedMember.getPassword()).isEqualTo(member.getPassword())
        );
    }

    @Test
    @DisplayName("단건 조회")
    void 단건_조회_테스트(){
        //given
        //when
        Member findMember = memberRepository.findById(member1.getId()).get();

        //then
        assertAll(
                () -> assertThat(findMember.getId()).isNotNull(),
                () -> assertThat(findMember.getEmail()).isEqualTo(member1.getEmail()),
                () -> assertThat(findMember.getPassword()).isEqualTo(member1.getPassword())
        );
    }

    @Test
    @DisplayName("전체 조회")
    void 전체_조회_테스트(){
        //given

        //when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이메일 조회")
    void 이메일_조회_테스트(){
        //given

        //when
        Member findMember = memberRepository.findMemberByEmail(member1.getEmail()).get();

        //then
        assertAll(
                () -> assertThat(findMember.getId()).isEqualTo(member1.getId()),
                () -> assertThat(findMember.getEmail()).isEqualTo(member1.getEmail()),
                () -> assertThat(findMember.getPassword()).isEqualTo(member1.getPassword())
        );
    }

    @Test
    @DisplayName("이메일 패스워드 조회")
    void 이메일_패스워드_조회_테스트(){
        //given

        //when
        Member findMember = memberRepository.findMemberByEmailAndPassword(member1.getEmail(), member1.getPassword()).get();

        //then
        assertAll(
                () -> assertThat(findMember.getId()).isEqualTo(member1.getId()),
                () -> assertThat(findMember.getEmail()).isEqualTo(member1.getEmail()),
                () -> assertThat(findMember.getPassword()).isEqualTo(member1.getPassword())
        );
    }

    @Test
    @DisplayName("일대다 연관관계 지연로딩 테스트")
    void 연관관계_지연로딩_테스트(){
        //given
        Product product1 = new Product.Builder()
                .name("테스트1")
                .price(123)
                .imageUrl("abc.png")
                .build();

        Product product2 = new Product.Builder()
                .name("테스트2")
                .price(123)
                .imageUrl("abc.png")
                .build();

        entityManager.persist(product1);
        entityManager.persist(product2);

        Wish wish1 = new Wish.Builder()
                .member(member1)
                .product(product1)
                .count(3)
                .build();

        Wish wish2 = new Wish.Builder()
                .member(member1)
                .product(product2)
                .count(3)
                .build();

        wish1.addMember(member1);
        wish1.addProduct(product1);

        wish2.addMember(member1);
        wish2.addProduct(product2);

        entityManager.persist(wish1);
        entityManager.persist(wish2);
        entityManager.flush();
        entityManager.clear();

        //when

        //지연 로딩 이므로 연관관계 조회 안함
        memberRepository.findMemberByEmail(member1.getEmail());
        entityManager.clear();

        //fetch join 을 사용했기 때문에 연관관계 한번에 조회
        Member findMember = memberRepository.findMemberWithRelation(member1.getEmail()).get();

        //then
        assertAll(
                () -> assertThat(findMember.getId()).isEqualTo(member1.getId()),
                () -> assertThat(findMember.getEmail()).isEqualTo(member1.getEmail()),
                () -> assertThat(findMember.getPassword()).isEqualTo(member1.getPassword()),
                () -> assertThat(findMember.getWishList().size()).isEqualTo(2)
        );
    }

}