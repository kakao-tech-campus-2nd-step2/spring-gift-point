//package gift.model.wishlist;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//@DataJpaTest
//public class WishListRepositoryTest {
//
//    @Autowired
//    private WishListRepository wishListRepository;
//
//    @Test
//    @DisplayName("WishList 생성 테스트")
//    public void createTest() {
//        WishListEntity wishList1 = new WishListEntity();
//        wishList1.setProductId(1L);
//        wishList1.setUserId(1L);
//        wishListRepository.save(wishList1);
//
//        WishListEntity wishList2 = new WishListEntity();
//        wishList2.setProductId(2L);
//        wishList2.setUserId(1L);
//        wishListRepository.save(wishList2);
//
//        List<WishListEntity> wishLists = wishListRepository.findAllByUserId(1L);
//        assertThat(wishLists).hasSize(2);
//    }
//
//    @Test
//    @DisplayName("WishList 삭제 테스트(사용자 ID)")
//    public void delteByUserIdTest() {
//        WishListEntity wishList1 = new WishListEntity();
//        wishList1.setProductId(1L);
//        wishList1.setUserId(1L);
//        wishListRepository.save(wishList1);
//
//        WishListEntity wishList2 = new WishListEntity();
//        wishList2.setProductId(2L);
//        wishList2.setUserId(1L);
//        wishListRepository.save(wishList2);
//
//        int deleted = wishListRepository.deleteWishListsByUserId(1L);
//        assertThat(deleted).isGreaterThan(0);
//        List<WishListEntity> wishLists = wishListRepository.findAllByUserId(1L);
//        assertThat(wishLists).isEmpty();
//    }
//
//    @Test
//    @DisplayName("WishList 삭제 테스트(사용자 ID, 상품 ID)")
//    public void deleteByUserIdAndProductIdTest() {
//        WishListEntity wishList1 = new WishListEntity();
//        wishList1.setProductId(1L);
//        wishList1.setUserId(1L);
//        wishListRepository.save(wishList1);
//
//        WishListEntity wishList2 = new WishListEntity();
//        wishList2.setProductId(2L);
//        wishList2.setUserId(1L);
//        wishListRepository.save(wishList2);
//
//        int deleted = wishListRepository.deleteWishListByUserIdAndProductId(1L, 1L);
//        assertThat(deleted).isGreaterThan(0);
//        List<WishListEntity> wishLists = wishListRepository.findAllByUserId(1L);
//        assertThat(wishLists).hasSize(1);
//        assertThat(wishLists.get(0).getProductId()).isEqualTo(2L);
//    }
//}
