package com.accountapi.Database;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.accountapi.controller.AccountApiController;
import com.accountapi.entity.Account;

@Configuration
public class AccountModelAssembler implements RepresentationModelAssembler<Account, EntityModel<Account>>{ 



@Override
public EntityModel<Account> toModel(Account account) {
	  EntityModel<Account> accountModel = EntityModel.of(account);
	
	accountModel.add(linkTo(methodOn(AccountApiController.class).getOne(account.getId())).withSelfRel());
	accountModel.add(linkTo(methodOn(AccountApiController.class).deposit(account.getId(), null)).withRel("deposit"));
	accountModel.add(linkTo(methodOn(AccountApiController.class).withdraw(account.getId(), null)).withRel("withdrawals"));
	accountModel.add(linkTo(methodOn(AccountApiController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
	
	return accountModel;
}

}