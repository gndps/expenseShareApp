package io.github.gndps.expenseshareapp;

import java.util.List;
import java.util.Random;

/**
 * Created by Gundeep on 10/03/18.
 */

public class Transaction {

    List<TransactionParticipant> transaction;
    int id;

    public Transaction(List<TransactionParticipant> transaction) {
        this.transaction = transaction;
        int random = new Random().nextInt(10000);
        this.id = random;
    }

    public List<TransactionParticipant> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<TransactionParticipant> transaction) {
        this.transaction = transaction;
        int random = new Random().nextInt(10000);
        this.id = random;
    }
}
