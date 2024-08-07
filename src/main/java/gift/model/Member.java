package gift.model;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private int points;

  public Member(Long id, String email, String password, String name, int points) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.points = points;
  }

  public Member(String email, String password, String name, int points) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.points = points;
  }

  public Member() {
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

  public String getName() {
    return name;
  }

  public int usePointsIfAvailable(boolean usePoints, int totalPrice) {
    if (usePoints && this.points > 0) {
      int pointsToUse = Math.min(totalPrice, this.points);
      this.points -= pointsToUse;
      return pointsToUse;
    }
    return 0;
  }

  public int calculateAndAddPoints(int totalPrice) {
    int pointsEarned = (int) (totalPrice * 0.1);
    this.points += pointsEarned;
    return pointsEarned;
  }

  public int getPoints() {
    return points;
  }
}