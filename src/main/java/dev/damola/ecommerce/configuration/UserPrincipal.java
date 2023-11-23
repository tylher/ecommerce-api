package dev.damola.ecommerce.configuration;

import dev.damola.ecommerce.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Builder
@Setter
public class UserPrincipal implements UserDetails {
    private User user;

    private String userId;
    private String email;
    private String password;
    private List<?extends GrantedAuthority> authorities;

    public UserPrincipal(User user){
        userId = user.getUserId();
        email = user.getEmail();
        password = user.getPassword();
        authorities = Arrays.stream(user.getRoles()
                .split(",")).map(role->new SimpleGrantedAuthority("ROLE_"+role.trim()))
                .collect(Collectors.toList());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
