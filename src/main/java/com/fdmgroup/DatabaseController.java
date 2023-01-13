package com.fdmgroup;

import com.fdmgroup.database.Accounts;
import com.fdmgroup.database.Transfers;
import com.fdmgroup.database.Users;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {

    private final String persistanceUnitName = "BankingPU";
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public DatabaseController() {
        this.emf =  Persistence.createEntityManagerFactory(persistanceUnitName);
        this.em = emf.createEntityManager();
        this.tx =  em.getTransaction();
    }

    public Users findUser (int id){
        return em.find(Users.class,id);
    }

    public Users findUserByMail (String email){
        TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE email=\'"+email+"\'", Users.class);
        try{
            Users u = query.getSingleResult();
            return(u);
        }catch (NoResultException e){
            return null;
        }
    }

    public boolean createNewUser(String firstName, String lastName, String email, String password){
        int user_id = ((Long) em.createQuery("SELECT COUNT(*) FROM Users").getSingleResult()).intValue();
        Users user = new Users(
                user_id,
                email,
                firstName,
                lastName,
                password
        );
        try{
            tx.begin();
            em.persist(user);
            tx.commit();
        }catch (RollbackException e){
            return false;
        }

        Users u = em.find(Users.class,user_id);
        if(u.getEmail().equals(email)){
            return true;
        }else {
            return false;
        }
    }

    public boolean updateUser(Users u, String email, String password){
        if(!u.getPassword().equals(password))return false;
        Users managedUser = em.merge(u);
        try{
            tx.begin();
            managedUser.setEmail(email);
            tx.commit();
        }catch (RollbackException e){
            return false;
        }
        return true;
    }

    public boolean deleteUser(int id){
        Users u = em.find(Users.class, id);
        if(u != null){
            tx.begin();
            em.remove(u);
            tx.commit();
        }else{
            return false;
        }
        return true;
    }

    public boolean createNewAccount(Users user){
        int randomId;
        do {
            randomId = (int) Math.floor(Math.random() * (99999 - 10000 + 1) + 10);
        }while(em.find(Accounts.class,randomId)!=null);
        Accounts account = new Accounts(randomId, user);
        try{
            tx.begin();
            em.persist(account);
            tx.commit();
        }catch (RollbackException e){
            return false;
        }

        Accounts u = em.find(Accounts.class,randomId);
        if(u.getOwner().equals(user)){
            return true;
        }else {
            return false;
        }
    }

    public boolean deleteAccount(Accounts account){
        if(account != null){
            deleteTransfer(account);
            tx.begin();
            em.remove(account);
            tx.commit();
        }else{
            return false;
        }
        return true;
    }

    public List<Accounts> getUserAccounts(Users user){
        Query query = em.createQuery("SELECT a FROM Accounts a WHERE owner=?1",Accounts.class);
        List<Accounts> userAccounts = query.setParameter(1,user).getResultList();
        return userAccounts;
    }

    public boolean changeBalance(Accounts account, Boolean isDeposit, int amount){
        Accounts a = em.find(Accounts.class,account.getAccountNumber());
        if(!isDeposit && a.getBalance()<amount)return false;
        Accounts mergedAccount = em.merge(a);
        try{
            tx.begin();
            if(isDeposit){
                mergedAccount.setBalance(a.getBalance() + amount);
            }else{
                mergedAccount.setBalance(a.getBalance() - amount);
            }
            tx.commit();
        }catch (RollbackException e){
            return false;
        }
        return true;
    }

    public boolean createTransfer(Accounts from, int accountToNumber, int amount){
        Accounts to = em.find(Accounts.class,accountToNumber);
        if(to == null || from.getBalance()<amount) return false;
        int transfer_id = ((Long) em.createQuery("SELECT COUNT(*) FROM Transfers").getSingleResult()).intValue();
        Transfers transfer = new Transfers(
                transfer_id,
                from,
                to,
                amount
        );
        try{
            tx.begin();
            em.persist(transfer);
            tx.commit();
            if(!(changeBalance(from,false,amount))||!(changeBalance(to,true,amount))) return false;
        }catch (RollbackException e){
            return false;
        }

        Transfers t = em.find(Transfers.class,transfer_id);
        return ((t.getFrom().equals(from)) && (t.getTo().equals(to)) && (t.getAmount() == amount));
    }

    public boolean deleteTransfer(Accounts accounts){
        List<Accounts> accountsLists = new ArrayList<Accounts>();
        accountsLists.add(accounts);
        List<Transfers> transfersList = getUserTransfers(accountsLists);
        if(transfersList == null)return false;
        tx.begin();
        transfersList.forEach(transfer -> {
            em.remove(transfer);
        });
        tx.commit();
        return true;
    }

    public List<Transfers> getUserTransfers (List<Accounts> accounts){
        Query query = em.createQuery("SELECT t FROM Transfers t WHERE t.accountFrom IN (?1) OR t.accountTo IN (?2) ORDER BY date",Transfers.class);
        List<Transfers> userTransfers = query.setParameter(1,accounts).setParameter(2,accounts).getResultList();
        return userTransfers;
    }

    public void close(){
        em.close();
        emf.close();
    }
}
