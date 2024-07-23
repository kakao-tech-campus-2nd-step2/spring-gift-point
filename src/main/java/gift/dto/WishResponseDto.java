package gift.dto;

import gift.domain.Wish;

public class WishResponseDto {
    private Long id;
    private String member_email;
    private String product_name;
    private int product_price;

    public WishResponseDto(Long id, String member_email, String product_name, int prduct_price) {
        this.id = id;
        this.member_email = member_email;
        this.product_name = product_name;
        this.product_price = prduct_price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public static WishResponseDto convertToDto(Wish wish) {
        return new WishResponseDto(
            wish.getId(),
            wish.getMember().getEmail(),
            wish.getProduct().getName(),
            wish.getProduct().getPrice());
    }
}
