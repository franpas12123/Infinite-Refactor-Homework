package atm;

class ATMAccount {
    private float balance;

    ATMAccount() {
        balance = 0;
    }

    ATMAccount(float balance) {
        this.balance = balance;
    }

    ATMAccount(int balance) {
        this.balance = balance;
    }

    void updateBalance(float amount) {
        balance -= amount;
    }

    void updateBalance(int amount) {
        balance -= amount;
    }

    void addBalance(float amount) {
        balance += amount;
    }

    void addBalance(int amount) {
        balance += amount;
    }

    public float getBalance() {
        return balance;
    }

    void printBalance() {
        System.out.println("Balance: " + balance);
    }
}