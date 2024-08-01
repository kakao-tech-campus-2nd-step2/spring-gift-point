package gift.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "options")
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany
    @Column(name = "option_list", nullable = false)
    private List<Option> optionList;

    protected Options() {}

    public Options(Product product, List<Option> options) {
        this.product = product;
        this.optionList = options;
    }

    public Option addOption(Option option) {
        if (optionList.stream()
                .anyMatch(inList -> inList.getName().equals(option.getName()))) {
            throw new IllegalArgumentException("옵션 이름이 중복되서는 안됩니다.");
        } else {
            optionList.add(option);
            return option;
        }
    }

    public Option updateOption(Option option) {
        optionList = optionList.stream()
                .filter(inList -> inList.getId() != option.getId())
                .collect(Collectors.toList());
        optionList.add(option);

        return option;
    }

    public void deleteOption(int id) {
        optionList = optionList.stream()
                .filter(option -> option.getId() != id)
                .collect(Collectors.toList());
    }

    public List<Option> getOptions() {
        return optionList;
    }

    public Product getProduct() {
        return product;
    }
}
