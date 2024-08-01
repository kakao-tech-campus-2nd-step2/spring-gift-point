package gift.domain.WishList;

import gift.domain.Member.Member;
import gift.domain.Menu.Menu;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    private Menu menu;

    private Date createdDate;

    public WishList() {
    }

    public WishList(Member member, Menu menu,Date createdDate) {
        this(null, member, menu,createdDate);
    }

    public WishList(Long id, Member member, Menu menu,Date createdDate) {
        this.id = id;
        this.member = member;
        this.menu = menu;
        this.createdDate = createdDate;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishList wishList = (WishList) o;
        return Objects.equals(id, wishList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}