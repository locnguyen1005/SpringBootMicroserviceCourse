package com.example.demo.Controller;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;

import com.example.demo.Config.CustomUserDetailsService;
import com.example.demo.DTO.AccountDTO;

import com.example.demo.Entity.AccountEntity;

import com.example.demo.Repository.AccountRepository;
import com.example.demo.Service.AccountService;
import com.example.demo.Service.JwtService;
import com.example.demo.utils.CommonValidate;
import com.example.demo.utils.Constant;
import com.example.demo.utils.ConstantCommon;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:3006/") // Replace with the origin of your React app
@RequestMapping("Account")
@Slf4j
public class AccountController {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	Gson gson = new Gson();
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@GetMapping("/GetAll")
	public Flux<AccountDTO> getAllAccount() {
		return accountService.getAllAccount();
	}

	@PostMapping("/Create")
	public ResponseEntity<Mono<AccountDTO>> createAccount(@RequestParam("data") String requestStr , @RequestParam(value = "file") MultipartFile file) {
		InputStream inputStream = AccountController.class.getClassLoader()
				.getResourceAsStream(Constant.JSON_CREATE_ACCOUNT);
		CommonValidate.jsonValidate(requestStr, inputStream);
		log.info(requestStr);
		log.info(requestStr);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(accountService.createAccount(gson.fromJson(requestStr, AccountDTO.class),file));
	}
	@PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AccountDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
        	log.info(jwtService.generateToken(authRequest.getEmail()));
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
	@GetMapping("/login")
	public Mono<AccountDTO> login(){	
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Mono<AccountDTO> accountEntity = accountService.findAccount(userDetails.getUsername());
			return accountEntity;
	}
	@GetMapping("/getEmail")
	public Mono<AccountDTO> getaccountId(@RequestBody String email){	
		return accountService.findAccount(email);
	}
	@GetMapping("/{accountid}")
	public Mono<AccountDTO> getaccountById(@PathVariable Long accountid){
		return accountService.getAccountbyID(accountid);
	}
}
