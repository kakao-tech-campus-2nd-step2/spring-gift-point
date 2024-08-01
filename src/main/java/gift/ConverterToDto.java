package gift;

import gift.DTO.Category;
import gift.DTO.CategoryDto;
import gift.DTO.Member;
import gift.DTO.MemberDto;
import gift.DTO.Option;
import gift.DTO.OptionDto;
import gift.DTO.Product;
import gift.DTO.ProductDto;
import gift.DTO.WishList;
import gift.DTO.WishListDto;

public class ConverterToDto {

  public static ProductDto convertToProductDto(Product product) {
    ProductDto productDto = new ProductDto(product.getId(), product.getName(),
      product.getPrice(), product.getImageUrl(), convertToCategoryDto(product.getCategory()));
    return productDto;
  }

  public static MemberDto convertToUserDto(Member member) {
    MemberDto memberDto = new MemberDto(member.getId(), member.getEmail(), member.getPassword());
    return memberDto;
  }

  public static WishListDto convertToWishListDto(WishList wishList) {
    ProductDto productDto = convertToProductDto(wishList.getProduct());
    MemberDto memberDto = convertToUserDto(wishList.getMember());
    WishListDto wishListDto = new WishListDto(wishList.getId(), memberDto, productDto);
    return wishListDto;
  }

  public static CategoryDto convertToCategoryDto(Category category) {
    CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName(),
      category.getColor(), category.getImageUrl(), category.getDescription());
    return categoryDto;
  }

  public static OptionDto convertToOptionDto(Option option) {
    ProductDto productDto = convertToProductDto(option.getProduct());
    OptionDto optionDto = new OptionDto(option.getId(), option.getName(), option.getQuantity(),
      productDto);
    return optionDto;
  }
}
