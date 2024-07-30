package gift.wish.service;

import gift.member.domain.*;
import gift.member.service.MemberService;
import gift.product.domain.ImageUrl;
import gift.product.domain.Product;
import gift.product.domain.ProductName;
import gift.product.domain.ProductPrice;
import gift.product.service.ProductService;
import gift.wish.domain.ProductCount;
import gift.wish.domain.Wish;
import gift.wish.dto.WishResponseDto;
import gift.wish.dto.WishServiceDto;
import gift.wish.exception.WishNotFoundException;
import gift.wish.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private WishService wishService;

    private Member member;
    private Product product;
    private Wish wishHasId;
    private Wish wishNoId;
    private WishServiceDto wishServiceDtoHasId;
    private WishServiceDto wishServiceDtoNoId;

    @BeforeEach
    void setUp() {
        member = new Member(1L, MemberType.USER, new Email("email"), new Password("password"), new Nickname("nickname"));
        product = new Product(1L, new ProductName("name"), new ProductPrice(10L), new ImageUrl("imageUrl"));
        wishHasId = new Wish(1L, member, product, new ProductCount(10L));
        wishNoId = new Wish(null, member, product, new ProductCount(10L));

        wishServiceDtoHasId = new WishServiceDto(1L, 1L, 1L, new ProductCount(10L));
        wishServiceDtoNoId = new WishServiceDto(null, 1L, 1L, new ProductCount(10L));
    }

    @Test
    @Description("getAllWishesByMember 테스트")
    void testGetAllWishesByMember() {
        // given
        given(wishRepository.findAllByMemberId(anyLong())).willReturn(List.of(wishHasId));

        // when
        List<WishResponseDto> wishResponseDtos = wishService.getAllWishesByMember(member);

        // then
        assertThat(wishResponseDtos).hasSize(1);

        // 리스트에서 첫 번째 WishResponseDto를 가져옴
        WishResponseDto wishResponseDto = wishResponseDtos.getFirst();

        assertThat(wishResponseDto.id()).isEqualTo(wishHasId.getId());
        assertThat(wishResponseDto.productId()).isEqualTo(wishHasId.getProduct().getId());
        assertThat(wishResponseDto.productName()).isEqualTo(wishHasId.getProduct().getName());
        assertThat(wishResponseDto.productCount()).isEqualTo(wishHasId.getProductCount());

        verify(wishRepository, times(1)).findAllByMemberId(anyLong());
    }

    @Test
    @Description("getWishById 테스트")
    void testGetWishById() {
        // given
        given(wishRepository.findById(anyLong())).willReturn(Optional.of(wishHasId));

        // when
        WishResponseDto wishResponseDto = wishService.getWishById(wishHasId.getId());

        // then
        assertThat(wishResponseDto.id()).isEqualTo(wishHasId.getId());
        assertThat(wishResponseDto.productId()).isEqualTo(wishHasId.getProduct().getId());
        assertThat(wishResponseDto.productName()).isEqualTo(wishHasId.getProduct().getName());
        assertThat(wishResponseDto.productCount()).isEqualTo(wishHasId.getProductCount());

        verify(wishRepository, times(1)).findById(anyLong());
    }

    @Test
    @Description("getWishById_NotFound 테스트")
    void testGetWishById_NotFound() {
        // given
        given(wishRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> wishService.getWishById(1L))
                .isInstanceOf(WishNotFoundException.class);

        verify(wishRepository, times(1)).findById(anyLong());
    }

    @Test
    @Description("createWish_NewWish 테스트")
    void testCreateWish_NewWish() {
        // given
        given(wishRepository.findByMemberIdAndProductId(anyLong(), anyLong())).willReturn(Optional.empty());
        given(wishRepository.save(any(Wish.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // when
        Wish createdWish = wishService.createWish(wishServiceDtoNoId);

        // then
        assertThat(createdWish).isEqualTo(wishNoId);

        verify(wishRepository, times(1)).findByMemberIdAndProductId(anyLong(), anyLong());
        verify(wishRepository, times(1)).save(any(Wish.class));
    }

    @Test
    @Description("createWish_ExistingWish 테스트")
    void testCreateWish_ExistingWish() {
        // given
        given(wishRepository.findByMemberIdAndProductId(anyLong(), anyLong())).willReturn(Optional.of(wishHasId));
        given(wishRepository.save(any(Wish.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // when
        Wish updatedWish = wishService.createWish(wishServiceDtoHasId);

        // then
        assertThat(updatedWish).isEqualTo(wishHasId);
        assertThat(updatedWish.getProductCount().getProductCountValue()).isEqualTo(20L);

        verify(wishRepository, times(1)).findByMemberIdAndProductId(anyLong(), anyLong());
        verify(wishRepository, times(1)).save(any(Wish.class));
    }

    @Test
    @Description("updateWish 테스트")
    void testUpdateWish() {
        // given
        given(memberService.getMemberById(anyLong())).willReturn(member);
        given(productService.getProductById(anyLong())).willReturn(product);
        given(wishRepository.findById(anyLong())).willReturn(Optional.of(wishHasId));
        given(wishRepository.save(any(Wish.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // when
        Wish updatedWish = wishService.updateWish(wishServiceDtoHasId);

        // then
        assertThat(updatedWish).isEqualTo(wishHasId);

        verify(memberService, times(1)).getMemberById(anyLong());
        verify(productService, times(1)).getProductById(anyLong());
        verify(wishRepository, times(1)).findById(anyLong());
        verify(wishRepository, times(1)).save(any(Wish.class));
    }

    @Test
    @Description("deleteWish 테스트")
    void testDeleteWish() {
        // given
        given(wishRepository.findById(anyLong())).willReturn(Optional.of(wishHasId));

        // when
        wishService.deleteWish(1L);

        // then
        verify(wishRepository, times(1)).findById(anyLong());
        verify(wishRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @Description("validateWishExists_NotFound 테스트")
    void testValidateWishExists_NotFound() {
        // given
        given(wishRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> wishService.deleteWish(1L))
                .isInstanceOf(WishNotFoundException.class);

        verify(wishRepository, times(1)).findById(anyLong());
    }
}
