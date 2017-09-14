package br.com.manzano.challenge.n26.controller;

import br.com.manzano.challenge.n26.model.Transaction;
import br.com.manzano.challenge.n26.repository.TransactionRepository;
import br.com.manzano.challenge.n26.repository.TransactionRepositoryInMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepositoryInMemory;

    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> transaction(@RequestBody Transaction transaction, HttpServletResponse response) {
        // Allow cors
        response.addHeader("Access-Control-Allow-Methods", "POST");
        response.addHeader("Access-Control-Allow-Origin", "*");

        // Check if transaction is older than 1 minute
        long numberOfSeconds = getNumberOfSeconds(transaction);
        if (numberOfSeconds > 60) {
            return ResponseEntity.noContent().build();
        }

        transactionRepositoryInMemory.persistTransaction(transaction);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(transaction.getAmount()).toUri();
        return ResponseEntity.created(location).build();
    }

    private long getNumberOfSeconds(Transaction transaction) {
        Instant transactionInstant = transaction.getTimestamp();
        Instant rightNow = Instant.now();
        Duration duration = Duration.between(transactionInstant, rightNow);
        return duration.getSeconds();
    }
}
