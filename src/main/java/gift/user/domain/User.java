package gift.user.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	//
	@Convert(converter = RoleConverter.class)
	@Column(nullable = false)
	private Set<Role> roles = new LinkedHashSet<>();

	public User() {
	}

	private User(String email, String password, Set<Role> roles) {
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public static User of(String email, String password, Set<Role> roles) {
		return new User(email, password, roles);
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

	public Set<Role> getRole() {
		return roles;
	}
}
