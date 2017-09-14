package br.com.manzano.challenge.n26.repository;

import br.com.manzano.challenge.n26.model.Transaction;

public interface TransactionRepository {

    void persistTransaction(Transaction transaction);
}
