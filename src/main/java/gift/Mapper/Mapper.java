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
        Optional<MemberDto> memberDtoOptional = memberService.findByUserId(wishlistDto.getUserId());
        MemberDto memberDto = memberDtoOptional.get();

        Optional<ProductDto> productDtoOptional = productService.getProductById(wishlistDto.getProductId());
        ProductDto productDto = productDtoOptional.get();
        productDto.setCategoryId(productDtoOptional.get().getCategoryId());

        OptionDto optionDto = optionService.getOptionById(wishlistDto.getOptionId());

        WishlistId id = new WishlistId(memberDto.getId(), productDto.getId());
        return new Wishlist(id, memberDtoToEntity(memberDto), productDtoToEntity(productDto), wishlistDto.getProductName(), wishlistDto.getCount(), wishlistDto.getPrice(), optionDtoToEntity(optionDto));

    }

    public WishlistDto wishlistToDto(Wishlist wishlist) {
        return new WishlistDto(wishlist.getMemberId(), wishlist.getProductId(), wishlist.getCount(), 0, wishlist.getProductName(), wishlist.getPrice(), wishlist.getOption().getId());
    }

    public Category categoryDtoToEntity(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public CategoryDto categoryToDto(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName());
    }

    //categoryService를 포함하기엔 뭔가 일관성이 깨지는 느낌인데???
    public Product productDtoToEntity(ProductDto productDto) {
        Category category = categoryService.getCategoryById(productDto.getCategoryId()).get();
        CategoryDto categoryDto = categoryToDto(category);
        return new Product(productDto.getId(), productDto.getName(), categoryDtoToEntity(categoryDto), productDto.getPrice(), productDto.getImageUrl(), productDto.isDeleted());
    }

    public ProductDto productToDto(Product product) {
        Category category = categoryService.getCategoryById(product.getCategory().getCategoryId()).get();

        return new ProductDto(product.getId(), product.getName(), categoryToDto(category).getId(), product.getPrice(), product.getImageUrl(), product.isDeleted());
    }

    public Member memberDtoToEntity(MemberDto memberDto) {
        return new Member(memberDto.getId(), memberDto.getEmail(), memberDto.getName(), memberDto.getPassword(), memberDto.isAdmin());
    }

    public MemberDto memberToDto(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getName(), member.getPassword(), member.isAdmin());
    }

    public OptionDto optionToDto(Option option) {
        return new OptionDto(option.getId(), option.getProduct().getId(), option.getName(), option.getPrice(), option.getQuantity());
    }

    public Option optionDtoToEntity(OptionDto optionDto) {
        Optional<ProductDto> productDtoOptional = productService.getProductById(optionDto.getProductId());
        ProductDto productDto = productDtoOptional.get();
        return new Option(optionDto.getId(), productDtoToEntity(productDto), optionDto.getName(), optionDto.getPrice(), optionDto.getQuantity());
    }

}
