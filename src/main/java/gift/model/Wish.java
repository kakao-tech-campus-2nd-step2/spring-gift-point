package gift.model;

import jakarta.persistence.*;

@Entity
@Table(name = "wish")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    protected Wish() {
    }

    public Wish(User user, Option option) {
        this.user = user;
        this.option = option;
    }

    public Long getId() { return id; }

    public User getUser() { return user; }

    public Option getOption() { return option; }

    public Long getUserId() { return user.getId(); }

    public Long getOptionId() { return option.getId(); }
}
