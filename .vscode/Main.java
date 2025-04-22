// Online Java Compiler
// Use this editor to write, compile and run your Java code online

import java.io.*;
import java.util.*;

class UserAccount implements Serializable {
    private String username;
    private String password;
    private double balance;

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
    }

    public String getUsername() { return username; }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

class BankSystem {
    private static final String DATA_FILE = "users.dat";
    private Map<String, UserAccount> users;

    public BankSystem() {
        users = loadUsers();
    }

    private Map<String, UserAccount> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (Map<String, UserAccount>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, new UserAccount(username, password));
        saveUsers();
        return true;
    }

    public UserAccount login(String username, String password) {
        UserAccount user = users.get(username);
        return (user != null && user.checkPassword(password)) ? user : null;
    }

    public void update() {
        saveUsers();
    }
}

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BankSystem bank = new BankSystem();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Online Banking System ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> {
                    System.out.println("Exiting. Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void register() {
        System.out.print("Enter username: ");
        String user = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();
        if (bank.register(user, pass)) {
            System.out.println("Registered successfully!");
        } else {
            System.out.println("Username already exists.");
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String user = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        UserAccount acc = bank.login(user, pass);
        if (acc != null) {
            System.out.println("Login successful!");
            dashboard(acc);
        } else {
            System.out.println("Login failed.");
        }
    }

    private static void dashboard(UserAccount acc) {
        while (true) {
            System.out.println("\n--- Dashboard ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Balance Check");
            System.out.println("4. Logout");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter amount: ");
                    acc.deposit(scanner.nextDouble());
                    bank.update();
                    System.out.println("Deposit successful.");
                }
                case 2 -> {
                    System.out.print("Enter amount: ");
                    double amt = scanner.nextDouble();
                    if (acc.withdraw(amt)) {
                        bank.update();
                        System.out.println("Withdrawal successful.");
                    } else {
                        System.out.println("Insufficient funds.");
                    }
                }
                case 3 -> System.out.println("Balance: ₹" + acc.getBalance());
                case 4 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}

