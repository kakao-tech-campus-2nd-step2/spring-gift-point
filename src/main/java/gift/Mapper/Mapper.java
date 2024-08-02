package gift.Mapper;

import gift.Entity.*;
import gift.Model.*;
import gift.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Mapper {

    private final ProductService productService;
    private final MemberService memberService;
    private final WishlistService wishlistService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    @Autowired
    public Mapper(@Lazy ProductService productService, @Lazy MemberService memberService, @Lazy WishlistService wishListService, @Lazy CategoryService categoryService, @Lazy OptionService optionService) {
        this.productService = productService;
        this.memberService = memberService;
        this.wishlistService = wishListService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }


    public Wishlist wishlistDtoToEntity(WishlistDto wishlistDto) {
        Optional<MemberDto> memberDtoOptional = memberService.findByUserId(wishlistDto.getMemberId());
        MemberDto memberDto = memberDtoOptional.get();

        Optional<ProductDto> productDtoOptional = productService.getProductById(wishlistDto.getProductId());
        ProductDto productDto = productDtoOptional.get();
        productDto.setCategoryId(productDtoOptional.get().getCategoryId());

        OptionDto optionDto = optionService.getOptionById(wishlistDto.getOptionId());

        return new Wishlist(wishlistDto.getWishlistId(), memberDtoToEntity(memberDto), productDtoToEntity(productDto), wishlistDto.getProductName(), wishlistDto.getQuantity(), wishlistDto.getPrice(), optionDtoToEntity(optionDto));

    }

    public WishlistDto wishlistToDto(Wishlist wishlist) {
       return new WishlistDto(wishlist.getId(), wishlist.getMember().getId(), wishlist.getProduct().getId(), wishlist.getCount(), wishlist.getProductName(), wishlist.getPrice(), wishlist.getOption().getId());
    }

    public Category categoryDtoToEntity(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName(), categoryDto.getDescription(), categoryDto.getColor(), categoryDto.getImageUrl());
    }

    public CategoryDto categoryToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getDescription(), category.getColor(), category.getImageUrl());
    }

    public Product productDtoToEntity(ProductDto productDto) {
        CategoryDto categoryDto = categoryService.getCategoryById(productDto.getCategoryId());
        return new Product(productDto.getId(), productDto.getName(), categoryDtoToEntity(categoryDto), productDto.getPrice(), productDto.getImageUrl());
    }

    public ProductDto productToDto(Product product) {
        CategoryDto categoryDto = categoryService.getCategoryById(product.getCategory().getId());

        return new ProductDto(product.getId(), product.getName(), categoryDto.getId(), product.getPrice(), product.getImageUrl());
    }

    public Member memberDtoToEntity(MemberDto memberDto) {
        return new Member(memberDto.getId(), memberDto.getEmail(), memberDto.getPassword(), memberDto.isAdmin());
    }

    public MemberDto memberToDto(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getPassword(), member.isAdmin());
    }

    public OptionDto optionToDto(Option option) {
        return new OptionDto(option.getId(), option.getProduct().getId(), option.getName(), option.getQuantity());
    }

    public Option optionDtoToEntity(OptionDto optionDto) {
        Optional<ProductDto> productDtoOptional = productService.getProductById(optionDto.getProductId());
        ProductDto productDto = productDtoOptional.get();
        return new Option(optionDto.getId(), productDtoToEntity(productDto), optionDto.getName(), optionDto.getQuantity());
    }

}
