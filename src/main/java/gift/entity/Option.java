package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OptionName name;

    protected Option() {
    }

    public Option(OptionName name) {
        this(null, name);
    }

    public Option(Long id, OptionName name) {
        this.id = id;
        this.name = name;
    }

    public void update(OptionName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public OptionName getName() {
        return name;
    }
}
