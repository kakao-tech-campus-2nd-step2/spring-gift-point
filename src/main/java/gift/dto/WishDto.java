package gift.dto;

import gift.model.member.Member;
import gift.model.product.Product;

public class WishDto {
    private Product product;
    private Member member;
    private int amount;

    public WishDto(Product product,Member member, int amount){
        this.product = product;
        this.member = member;
        this.amount = amount;
    }

    public Long getProductId(){
        return product.getId();
    }

    public Long getMemberId(){
        return member.getId();
    }

    public Product getProduct(){
        return product;
    }

    public Member getMember(){
        return member;
    }

    public int getAmount(){
        return amount;
    }
}
