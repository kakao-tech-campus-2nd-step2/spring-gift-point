package gift.domain.MemberDomain;

import gift.domain.WishListDomain.WishList;
import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "member")
public class Member {
    @Id
    private String id;

    private String name;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<WishList> wishList;

    @Embedded
    private MemberPoint memberPoint;

    public Member() {
    }

    public Member(String id, String name, String password,List<WishList> wishList) {
        this.id = id;
        this.password = password;
        this.wishList = wishList;
        this.name = name;
    }
  
    public Member(String id, String password, LinkedList<WishList> wishLists,MemberPoint memberPoint) {
        this.id = id;
        this.password = password;
        this.wishList = wishLists;
        this.name = null;
        this.memberPoint = memberPoint;
    }

    public Member(String email, String password, LinkedList<WishList> wishLists) {
        this(email,password,wishLists,new MemberPoint(1000));
    }


    public String getPassword() {
        return password;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPoint() {
        return memberPoint.getPoint();
    }

    public Integer subtractPoint(int usePoint) throws IllegalAccessException {
        memberPoint.subtract(usePoint);
        return memberPoint.getPoint();
    }

    public List<WishList> getWishList() {
        return wishList;
    }

    public boolean checkPassword(String password){
        if(this.password.equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setWishList(List<WishList> wishLists) {
        this.wishList = wishLists;
    }
}
