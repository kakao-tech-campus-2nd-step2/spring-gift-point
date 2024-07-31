package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.dto.WishlistResponseDto;
import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class WishRepositoryTest {
    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("위시리스트 추가 태스트")
    public void addWishListTest() {
        Member member = new Member("admin@gmail.com", "password");
        memberRepository.save(member);

        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        Product product = new Product("치킨", 20000, "chicken.com", category);
        productRepository.save(product);

        Wish wish = new Wish(member, product);
        wishRepository.save(wish);

        List<WishlistResponseDto> wishList = wishRepository.findByMemberId(member.getId()).stream()
            .map(Wish::toDto)
            .collect(Collectors.toList());

        assertThat(wishList.get(0).getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("위시리스트 삭제 테스트")
    public void deleteWishTest() {
        Member member = new Member("admin@gmail.com", "password");
        memberRepository.save(member);

        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        Product product = new Product("치킨", 20000, "chicken.com", category);
        productRepository.save(product);

        Wish wish = new Wish(member, product);
        wishRepository.save(wish);

        wishRepository.delete(wish);

        List<Wish> wishList = wishRepository.findByMemberId(member.getId());
        assertThat(wishList).isEmpty();
    }


    @Test
    public void save() {
        Member member = new Member("admin@gmail.com", "password");
        memberRepository.save(member);

        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        Product product = new Product("치킨", 20000, "chicken.com", category);
        productRepository.save(product);

        Wish expected = new Wish(member, product);
        Wish actual = wishRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getMember().getId()).isEqualTo(expected.getMember().getId()),
            () -> assertThat(actual.getProduct().getId()).isEqualTo(expected.getProduct().getId())
        );
    }


    @Test
    public void findByMemberId() {
        Member member = new Member("admin@gmail.com", "password");
        memberRepository.save(member);

        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        Product product = new Product("치킨", 20000, "chicken.com", category);
        productRepository.save(product);

        Product product2 = new Product("피자", 30000, "pizza.com", category);
        productRepository.save(product2);

        wishRepository.save(new Wish(member, product));
        wishRepository.save(new Wish(member, product2));

        List<Wish> wishlists = wishRepository.findByMemberId(member.getId());
        assertThat(wishlists).hasSize(2);
    }

    @Test
    public void findByMemberIdPagingTest(){
        Member member = new Member("admin@gmail.com", "password");
        memberRepository.save(member);
        Long memberId = member.getId();

        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        for(int i=0; i<50; i++){
            Product product = new Product("name"+i,1000*i, i+".com",category);
            productRepository.save(product);

            Wish wish = new Wish(member, product);
            wishRepository.save(wish);
        }

        Pageable pageable = PageRequest.of(0,10);
        Page<Wish> page = wishRepository.findByMemberId(memberId, pageable);

        assertThat(page.getTotalElements()).isEqualTo(50);
        assertThat(page.getTotalPages()).isEqualTo(5);
        assertThat(page.getContent().size()).isEqualTo(10);

    }



}
