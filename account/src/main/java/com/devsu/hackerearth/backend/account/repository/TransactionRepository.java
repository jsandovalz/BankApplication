package com.devsu.hackerearth.backend.account.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("Select t from Transaction t where t.accountId in "
        + "(Select a.id from Account a where a.clientId= :clientId)"
        + " and t.date between :dateTransactionStart and :dateTransactionEnd "
        + " order by t.date asc")
    List<Transaction> findAllByAccountClientIdAndDateBetween(
        @Param("clientId") Long clientId,
        @Param("dateTransactionStart") Date dateTransacionStart,
        @Param("dateTransactionEnd") Date dateTransactionEnd);

    Transaction findTopByAccountIdOrderByDateDescIdDesc(Long accountId);
    
}
