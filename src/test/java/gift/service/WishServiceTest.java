package gift.service;

import gift.domain.Product;
import gift.domain.Wish;
import gift.domain.member.Member;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishService wishService;

    @DisplayName("위시리스트에 존재하는 모든 상품을 조회한다.")
    @Test
    void getProducts() throws Exception {
        //given
        Member member = new Member();
        PageRequest pageable = PageRequest.of(0, 10);

        given(wishRepository.findByMember(any(Member.class), any(Pageable.class))).willReturn(new PageImpl<>(List.of()));

        //when
        wishService.getProducts(member, pageable);

        //then
        then(wishRepository).should().findByMember(any(Member.class), any(Pageable.class));
    }

    @DisplayName("위시리스트에 상품 하나를 추가한다.")
    @Test
    void addProduct() throws Exception {
        //given
        Member member = new Member.MemberBuilder().id(1L).build();
        Product product = new Product();
        Wish wish = new Wish();

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(wishRepository.existsByMemberIdAndProductId(anyLong(), anyLong())).willReturn(false);
        given(wishRepository.save(any(Wish.class))).willReturn(wish);

        //when
        wishService.addProduct(member, 1L);

        //then
        then(productRepository).should().findById(anyLong());
        then(wishRepository).should().existsByMemberIdAndProductId(anyLong(), anyLong());
        then(wishRepository).should().save(any(Wish.class));
    }

    @DisplayName("위시리스트에 존재하는 상품 하나를 삭제한다.")
    @Test
    void removeProduct() throws Exception {
        //given
        Member member = new Member.MemberBuilder().id(1L).build();
        Product product = new Product();
        Wish wish = new Wish();

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(wishRepository.findByMemberAndProduct(any(Member.class), any(Product.class))).willReturn(Optional.of(wish));
        willDoNothing().given(wishRepository).delete(any(Wish.class));

        //when
        wishService.removeProduct(member, 1L);

        //then
        then(productRepository).should().findById(anyLong());
        then(wishRepository).should().findByMemberAndProduct(any(Member.class), any(Product.class));
        then(wishRepository).should().delete(any(Wish.class));
    }

}
