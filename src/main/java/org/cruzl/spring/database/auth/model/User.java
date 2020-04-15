package org.cruzl.spring.database.auth.model;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import com.google.common.base.Joiner;
import com.sun.istack.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(of = "userName")
@EqualsAndHashCode(of = "userName")
public class User {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Id Long id;

	private @NotNull String userName;
	private @NotNull String password;
	private @NotNull String firstName;
	private @NotNull String lastName;
	private @NotNull String email;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	private String displayName;

	@Type(type = "org.hibernate.type.ImageType")
	private @Lob byte[] image;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public String getDisplayName() {
		String displayName;
		if (this.displayName == null) {
			displayName = this.getFullName();
		} else {
			displayName = this.displayName;
		}
		return displayName;
	}

	public String getFullName() {
		return Joiner.on(" ").join(this.firstName, this.lastName);
	}

	public String getImageAsString() {
		String image = null;
		if (this.image != null) {
			byte[] encodeBase64 = Base64.getEncoder().encode(this.image);
			image = new String(encodeBase64);
		}
		return image;
	}
}
