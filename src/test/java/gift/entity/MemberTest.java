package gift.entity;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("회원 비밀번호와 입력된 비밀번호 일치 테스트")
    void isCorrectPassword() {
        Member member = new Member("sgoh@sgoh", "password");
        MemberDto memberDto = new MemberDto(null, "sgoh@sgoh", "password");
        assertThat(member.isCorrectPassword(memberDto.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 정보에 위시리스트 추가 테스트")
    void addWishlist() {
        Member member = new Member("sgoh@sgoh", "password");
        Category category = new Category("생일선물", "Red", "http", "생일선물 카테고리");
        Product product = new Product("kakao", 5000L, "https", category);
        Wishlist wishlist = new Wishlist(member, product);

        member.addWishlist(wishlist);
        assertThat(member.getWishlist().size()).isEqualTo(1);
    }
}