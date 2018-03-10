package io.github.gndps.expenseshareapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gundeep on 10/03/18.
 */

public class MySingleton {

    private static MySingleton uniqInstance;
    private List<Transaction> transactionList;

    private MySingleton() {
    }

    public static MySingleton getInstance() {
        if (uniqInstance == null)
            uniqInstance = new MySingleton();
        return uniqInstance;
    }

    public List<Transaction> getTransactionList() {
        if (transactionList == null)
            transactionList = new ArrayList<>();
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
