package atm;

import java.util.ArrayList;

public class ATM {
    private ATMBills bills;
    private ArrayList<Integer> noOfDispenseBills;

    ATM() {
        bills = new ATMBills();
        noOfDispenseBills = new ArrayList<>();
        initDispenseBills();
    }

    int calculateATMCash() {
        int max = 0;
        for(int i = 0; i < bills.getBills().size(); i++) {
            max += (bills.getBill(i) * bills.getNoOfBills().get(i));
        }
        return max;
    }

    void setNoOfBills(ArrayList<Integer> billList){
        bills.setNoOfBills(billList);
    }

    boolean enhancedDispense(ATMAccount acc, int amount) {
        float balance = acc.getBalance();
        if(amount > balance) {
            System.out.println("Insufficient balance. You have P" + balance + " in your account.");
        } else if(amount > calculateATMCash()) {
            System.out.println("Cannot withdraw. Remaining cash is insufficient.");
        } else {
            // dummyAmount is used for manipulating amount without changing its value
            int dummyAmount = amount;
            int indexOf50 = bills.getIndexOfBill(50);

            if(dummyAmount % 20 == 0) { // if divisible, distribute to other denominations
                distributeDenominations(dummyAmount);
                return true;
            } else if((dummyAmount % 20 == 10) && (dummyAmount >= 50) && (bills.getRemainingBill(indexOf50) >= 1)) { // can be divisible by 20 if amount is deducted by 50
                dummyAmount -= 50;
                update(50, 1);
                distributeDenominations(dummyAmount);
                return true;
            } else {
                System.out.println("Invalid cash withdrawal.");
            }
        } return false;
    }

    void distributeDenominations(int amount) {
        // loop thru denominations and convert basic denomination to denomination next to it
        for (int i = 0, skip = 0; i < bills.getSize(); i++) {
            // if denomination is 20, skip 50
            if(i != bills.getIndexOfBill(50)) {
                // calculate total amount of basic denomination
                int totalBasicDenomination = amount / bills.getBill(i);
                // convert basic denomination to next denomination (skipping 50)
                int neededForUpgrade = 1;
                if(i == 0) { // skip 50 bill
                    neededForUpgrade = bills.getBill(i + 2) / bills.getBill(i);
                } else if(i + 1 < bills.getSize()) {
                    neededForUpgrade = bills.getBill(i + 1) / bills.getBill(i);
                }
                // amount of bills of next denomination
                int upgradedBill = totalBasicDenomination / neededForUpgrade;
                // bills that cannot be converted to next denomination
                int remainingBill = totalBasicDenomination % neededForUpgrade;
                // check if ATM bills of this type is enough for remainingBills
                if (bills.getRemainingBill(0) < remainingBill) { // skip i bill type
                    skip++;
                } else {
                    skip = 0;
                    update(bills.getBill(i), remainingBill);
                }
            }
        }
    }

    void update(int bill, int numberOfUsedBills) {
        // update no of dispensed bills
        int indexOfBill = bills.getIndexOfBill(bill);
        // update dispensed bills
        noOfDispenseBills.set(indexOfBill, noOfDispenseBills.get(indexOfBill) + numberOfUsedBills);
        // update atm bills
        bills.deductBills(indexOfBill, numberOfUsedBills);
    }

    void withdraw(ATMAccount acc, int amount) {
        System.out.println("==============================");
        System.out.println("Amount: P" + amount);
        if(enhancedDispense(acc, amount)) {
            System.out.println("Previous balance: P" + acc.getBalance());
            acc.updateBalance(amount);
            acc.printBalance();
            printDispensedBills();
        } else {
            reset();
        }
        System.out.println("==============================");
    }

    // reset values of saved dispensed and noOfRemaining bills
    void reset() {
        noOfDispenseBills.clear();
        initDispenseBills();
    }

    // initialize all dispensed bills to 0
    void initDispenseBills() {
        for(int i = 0; i < bills.getBills().size(); i++) {
            noOfDispenseBills.add(0);
        }
    }

    void printDispensedBills() {
        System.out.println("==============================");
        System.out.println("Dispensed Bills: ");
        int total = 0;
        for(int i = bills.getBills().size() - 1; i >= 0; i--) {
            int noOfDispensedBill = noOfDispenseBills.get(i);
            if(noOfDispensedBill > 0) {
                System.out.println("P" + bills.getBill(i) + "  " + noOfDispensedBill);
            }
            total += (noOfDispensedBill * bills.getBill(i));
        }
        System.out.println("Total: " + total);
    }

//====================================================================================//
//====================================================================================//

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.testcase();
    }

//====================================================================================//
// TESTCASES
//====================================================================================//
    /*
    This test case creates an account with a balance of P50000.
    The user will withdraw P10 up to P1000 with increments of 10 every withdraw
    */
    void testcase() {
        ATM atm = new ATM();
        ATMAccount acc = new ATMAccount(50000);

        for(int i = 1, amount = 10, increment = 10; i <= 100; i++, amount += increment) {
            System.out.println("\n\nWITHDRAW #" + i);
            atm.withdraw(acc, amount);
            atm.reset();
        }
    }

//====================================================================================//
// END TESTCASES
//====================================================================================//
}