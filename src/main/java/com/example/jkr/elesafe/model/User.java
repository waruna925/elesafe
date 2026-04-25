package com.example.jkr.elesafe.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    // ✅ Auto generates U0001, U0002, U0003 ...

    @Id
    private String userId ;

    private String nic;
    private String lastName;
    private String firstName;
    private Role role;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;
    private Gender gender;
    private String password;
    private String address;
    private String village;
    private UserStatus status;

    @Schema(enumAsRef = true)
    public enum Role {
        ADMIN, USER, MODERATOR, WILD_OFFICER
    }

    @Schema(enumAsRef = true)
    public enum Gender {
        MALE, FEMALE, OTHER
    }

    @Schema(enumAsRef = true)
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED, PENDING
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return status != UserStatus.SUSPENDED; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return status == UserStatus.ACTIVE; }
}