package com.example.accountservice.Config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.accountservice.Entity.AccountEntity;

public class CustomUserDetails implements UserDetails {

    private String email;
    private String password;
    private String role;

    public CustomUserDetails(AccountEntity userCredential) {
        this.email = userCredential.getEmail();
        this.password = userCredential.getPassword();
        this.role=userCredential.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	Collection<GrantedAuthority> authorities =
                new ArrayList<GrantedAuthority>();
    	authorities.add(new SimpleGrantedAuthority(this.role));
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
