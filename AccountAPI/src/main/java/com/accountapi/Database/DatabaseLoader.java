package com.accountapi.Database;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accountapi.Repository.AccountRepository;
import com.accountapi.entity.Account;

@Configuration
public class DatabaseLoader {
	
	private AccountRepository repo;
	
	public DatabaseLoader(AccountRepository repo) {
		this.repo = repo;
	}
	
	@Bean
	public CommandLineRunner initDatabase() {
		return args -> {
			Account account1 = new Account("1234567890", 100);
			Account account2 = new Account("1098456362",50);
			Account account3 = new Account("1098765432",1000);
			
			repo.saveAll(List.of(account1, account2, account3));
			
			System.out.println("Sample database initialized");
		};
	}
}
