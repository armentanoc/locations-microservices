package br.ucsal.domain.users;

import br.ucsal.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String securePassword;

	@Column(nullable = false)
	private UserRole role;

	protected User() {
		// default for JPA
	}

	public User(String name, String email, String username, String securePassword, UserRole role) {
		this.name = name;
		this.email = email;
		this.username = username;
		this.securePassword = securePassword;
		this.role = role;
	}

	public User(User user) {
		super();
		this.username = user.username;
		this.email = user.email;
		this.name = user.name;
		this.securePassword = user.securePassword;
		this.role = user.role;
	}

	public User copy() {
		return new User(this);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if (username != null) {
			this.username = username;
		}
	}

	public String getPassword() {
		return securePassword;
	}

	public void setPassword(String securePassword) {
		if (securePassword != null) {
			this.securePassword = securePassword;
		}
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		if (role != null) {
			this.role = role;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null) {
			this.name = name;
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null) {
			this.email = email;
		}
	}

	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				", securePassword='" + securePassword + '\'' +
				", role=" + role +
				'}';
	}
}
