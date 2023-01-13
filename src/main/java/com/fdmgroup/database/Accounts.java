package com.fdmgroup.database;

import javax.persistence.*;

@Entity
public class Accounts {

    @Id
    private int accountNumber;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users owner;
    private int balance;

    public Accounts(){
        super();
    }

    public Accounts(int accountNumber, Users owner) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = 0;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public Users getOwner() {
        return owner;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
