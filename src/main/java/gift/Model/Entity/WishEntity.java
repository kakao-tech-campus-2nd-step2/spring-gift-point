package gift.Model.Entity;

import gift.Model.response.WishResponse;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="wish")
public class WishEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @OnDelete(action= OnDeleteAction.CASCADE)
        @JoinColumn(name="member_id")
        private MemberEntity member;

        @ManyToOne(fetch = FetchType.LAZY)
        @OnDelete(action= OnDeleteAction.CASCADE)
        @JoinColumn(name="product_id")
        private ProductEntity product;

        public WishEntity(){}

        public WishEntity(MemberEntity member, ProductEntity product){
                this.member = member;
                this.product = product;
        }

        public WishResponse mapToResponse(){
                return new WishResponse(this.product.getId(), this.product.getName(), this.product.getPrice(), this.product.getImageUrl(), this.id);
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public MemberEntity getMember() {
                return member;
        }

        public void setMember(MemberEntity member) {
                this.member = member;
        }

        public ProductEntity getProduct() {
                return product;
        }

        public void setProduct(ProductEntity product) {
                this.product = product;
        }
}
