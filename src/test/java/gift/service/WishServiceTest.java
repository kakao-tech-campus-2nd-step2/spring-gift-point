package gift.service;

import gift.dto.request.WishRequest;
import gift.dto.response.WishProductResponse;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.WishAlreadyExistsException;
import gift.exception.WishNotFoundException;
import gift.repository.WishRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("위시리스트 서비스 단위테스트")
class WishServiceTest {

    @Mock
    private WishRepository wishRepository;
    @Mock
    private ProductService productService;
    @Mock
    private MemberService memberService;
    @InjectMocks
    private WishService wishService;

    @Test
    @DisplayName("멤버의 모든 위시리스트 상품 조회")
    void getWishProductsByMemberId() {
        //Given
        Long memberId = 1L;
        Member member = new Member("test@email.com", "password");
        Product product = new Product("name", 1000, "imageUrl", null, List.of(new Option("option1", 100)));
        Wish wish = new Wish(member, 1, product);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Wish> wishPage = new PageImpl<>(List.of(wish), pageable, 1);

        when(memberService.getMember(memberId)).thenReturn(member);
        when(wishRepository.findAllByMember(member, pageable)).thenReturn(wishPage);

        //When
        Page<WishProductResponse> responsePage = wishService.getWishProductResponses(memberId, pageable);

        //Then
        assertThat(responsePage.getContent()).hasSize(1);
        assertThat(responsePage.getContent().get(0))
                .extracting("productName", "productAmount")
                .containsExactly("name", 1);
    }

    @Nested
    @DisplayName("상품을 위시리스트에 추가")
    class AddProductToWishList {

        @Test
        @DisplayName("성공")
        void success() {
            //Given
            Long memberId = 1L;
            WishRequest request = new WishRequest(1L, 1);
            Member member = new Member("test@email.com", "password");
            Product product = new Product("name", 1000, "imageUrl", null, List.of(new Option("option1", 100)));

            when(memberService.getMember(memberId)).thenReturn(member);
            when(productService.getProduct(request.productId())).thenReturn(product);
            when(wishRepository.existsByMemberAndProduct(member, product)).thenReturn(false);

            //When
            wishService.addProductToWish(memberId, request);

            //Then
            verify(wishRepository).save(any(Wish.class));
        }

        @Test
        @DisplayName("실패 - 이미 존재하는 위시")
        void fail() {
            // Given
            Long memberId = 1L;
            WishRequest request = new WishRequest(1L, 1);
            Member member = new Member("test@email.com", "password");
            Product product = new Product("name", 1000, "imageUrl", null, List.of(new Option("option1", 100)));

            when(memberService.getMember(memberId)).thenReturn(member);
            when(productService.getProduct(request.productId())).thenReturn(product);
            when(wishRepository.existsByMemberAndProduct(member, product)).thenReturn(true);

            //  When Then
            assertThatThrownBy(() -> wishService.addProductToWish(memberId, request))
                    .isInstanceOf(WishAlreadyExistsException.class);
        }
    }

    @Nested
    @DisplayName("위시리스트에서 상품 삭제")
    class DeleteProductInWishList {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            Long memberId = 1L;
            Long productId = 1L;
            Member member = new Member("test@email.com", "password");
            Product product = new Product("name", 1000, "imageUrl", null, List.of(new Option("option1", 100)));
            Wish wish = new Wish(member, 1, product);

            when(memberService.getMember(memberId)).thenReturn(member);
            when(productService.getProduct(productId)).thenReturn(product);
            when(wishRepository.findByMemberAndProduct(member, product)).thenReturn(Optional.of(wish));

            //When
            wishService.findAndDeleteProductInWish(memberId, productId);

            //Then
            verify(wishRepository).delete(wish);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 위시")
        void fail() {
            //Given
            Long memberId = 1L;
            Long productId = 1L;
            Member member = new Member("test@email.com", "password");
            Product product = new Product("name", 1000, "imageUrl", null, List.of(new Option("option1", 100)));

            when(memberService.getMember(memberId)).thenReturn(member);
            when(productService.getProduct(productId)).thenReturn(product);
            when(wishRepository.findByMemberAndProduct(member, product)).thenReturn(Optional.empty());

            //When Then
            assertThatThrownBy(() -> wishService.findAndDeleteProductInWish(memberId, productId))
                    .isInstanceOf(WishNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("위시리스트 상품 수량 업데이트")
    class UpdateWishProductAmount {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            Long memberId = 1L;
            WishRequest request = new WishRequest(1L, 10);
            Member member = new Member("test@email.com", "password");
            Product product = new Product("name", 1000, "imageUrl", null, List.of(new Option("option1", 100)));
            Wish wish = new Wish(member, 1, product);

            when(memberService.getMember(memberId)).thenReturn(member);
            when(productService.getProduct(request.productId())).thenReturn(product);
            when(wishRepository.findByMemberAndProduct(member, product)).thenReturn(Optional.of(wish));

            //When
            wishService.updateWishProductQuantity(memberId, request);

            //Then
            assertThat(wish.getQuantity()).isEqualTo(10);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 위시")
        void fail() {
            //Given
            Long memberId = 1L;
            WishRequest request = new WishRequest(1L, 10);
            Member member = new Member("test@email.com", "password");
            Product product = new Product("name", 1000, "imageUrl", null, List.of(new Option("option1", 100)));

            when(memberService.getMember(memberId)).thenReturn(member);
            when(productService.getProduct(request.productId())).thenReturn(product);
            when(wishRepository.findByMemberAndProduct(member, product)).thenReturn(Optional.empty());

            //When Then
            assertThatThrownBy(() -> wishService.updateWishProductQuantity(memberId, request))
                    .isInstanceOf(WishNotFoundException.class);
        }
    }
}
