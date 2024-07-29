package gift.model.gift;

import gift.model.category.Category;
import gift.model.option.Option;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gift")
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int price;

    @NotNull
    @Column(name = "imageurl")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;


    @OneToMany(mappedBy = "gift", cascade = CascadeType.ALL)
    private List<Option> options = new ArrayList<>();

    protected Gift() {
    }

    public Gift(String name, int price, String imageUrl, Category category) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("카카오 문구는 MD와 협의 후 사용가능합니다.");
        }
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Gift(String name, int price, String imageUrl, Category category, List<Option> options) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("카카오 문구는 MD와 협의 후 사용가능합니다.");
        }
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
        for (Option option : options) {
            option.setGift(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public int getPrice() {
        return price;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    private boolean isValidName(String name) {
        return name != null && !name.contains("카카오");
    }

    public void modify(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public boolean hasOption(Long optionId) {
        return options.stream()
                .anyMatch(option -> option.getId().equals(optionId));
    }


    public void addOption(Option option) {
        option.setGift(this);
        this.options.add(option);
    }

    public void removeOption(Option option) {
        this.options.remove(option);
    }
}