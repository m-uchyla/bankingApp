package com.fdmgroup.database;

import javax.persistence.*;
import java.text.SimpleDateFormat;

@Entity
public class Transfers {

    @Id
    private int transfer_id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "accountFrom", referencedColumnName= "accountNumber")
    private Accounts accountFrom;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "accountTo", referencedColumnName= "accountNumber")
    private Accounts accountTo;

    private int amount;

    private Long date;

    public Transfers(){
        super();
    }

    public Transfers(int transfer_id, Accounts accountFrom, Accounts accountTo, int amount) {
        this.transfer_id = transfer_id;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.date = System.currentTimeMillis();
    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public Accounts getFrom() {
        return accountFrom;
    }

    public Accounts getTo() {
        return accountTo;
    }

    public int getAmount() {
        return amount;
    }

    public Long getDate() {
        return date;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        return "Transfers{" +
                "transfer_id=" + transfer_id +
                ", from=" + accountFrom +
                ", to=" + accountTo +
                ", amount=" + amount +
                ", date=" + formatter.format(date) +
                '}';
    }
}
