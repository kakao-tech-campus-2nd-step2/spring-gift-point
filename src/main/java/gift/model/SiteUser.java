package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "site_user")
public class SiteUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true , nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(unique = true , nullable = false)
	private String email;

	public SiteUser() {
	}

	public SiteUser(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
}


