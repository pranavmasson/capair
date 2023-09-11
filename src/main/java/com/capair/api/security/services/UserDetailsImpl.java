package com.capair.api.security.services;
import java.util.Collection;
import java.util.HashSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.capair.api.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Holds information for the Authentication Class from Spring Security
 */
public class UserDetailsImpl implements UserDetails{

    private int id;

    private String username;

    public UserDetailsImpl(int id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword());
    }

    public int getId() {
        return id;
    }

    @JsonIgnore // do not want to show private user information
    private String password;

    @Override
    public String getPassword() {
       return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //credentials cannot be expired per implementation
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {        
        return new HashSet<GrantedAuthority>();
    }
    
}
