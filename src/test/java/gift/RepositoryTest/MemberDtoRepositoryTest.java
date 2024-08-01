
package gift.RepositoryTest;

import gift.Entity.Member;
import gift.Mapper.Mapper;
import gift.Model.MemberDto;
import gift.Repository.MemberJpaRepository;
import gift.Service.MemberService;
import gift.Service.OptionService;
import gift.Service.ProductService;
import gift.Service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class MemberDtoRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Mapper mapper;

    @BeforeEach
    void setUp() {
        // 필요한 서비스의 목 객체 생성
        ProductService productService = Mockito.mock(ProductService.class);
        MemberService memberService = Mockito.mock(MemberService.class);
        WishlistService wishListService = Mockito.mock(WishlistService.class);
        OptionService optionService = Mockito.mock(OptionService.class);

        // Mapper 인스턴스 수동 생성 및 주입
        mapper = new Mapper(productService, memberService, wishListService, null, optionService);
    }

    @Test
    public void testGetAllUsers() {
        MemberDto memberDto1 = new MemberDto(1L, "1234@naver.com", "1234", false);
        MemberDto memberDto2 = new MemberDto(2L, "5678@naver.com", "5678",false);

        Member member1 = mapper.memberDtoToEntity(memberDto1);
        Member member2 = mapper.memberDtoToEntity(memberDto2);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        assertThat(memberJpaRepository.findAll()).isNotEmpty();
    }

    @Test
    public void testGetUserById() {
        MemberDto memberDto1 = new MemberDto(1L, "1234@naver.com", "1234",false);
        Member member1 = mapper.memberDtoToEntity(memberDto1);
        memberJpaRepository.save(member1);

        Optional<Member> foundUserOptional = memberJpaRepository.findById(member1.getId());
        assertThat(foundUserOptional).isNotNull();

        foundUserOptional.ifPresent(foundUser -> {
            assertThat(foundUser.getEmail()).isEqualTo(member1.getEmail());
            assertThat(foundUser.getPassword()).isEqualTo(member1.getPassword());
        });

    }

    @Test
    public void testSaveUser() {
        MemberDto memberDto1 = new MemberDto(1L, "1234@naver.com", "1234", false);
        Member member1 = mapper.memberDtoToEntity(memberDto1);
        Member savedMember = Optional.of(memberJpaRepository.save(member1)).get();
        assertThat(savedMember.getEmail()).isEqualTo(member1.getEmail());
        assertThat(savedMember.getPassword()).isEqualTo(member1.getPassword());

    }

    @Test
    public void testDeleteUser() {
        MemberDto memberDto1 = new MemberDto(1L, "1234@naver.com", "1234", false);
        Member member1 = mapper.memberDtoToEntity(memberDto1);
        memberJpaRepository.save(member1);

        memberJpaRepository.delete(member1);

        assertThat(memberJpaRepository.findById(member1.getId())).isEmpty();
    }

    @Test
    public void testUpdateUser() {
        MemberDto memberDto1 = new MemberDto(1L, "1234@naver.com", "1234", false);
        Member member1 = mapper.memberDtoToEntity(memberDto1);
        memberJpaRepository.save(member1);
    }


}
