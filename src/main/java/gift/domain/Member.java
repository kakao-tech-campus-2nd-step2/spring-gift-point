package gift.domain;

import gift.exception.customException.PointsNotAvailableException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static gift.exception.errorMessage.Messages.*;

@Entity
@Table(name="members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "points")
    @Min(value = 0, message = POINTS_CANNOT_BE_NEGATIVE)
    private int points;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private List<Wish> wishes = new ArrayList<>();

    public void addWish(Wish wish) {
        this.wishes.add(wish);
        wish.setMember(this);
    }

    public void removeWish(Wish wish) {
        wish.setMember(null);
        this.wishes.remove(wish);
    }

    public void removeWishes() {
        Iterator<Wish> iterator = wishes.iterator();

        while(iterator.hasNext()){
            Wish wish = iterator.next();
            wish.setMember(null);
            iterator.remove();
        }
    }

    // JPA가 엔티티 객체를 생성할 때 reflection을 사용하기 때문에 필요함
    protected Member() {
    }

    public Member(Long id, String email, String password, int points) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.points = points;
    }

    public Member(String email, String password, int points) {
        this.email = email;
        this.password = password;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public int getPoints() {
        return points;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setWishes(List<Wish> wishes) {
        this.wishes = wishes;
    }

    public void addPoints(int addPoints){
        this.points += addPoints;
    }

    public void usePoints(int usePoints){
        this.points -= usePoints;
    }

    public void validatePointsUsage(int pointsUsed, int price) {
        if(pointsUsed > this.points) {
            throw new PointsNotAvailableException(INSUFFICIENT_POINTS);
        }

        if(pointsUsed > price / 2) {
            throw new PointsNotAvailableException(POINTS_USAGE_LIMIT);
        }
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
}
