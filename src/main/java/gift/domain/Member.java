package gift.domain;

import gift.dto.MemberDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<WishedProduct> wishList = new ArrayList<>();

    protected Member() {

    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberDTO toDTO() {
        return new MemberDTO(email, password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<WishedProduct> getWishList() {
        return wishList;
    }
}
