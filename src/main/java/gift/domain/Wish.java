package gift.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="wishes")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected Wish () {
    }

    private Wish(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.option = builder.option;
        this.quantity = builder.quantity;
    }

    public Wish(Long id, Member member, Option option, int quantity) {
        this.id = id;
        this.member = member;
        this.option = option;
        this.quantity = quantity;
    }

    public Wish(Member member, Option option, int quantity) {
        this.member = member;
        this.option = option;
        this.quantity = quantity;
        if(this.member != null){
            this.member.addWish(this);
        }
        if(this.option != null){
            this.option.addWish(this);
        }
    }

    public void remove(){
        if(this.member != null){
            this.member.removeWish(this);
        }
        if(this.option != null){
            this.option.removeWish(this);
        }
    }

    public void updateQuantity(int quantity){
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public static class Builder{
        private Long id;
        private Member member;
        private Option option;
        private int quantity;

        public Builder id(Long id){
            this.id = id;
            return this;
        }

        public Builder member(Member member){
            this.member = member;
            return this;
        }

        public Builder option(Option option){
            this.option = option;
            return this;
        }

        public Builder qunatity(int quantity){
            this.quantity = quantity;
            return this;
        }

        public Wish build() {
            Wish wish = new Wish(this);
            if(wish.member != null && wish.id == null){
                wish.member.addWish(wish);
            }
            if(wish.option != null && wish.id == null){
                wish.option.addWish(wish);
            }
            return wish;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wish wish = (Wish) o;
        return Objects.equals(id, wish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}