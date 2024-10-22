package com.accountapi.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accountapi.Amount.Amount;
import com.accountapi.Database.AccountModelAssembler;
import com.accountapi.entity.Account;
import com.accountapi.service.AccountService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/accounts")
public class AccountApiController {
	
	private AccountService service;
	private AccountModelAssembler assembler;
	
	public AccountApiController(AccountService service, AccountModelAssembler assembler) {
		this.service = service;
		this.assembler=assembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<Account>>> listAll(){
		List<Account> listAccounts =  service.listAll();
		
		if(listAccounts.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		CollectionModel<EntityModel<Account>> collectionModel = assembler.toCollectionModel(listAccounts);
		
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Account>> getOne(@PathVariable("id")Integer id) {
		try {
			Account account = service.get(id);
			
			EntityModel<Account> entityModel = assembler.toModel(account);
			
			return new ResponseEntity<>(entityModel, HttpStatus.OK);
		}
		catch(NoSuchElementException ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<Account>> add(@RequestBody Account account){
		Account savedAccount = service.save(account);
		
		EntityModel<Account> entityModel = assembler.toModel(account);
		
		
		return ResponseEntity.created(
				linkTo(methodOn(AccountApiController.class).getOne(savedAccount.getId())).toUri())
				.body(entityModel);
	}
	
	@PutMapping
	public ResponseEntity<EntityModel<Account>> replace(@RequestBody Account account){
		Account updatedAccount = service.save(account);
		
		EntityModel<Account> entityModel = assembler.toModel(account);
		
		
		
		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}/deposit")
	public ResponseEntity<EntityModel<Account>> deposit(@PathVariable("id")Integer id, @RequestBody Amount amount){
		Account updatedAccount = service.deposit(amount.getAmount(), id);
		
		EntityModel<Account> entityModel = assembler.toModel(updatedAccount);
		
		return new ResponseEntity<>(entityModel, HttpStatus.OK);
		
	}
	
	@PatchMapping("/{id}/withdraw")
	public ResponseEntity<EntityModel<Account>> withdraw(@PathVariable("id")Integer id, @RequestBody Amount amount){
		Account updatedAccount = service.withdraw(amount.getAmount(), id);
		
		EntityModel<Account> entityModel = assembler.toModel(updatedAccount);
		
		return new ResponseEntity<>(entityModel, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id")Integer id){
		try {
			service.delete(id);
			
			return ResponseEntity.noContent().build();
		}
		catch(AccountNotFoundException ex) {
			return ResponseEntity.notFound().build();
	}
	}

}
