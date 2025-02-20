package com.laila.pet_symptom_tracker.entities.user;

import com.laila.pet_symptom_tracker.entities.authentication.Role;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @Column(unique = true, nullable = false)
  @Setter
  private String email;

  @Column(nullable = false)
  @Setter
  private String password;

  @Column(unique = true, nullable = false)
  @Setter
  private String username;

  private String role;

  @Column(nullable = true)
  @Setter
  private String firstName;

  @Column(nullable = true)
  @Setter
  private String lastName;

  @Column(nullable = false)
  @Setter
  private Boolean enabled;

  @Column(nullable = false)
  @Setter
  private Boolean locked;

  public User(String email, String password, String username, Role role) {
    this.email = email;
    this.password = password;
    this.username = username;
    this.enabled = true;
    this.locked = false;
    this.role = role.toString();
  }

  public User(Builder builder) {
    this.username = builder.username;
    this.email = builder.email;
    this.password = builder.password;
    this.firstName = builder.firstname;
    this.lastName = builder.lastname;
    this.role = builder.role == null ? Role.USER.toString() : builder.role;
  }

  public boolean hasRole(Role role) {
    return this.role.equals(role.toString());
  }

  public void setRole(Role role) {
    this.role = role.toString();
  }

  public Boolean isLocked() {
    return this.locked;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role));
  }

  @Override
  public boolean isAccountNonLocked() {
    return locked;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  /*  ~~~~~~~~~~~  Helper methods ~~~~~~~~~~~  */
  public Boolean isAdmin() {
    return hasRole(Role.ADMIN);
  }

  public Boolean isModerator() {
    return hasRole(Role.MODERATOR);
  }

  public Boolean isSameUser(Object otherUser) {
    if (otherUser instanceof User) {
      return this.getId().equals(((User) otherUser).id);
    }
    return false;
  }

  public Boolean isUser() {
    return hasRole(Role.USER);
  }

  public static class Builder {
    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String role;

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder firstname(String firstname) {
      this.firstname = firstname;
      return this;
    }

    public Builder lastname(String lastname) {
      this.lastname = lastname;
      return this;
    }

    public Builder role(Role role) {
      if (role == null) {
        this.role = Role.USER.toString();
      } else {
        this.role = role.toString();
      }
      return this;
    }

    public User build() {
      return new User(this);
    }
  }
}
