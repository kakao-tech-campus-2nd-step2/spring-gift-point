package gift.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    public Option() {}

    public void update(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // (selectable feature) admin only has right to add option.
}
