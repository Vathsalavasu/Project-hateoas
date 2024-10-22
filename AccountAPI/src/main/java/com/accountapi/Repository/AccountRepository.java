package com.accountapi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.accountapi.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	
	@Query("UPDATE Account a SET a.balance = a.balance + ?1 WHERE a.id =?2")
	@Modifying
	public void updateBalance(float amount, Integer id);


}
