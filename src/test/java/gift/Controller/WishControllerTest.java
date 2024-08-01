package gift.Controller;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.DTO.CategoryDto;
import gift.DTO.MemberDto;
import gift.DTO.ProductDto;
import gift.DTO.WishListDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WishControllerTest {

  WishController wishController;
  ProductController productController;
  MemberController memberController;
  CategoryController categoryController;


  @Autowired
  public WishControllerTest(WishController wishController, ProductController productController,
    MemberController memberController, CategoryController categoryController) {
    this.wishController = wishController;
    this.productController = productController;
    this.memberController = memberController;
    this.categoryController = categoryController;
  }

  @Test
  void getWishListTest() {
    Pageable pageable = PageRequest.of(0, 5);
    CategoryDto categoryDto = new CategoryDto(1L, "교환권", "#61cdef", "image.url", "교환권 카테고리");
    categoryController.addCategory(categoryDto);

    MemberDto memberDto1 = new MemberDto(1L, "a@naver.com", "abcde");
    memberController.SignUp(memberDto1);

    ProductDto productDto1 = new ProductDto(1L, "product1", 100, "abcd.img", categoryDto);
    ProductDto productDto2 = new ProductDto(2L, "product2", 200, "efgh.img", categoryDto);
    productController.addProduct(productDto1);
    productController.addProduct(productDto2);

    WishListDto wishListDto1 = new WishListDto(1L, memberDto1, productDto1);
    WishListDto wishListDto2 = new WishListDto(2L, memberDto1, productDto2);
    wishController.addProductToWishList(wishListDto1, null);
    wishController.addProductToWishList(wishListDto2, null);

    ResponseEntity<Page<WishListDto>> wishListDto = wishController.getWishList(pageable);
    Page<WishListDto> wishListDtos = wishListDto.getBody();

    assertThat(wishListDtos.getContent().get(0).getMemberDto().getId()).isEqualTo(
      memberDto1.getId());
    assertThat(wishListDtos.getContent().get(0).getMemberDto().getEmail()).isEqualTo(
      memberDto1.getEmail());
    assertThat(wishListDtos.getContent().get(0).getMemberDto().getPassword()).isEqualTo(
      memberDto1.getPassword());

    assertThat(wishListDtos.getContent().get(0).getProductDto().getId()).isEqualTo(
      productDto1.getId());
    assertThat(wishListDtos.getContent().get(0).getProductDto().getName()).isEqualTo(
      productDto1.getName());
    assertThat(wishListDtos.getContent().get(0).getProductDto().getPrice()).isEqualTo(
      productDto1.getPrice());
    assertThat(wishListDtos.getContent().get(0).getProductDto().getImageUrl()).isEqualTo(
      productDto1.getImageUrl());

    assertThat(wishListDtos.getContent().get(1).getProductDto().getId()).isEqualTo(
      productDto2.getId());
    assertThat(wishListDtos.getContent().get(1).getProductDto().getName()).isEqualTo(
      productDto2.getName());
    assertThat(wishListDtos.getContent().get(1).getProductDto().getPrice()).isEqualTo(
      productDto2.getPrice());
    assertThat(wishListDtos.getContent().get(1).getProductDto().getImageUrl()).isEqualTo(
      productDto2.getImageUrl());

  }

  @Test
  void addProductToWishListTest() {
    MemberDto memberDto1 = new MemberDto(1L, "a@naver.com", "abcde");
    memberController.SignUp(memberDto1);

    CategoryDto categoryDto = new CategoryDto(1L, "교환권3", "#61cdef", "image.url", "교환권 카테고리");
    categoryController.addCategory(categoryDto);

    ProductDto productDto1 = new ProductDto(1L, "product13", 100, "abcd.img", categoryDto);
    productController.addProduct(productDto1);

    WishListDto wishListDto1 = new WishListDto(1L, memberDto1, productDto1);

    WishListDto addedWishListDto = wishController.addProductToWishList(wishListDto1, null)
      .getBody();

    assertThat(addedWishListDto.getMemberDto().getId()).isEqualTo(memberDto1.getId());
    assertThat(addedWishListDto.getMemberDto().getEmail()).isEqualTo(memberDto1.getEmail());
    assertThat(addedWishListDto.getMemberDto().getPassword()).isEqualTo(memberDto1.getPassword());

    assertThat(addedWishListDto.getProductDto().getId()).isEqualTo(productDto1.getId());
    assertThat(addedWishListDto.getProductDto().getName()).isEqualTo(productDto1.getName());
    assertThat(addedWishListDto.getProductDto().getPrice()).isEqualTo(productDto1.getPrice());
    assertThat(addedWishListDto.getProductDto().getImageUrl()).isEqualTo(productDto1.getImageUrl());

  }

  @Test
  void deleteProductToWishListTest() {
    MemberDto memberDto1 = new MemberDto(1L, "a@naver.com", "abcde");
    memberController.SignUp(memberDto1);

    CategoryDto categoryDto = new CategoryDto(1L, "교환권2", "#61cdef", "image.url", "교환권 카테고리");
    categoryController.addCategory(categoryDto);

    ProductDto productDto1 = new ProductDto(1L, "product12", 100, "abcd.img", categoryDto);
    productController.addProduct(productDto1);

    WishListDto wishListDto1 = new WishListDto(1L, memberDto1, productDto1);
    wishController.addProductToWishList(wishListDto1, null);

    ResponseEntity responseEntity = wishController.deleteProductToWishList(1L);
    ResponseEntity<Void> expectedResponse = ResponseEntity.noContent().build();

    assertThat(responseEntity.getStatusCode()).isEqualTo(expectedResponse.getStatusCode());
  }
}