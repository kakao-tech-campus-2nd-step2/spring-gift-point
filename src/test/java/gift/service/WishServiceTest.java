package gift.service;

import gift.domain.*;
import gift.dto.response.WishResponseDto;
import gift.exception.customException.EntityNotFoundException;
import gift.exception.customException.ForbiddenException;
import gift.repository.member.MemberRepository;
import gift.repository.product.ProductRepository;
import gift.repository.wish.WishRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static gift.exception.exceptionMessage.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @InjectMocks
    private WishService wishService;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("WISH 저장 시 상품 NOT FOUND EXCEPTION 테스트")
    void 위시_저장_상품_NOT_FOUND_EXCEPTION_테스트(){
        //given
        Long nullProductId = 2L;

        String email = "abc@pusan.ac.kr";

        given(productRepository.findById(nullProductId)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> wishService.addWish(nullProductId, email, 100))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(PRODUCT_NOT_FOUND)
        );
    }

    @Test
    @DisplayName("WISH 저장 시 멤버 NOT FOUND EXCEPTION 테스트")
    void 위시_저장_멤버_NOT_FOUND_EXCEPTION_테스트(){
        //given
        Long productId = 1L;
        Category category = new Category("상품권", "#0000", "abc.png");
        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        String nullEmail = "abcd@pusan.ac.kr";

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(memberRepository.findMemberByEmail(nullEmail)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> wishService.addWish(productId, nullEmail, 100))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(NOT_EXISTS_MEMBER)
        );
    }
    @Test
    @DisplayName("WISH 저장 테스트")
    void 위시_저장_테스트() throws Exception{
        //given
        Long productId = 1L;
        Category category = new Category("상품권", "#0000", "abc.png");
        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        String email = "abc@pusan.ac.kr";
        Member member = new Member.Builder()
                .email(email)
                .password("abc")
                .build();

        Wish wish = new Wish.Builder()
                .member(member)
                .product(product)
                .count(100)
                .build();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(memberRepository.findMemberByEmail(email)).willReturn(Optional.of(member));
        given(wishRepository.save(any(Wish.class))).willReturn(wish);

        //when
        WishResponseDto wishResponseDto = wishService.addWish(productId, email, 100);

        //then
        assertAll(
                () -> assertThat(wishResponseDto.count()).isEqualTo(100),
                () -> assertThat(wishResponseDto.productId()).isEqualTo(1L)
        );
    }
    @Test
    @DisplayName("WISH 수정 시 위시 NOT FOUND EXCEPTION 테스트")
    void 위시_수정_위시_NOT_FOUND_EXCEPTION_테스트(){
        //given
        Long inValidId = 2L;

        String validEmail = "abc@pusan.ac.kr";

        given(wishRepository.findById(inValidId)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> wishService.editWish(inValidId, validEmail, 100))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(WISH_NOT_FOUND)
        );
    }

    @Test
    @DisplayName("WISH 수정 시 권한 EXCEPTION 테스트")
    void 위시_수정_권한_EXCEPTION_테스트(){
        //given
        Long validId = 1L;
        String inValidEmail = "abcd@pusan.ac.kr";

        given(wishRepository.findById(validId)).willReturn(Optional.of(new Wish()));
        given(wishRepository.findWishByIdAndMemberEmail(validId, inValidEmail)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> wishService.editWish(validId, inValidEmail, 100))
                        .isInstanceOf(ForbiddenException.class)
        );
    }

    @Test
    @DisplayName("WISH 정상 수정 테스트")
    void 위시_정상_수정_테스트() throws Exception{
        //given
        Long validId = 1L;

        String validEmail = "abc@pusan.ac.kr";

        Category category = new Category("상품권", "#0000", "abc.png");

        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        Member member = new Member.Builder()
                .email(validEmail)
                .password("abc")
                .build();

        Wish wish = new Wish.Builder()
                .member(member)
                .product(product)
                .count(100)
                .build();

        given(wishRepository.findById(validId)).willReturn(Optional.of(new Wish()));
        given(wishRepository.findWishByIdAndMemberEmail(validId, validEmail)).willReturn(Optional.of(wish));

        //when
        WishResponseDto wishResponseDto = wishService.editWish(validId, validEmail, 1000);

        //then
        assertAll(
                () -> assertThat(wishResponseDto.count()).isEqualTo(1000),
                () -> assertThat(wishResponseDto.productId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("WISH 삭제 시 위시 NOT FOUND EXCEPTION 테스트")
    void 위시_삭제_위시_NOT_FOUND_EXCEPTION_테스트(){
        //given
        Long inValidId = 2L;

        String validEmail = "abc@pusan.ac.kr";

        given(wishRepository.findById(inValidId)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> wishService.deleteWish(inValidId, validEmail))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(WISH_NOT_FOUND)
        );
    }

    @Test
    @DisplayName("WISH 수정 시 권한 EXCEPTION 테스트")
    void 위시_삭제_권한_EXCEPTION_테스트(){
        //given
        Long validId = 1L;
        String inValidEmail = "abcd@pusan.ac.kr";

        given(wishRepository.findById(validId)).willReturn(Optional.of(new Wish()));
        given(wishRepository.findWishByIdAndMemberEmail(validId, inValidEmail)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> wishService.deleteWish(validId, inValidEmail))
                        .isInstanceOf(ForbiddenException.class)
        );
    }

    @Test
    @DisplayName("WISH 정상 삭제 테스트")
    void 위시_정상_삭제_테스트() throws Exception{
        //given
        Long validId = 1L;

        String validEmail = "abc@pusan.ac.kr";

        Category category = new Category("상품권", "#0000", "abc.png");

        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        Member member = new Member.Builder()
                .email(validEmail)
                .password("abc")
                .build();

        Wish wish = new Wish.Builder()
                .member(member)
                .product(product)
                .count(100)
                .build();

        given(wishRepository.findById(validId)).willReturn(Optional.of(new Wish()));
        given(wishRepository.findWishByIdAndMemberEmail(validId, validEmail)).willReturn(Optional.of(wish));

        //when
        WishResponseDto wishResponseDto = wishService.deleteWish(validId, validEmail);

        //then
        assertAll(
                () -> assertThat(wishResponseDto.count()).isEqualTo(100),
                () -> assertThat(wishResponseDto.productId()).isEqualTo(1L),
                () -> verify(wishRepository, times(1)).delete(any(Wish.class))
        );
    }

    @Test
    @DisplayName("WISH 전체 조회 테스트")
    void 위시_전체_조회_테스트() throws Exception{
        //given
        Category category = new Category("상품권", "#0000", "abc.png");
        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        Product product2 = new Product.Builder()
                .name("테스트 상품2")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        Member member = new Member.Builder()
                .email("abc@pusan.ac.kr")
                .password("abc")
                .build();

        Wish wish1 = new Wish.Builder()
                .member(member)
                .product(product)
                .count(100)
                .build();

        Wish wish2 = new Wish.Builder()
                .member(member)
                .product(product2)
                .count(100)
                .build();

        List<Wish> wishes = Arrays.asList(wish1, wish2);
        given(wishRepository.findWishByByMemberEmail(member.getEmail())).willReturn(wishes);

        //when
        List<WishResponseDto> wishDtos = wishService.findAllWish(member.getEmail());

        //then
        assertAll(
                () -> assertThat(wishDtos.size()).isEqualTo(2),
                () -> assertThat(wishDtos.get(0).productId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("WISH 페이징 조회 테스트")
    void 위시_페이징_조회_테스트(){
        //given
        Member member = new Member.Builder()
                .email("abc@pusan.ac.kr")
                .password("abc")
                .build();

        List<Wish> wishes = new ArrayList<>();

        Category category = new Category("상품권", "#0000", "abc.png");
        for(int i=0; i<20; i++){
            Product product = new Product.Builder()
                    .name("테스트" + i)
                    .price(i)
                    .imageUrl("abc.png")
                    .category(category)
                    .build();

            Wish wish = new Wish.Builder()
                    .member(member)
                    .product(product)
                    .count(i)
                    .build();

            wishes.add(wish);
        }

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "count"));

        given(wishRepository.findWishesByMemberEmail(member.getEmail(), pageRequest)).willReturn(new PageImpl<>(wishes.subList(15, 20).reversed(), pageRequest, wishes.size()));

        //when
        Page<WishResponseDto> wishDtos = wishService.findWishesPaging(member.getEmail(), pageRequest);

        //then
        assertAll(
                () -> assertThat(wishDtos.getSize()).isEqualTo(5),
                () -> assertThat(wishDtos.getContent().get(0).count()).isEqualTo(19)
        );
    }

}