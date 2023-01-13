package com.fdmgroup;

import com.fdmgroup.database.Accounts;
import com.fdmgroup.database.Transfers;
import com.fdmgroup.database.Users;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DatabaseController dc = new DatabaseController();
//        DatabaseController dbController = new DatabaseController();
//        //System.out.println(dbController.updateUser(0, "test@gmail.com", "test"));
//        //System.out.println(dbController.findUser(0));
//        System.out.println(dbController.deleteUser(0));
//        dbController.close();

        System.out.println("╔═══════════════════════════════╗");
        System.out.println("║                               ║");
        System.out.println("║        BANKING SYSTEM         ║");
        System.out.println("║              1.0              ║");
        System.out.println("║                               ║");
        System.out.println("╚═══════════════════════════════╝");
        System.out.println("\n PRESS ANYTHING TO START...");
        sc.next().charAt(0);
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        int loginOrRegisterChoice = 0;
        do {
            loginOrRegisterChoice = loginOrRegister(sc);
            if(loginOrRegisterChoice == 2){
                System.out.println(register(sc, dc));
                sleep(1000);
            }
        }while(loginOrRegisterChoice != 1);
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        Users currentUser;
        do{
            currentUser = login(sc,dc);
        }while (currentUser == null);
        mainMenu(currentUser, sc, dc);
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        System.out.println("SYSTEM CLOSED");
        dc.close();
        sleep(1000);
    }

    public static int loginOrRegister(Scanner sc){
        System.out.println("\nWhat do you want to do?");
        System.out.println("[1] Login");
        System.out.println("[2] Register");
        return sc.nextInt();
    }

    public static String register(Scanner sc, DatabaseController dc){
        String firstName,lastName,email,password;
        System.out.println("\nREGISTRATION");
        sc.nextLine();
        System.out.println("First name:");
        firstName = sc.nextLine();
        System.out.println("Last name:");
        lastName = sc.nextLine();
        System.out.println("Email address:");
        email = sc.nextLine();
        System.out.println("Password:");
        password = sc.nextLine();
        if(dc.createNewUser(firstName,lastName,email,password)){
            return "Registered successfully, now you can log in.";
        }else{
            return "Error during registration process. Try again later, or using different email";
        }
    }

    public static Users login(Scanner sc, DatabaseController dc){
        String email,password;
        System.out.println("\nLOGIN");
        sc.nextLine();
        System.out.println("Email address:");
        email = sc.nextLine();
        System.out.println("Password:");
        password = sc.nextLine();
        Users u = dc.findUserByMail(email);
        if (u == null || !(u.getPassword().equals(password))) {
            System.out.println("Wrong mail or password");
            return null;
        } else {
            System.out.println("You are logged in\n");
            return (u);
        }
    }

    public static void mainMenu(Users user, Scanner sc, DatabaseController dc){
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        List<Accounts> userAccounts = dc.getUserAccounts(user);
        System.out.println("╔═══════════╗");
        System.out.println("║ MAIN MENU ║");
        System.out.println("╚═══════════╝");
        System.out.println("\n Welcome "+user.getFirstName()+"!");
        System.out.println("\n Your data: ");
        System.out.println("Name: "+user.getFirstName()+" "+user.getLastName());
        System.out.println("Email: "+user.getEmail());
        System.out.println("Accounts (Number | balance): ");
        userAccounts.forEach(userAccount -> {
            System.out.println("─ "+userAccount.getAccountNumber() + " | $" +userAccount.getBalance());
        });
        System.out.println("\n What do you want to do today?");
        System.out.println("[1] Open new bank account");
        System.out.println("[2] Withdraw money");
        System.out.println("[3] Deposit money");
        System.out.println("[4] New transfer");
        System.out.println("[5] Transfers list");
        System.out.println("[6] Change email");
        System.out.println("[7] Close bank account");
        System.out.println("[8] Close system");
        int choice = sc.nextInt();
        switch (choice){
            case 1:
                newBankAccount(user, dc, sc);
                sleep(400);
                mainMenu(user,sc,dc);
                break;
            case 2:
                withdrawMoney(userAccounts,dc,sc);
                sleep(1000);
                mainMenu(user,sc,dc);
                break;
            case 3:
                depositMoney(userAccounts,dc,sc);
                sleep(1000);
                mainMenu(user,sc,dc);
                break;
            case 4:
                newTransfer(userAccounts,dc,sc);
                sleep(1000);
                mainMenu(user,sc,dc);
                break;
            case 5:
                transfersList(userAccounts,dc,sc);
                sleep(1000);
                mainMenu(user,sc,dc);
                break;
            case 6:
                changeEmail(user,dc,sc);
                sleep(1000);
                mainMenu(user,sc,dc);
                break;
            case 7:
                deleteBankAccount(userAccounts,dc,sc);
                sleep(1000);
                mainMenu(user,sc,dc);
                break;
            case 8:
                break;
            default:
                mainMenu(user,sc,dc);
        }
    }
    public static void newBankAccount(Users user,DatabaseController dc, Scanner sc){
        if(!dc.createNewAccount(user)) System.out.println("Bank account creation failed");
    }

    public static void deleteBankAccount(List<Accounts> accounts, DatabaseController dc, Scanner sc){
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        System.out.println("WARNING: ALL MONEY WILL BE LOST");
        System.out.println("\n Which account do you want do delete?");
        System.out.println("\nAccounts (Number | balance): ");
        for(int i = 0; i<accounts.size();i++){
            System.out.println("["+(i+1)+"] "+accounts.get(i).getAccountNumber() + " | $" +accounts.get(i).getBalance());
        }
        System.out.println("\nWhich account do you want do delete?");
        int accountIndex = sc.nextInt();
        if(dc.deleteAccount(accounts.get(accountIndex-1))){
            System.out.println("\nAccount has been deleted");
        }else{
            System.out.println("\nSomething went wrong! Please, try again later");
        };
    }

    public static void withdrawMoney(List<Accounts> accounts, DatabaseController dc, Scanner sc){
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        System.out.println("\n How much do you want to withdraw?");
        int deposit = sc.nextInt();
        System.out.println("\nAccounts (Number | balance): ");
        for(int i = 0; i<accounts.size();i++){
            System.out.println("["+(i+1)+"] "+accounts.get(i).getAccountNumber() + " | $" +accounts.get(i).getBalance());
        }
        System.out.println("\nChoose account to withdraw:");
        int accountIndex = sc.nextInt();
        if(dc.changeBalance(accounts.get(accountIndex-1),false,deposit)){
            System.out.println("\nYour money have benn withdrawn");
        }else{
            System.out.println("\nSomething went wrong! Check again your withdraw amount and please, try again");
        };
    }

    public static void depositMoney(List<Accounts> accounts, DatabaseController dc, Scanner sc){
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        System.out.println("\n How much do you want to deposit?");
        int deposit = sc.nextInt();
        System.out.println("\nAccounts (Number | balance): ");
        for(int i = 0; i<accounts.size();i++){
            System.out.println("["+(i+1)+"] "+accounts.get(i).getAccountNumber() + " | $" +accounts.get(i).getBalance());
        }
        System.out.println("\nChoose account to deposit:");
        int accountIndex = sc.nextInt();
        if(dc.changeBalance(accounts.get(accountIndex-1),true,deposit)){
            System.out.println("\nYour money have benn deposited");
        }else{
            System.out.println("\nSomething went wrong! Please, try again");
        };
    }

    public static void newTransfer(List<Accounts> accounts, DatabaseController dc, Scanner sc){
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        System.out.println("\nAccounts (Number | balance): ");
        for(int i = 0; i<accounts.size();i++){
            System.out.println("["+(i+1)+"] "+accounts.get(i).getAccountNumber() + " | $" +accounts.get(i).getBalance());
        }
        System.out.println("\nChoose account to transfer from:");
        int accountIndex = sc.nextInt();
        System.out.println("\n How much do you want to transfer?");
        int amount = sc.nextInt();
        System.out.println("\n Enter account number that you want to send to:");
        int accountToNumber = sc.nextInt();
        if(dc.createTransfer(accounts.get(accountIndex-1),accountToNumber,amount)){
            System.out.println("\nYour money have been sent");
        }else{
            System.out.println("\nSomething went wrong! Check again account numbers or amount and try again");
        };
    }

    public static void transfersList(List<Accounts> accounts, DatabaseController dc, Scanner sc){
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        System.out.println("\nTRANSFER HISTORY: ");
        List<Transfers> transfersList = dc.getUserTransfers(accounts);
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        transfersList.forEach(transfer -> {
            System.out.println("─ "+transfer.getFrom().getAccountNumber()+" -> "+transfer.getTo().getAccountNumber()+" | Amount: "+transfer.getAmount() + " | "+
                     formatter.format(transfer.getDate())
                    );
        });
        System.out.println("Press [1] to return to main menu");
        sc.nextInt();
    }

    public static void changeEmail(Users user,DatabaseController dc, Scanner sc){
        System.out.println("┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
        String email,password;
        System.out.println("\nCHANGE EMAIL ADDRESS");
        sc.nextLine();
        System.out.println("Email address:");
        email = sc.nextLine();
        System.out.println("Confirm with password:");
        password = sc.nextLine();
        if(dc.updateUser(user, email, password)){
            System.out.println("Email changed successfully.");
        }else{
            System.out.println("Error during email changing process. Try again later, or use different email");
        }
    }

    private static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}