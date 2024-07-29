package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    @Column(name = "option_name")
    private String optionName;

    public ProductOption(Product product, Option option, String optionName) {
        this.product = product;
        this.option = option;
        this.optionName = optionName;
    }

    public ProductOption() {
    }

    public Product getProduct() {
        return product;
    }

    public Option getOption() {
        return option;
    }
}
