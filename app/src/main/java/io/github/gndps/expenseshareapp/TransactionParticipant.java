package io.github.gndps.expenseshareapp;

import java.util.Date;

/**
 * Created by Gundeep on 10/03/18.
 */

public class TransactionParticipant {

    String name;
    int paid;
    int share;

    public TransactionParticipant(String name, int paid, int share) {
        this.name = name;
        this.paid = paid;
        this.share = share;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }
}
