package br.com.manzano.challenge.n26.repository;

import br.com.manzano.challenge.n26.util.StatisticManager;
import br.com.manzano.challenge.n26.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public class TransactionRepositoryInMemory implements TransactionRepository {

    @Autowired
    StatisticManager statisticManager;

    @Override
    public void persistTransaction(Transaction transaction) {
        statisticManager.persistNewTransaction(transaction);
    }
}
