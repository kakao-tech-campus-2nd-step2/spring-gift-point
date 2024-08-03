package gift.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.wish.WishDto;
import gift.product.model.Category;
import gift.product.model.Member;
import gift.product.model.Product;
import gift.product.model.Wish;
import gift.product.repository.AuthRepository;
import gift.product.repository.ProductRepository;
import gift.product.repository.WishRepository;
import gift.product.service.WishService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class WishServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    AuthRepository authRepository;

    @Mock
    WishRepository wishRepository;

    @InjectMocks
    WishService wishService;

    @Test
    void 위시리스트_항목_추가() {
        //given
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Member member = new Member(1L, "테스트회원이름", "test@test.com", "test");
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(authRepository.findById(any())).willReturn(Optional.of(member));
        given(wishRepository.existsByProductIdAndMemberId(product.getId(),
            member.getId())).willReturn(false);

        //when
        WishDto wishDto = new WishDto(product.getId());
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(member.getId());
        wishService.insertWish(wishDto, loginMemberIdDto);

        //then
        then(wishRepository).should().save(any());
    }

    @Test
    void 위시리스트_전체_조회_페이지_테스트() {
        //given
        int PAGE = 1;
        int SIZE = 4;
        String SORT = "product.name";
        String DIRECTION = "desc";
        Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.Direction.fromString(DIRECTION), SORT);
        Member member = new Member(1L, "테스트멤버이름", "test@test.com", "test");
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1000, "테스트주소", category);
        List<Wish> wishes = new ArrayList<>();
        wishes.add(new Wish(member, product));
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(member.getId());

        given(wishRepository.findAllByMemberId(pageable,
            loginMemberIdDto.id())).willReturn(new PageImpl<>(wishes));

        //when
        wishService.getWishAll(pageable, loginMemberIdDto);

        //then
        then(wishRepository).should().findAllByMemberId(pageable, loginMemberIdDto.id());
    }

    @Test
    void 실패_존재하지_않는_위시_항목_조회() {
        //given
        LoginMemberIdDto testMember = new LoginMemberIdDto(1L);
        given(wishRepository.findByIdAndMemberId(any(), any())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> wishService.getWish(-1L, testMember)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    void 실패_존재하지_않는_위시_항목_삭제() {
        //given
        LoginMemberIdDto testMember = new LoginMemberIdDto(1L);
        given(wishRepository.findByIdAndMemberId(any(), any())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> wishService.deleteWish(-1L, testMember)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    void 실패_존재하지_않는_회원_정보로_위시리스트_추가_시도() {
        //given
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(authRepository.findById(any())).willReturn(Optional.empty());

        WishDto wishDto = new WishDto(product.getId());
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(-1L);

        //when, then
        assertThatThrownBy(
            () -> wishService.insertWish(wishDto, loginMemberIdDto)).isInstanceOf(
            NoSuchElementException.class);
    }

    @Test
    void 실패_위시_리스트에_상품_중복_추가() {
        //given
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        WishDto wishDto = new WishDto(product.getId());
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(wishRepository.existsByProductIdAndMemberId(product.getId(),
            loginMemberIdDto.id())).willReturn(true);

        //when, then
        assertThatThrownBy(() -> wishService.insertWish(wishDto, loginMemberIdDto)).isInstanceOf(
            IllegalArgumentException.class);
    }
}
