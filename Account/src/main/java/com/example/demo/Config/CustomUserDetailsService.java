package com.example.demo.Config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.example.demo.Entity.AccountEntity;
import com.example.demo.Repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<AccountEntity> credential = accountRepository.findByEmail(username).blockOptional();
        return credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
	}
}
