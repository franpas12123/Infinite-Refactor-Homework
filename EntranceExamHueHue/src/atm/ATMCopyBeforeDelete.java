package atm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ATMCopyBeforeDelete {
    private ATMBills bills;
    private ArrayList<Integer> noOfDispenseBills;
    private Hashtable possibleAmount;

    ATMCopyBeforeDelete() {
        bills = new ATMBills();
        noOfDispenseBills = new ArrayList<>();
        initDispenseBills();
        possibleAmount = new Hashtable();
    }

    void printATMBills() {
        bills.printATMBills();
    }

    void checkBalance(ATMAccount acc) {
        System.out.println("Balance: " + acc.getBalance());
    }

    int calculateATMCash() {
        int max = 0;
        for(int i = 0; i < bills.getBills().size(); i++) {
            max += (bills.getBill(i) * bills.getNoOfBills().get(i));
        }
        return max;
    }

    // this dispense method does not work perfectly -_-
    boolean dispense(ATMAccount acc, int amount) {
        float balance = acc.getBalance();

        if(amount > balance) {
            System.out.println("Insufficient balance. You have P" + balance + " in your account.");
            return false;
        } else if(amount > calculateATMCash()) {
            System.out.println("Cannot withdraw. Remaining cash is insufficient.");
            return false;
        } else {
            // dummyAmount is used for manipulating amount without changing its value
            int dummyAmount = amount;
            // loop thru bills
            for(int i = bills.getBills().size() - 1; i > 0; i--) {
                int currentBill = bills.getBill(i);
                // check if currentBill can be used to dispense amount and if the currentBill is not all used up
                if(dummyAmount >= currentBill && bills.getNoOfBills().get(i) > 0) {
                    // deduct currentBill until amount is less than currentBill or there are no bills left
                    do {
                        dummyAmount -= currentBill;
                        noOfDispenseBills.set(i, noOfDispenseBills.get(i) + 1);
                        bills.deductBills(i, 1);

                        if(dummyAmount == 0) {
                            return true;
                        }
                    } while(dummyAmount >= currentBill && bills.getNoOfBills().get(i) > 0);
                }
//                else {
//                    System.out.println("Cannot withdraw. Remaining cash is insufficient.");
//                    return false;
//                }
            }
            if(dummyAmount > 0) {
                if (dummyAmount % 20 == 0) {
                    // deduct currentBill until amount is less than currentBill
                    do {
                        dummyAmount -= 20;
                        noOfDispenseBills.set(bills.getBills().indexOf(20), (noOfDispenseBills.get(bills.getBills().indexOf(20)) + 1));
                        bills.deductBills(bills.getBills().indexOf(20), 1);
                    } while (dummyAmount >= 20 && bills.getNoOfBills().get(bills.getBills().indexOf(20)) > 0);
                    return true;
                } else { // cannot dispense cash
                    System.out.println("ATM cannot dispense cash.");
                    return false;
                }
            } else { // cannot dispense cash
                System.out.println("ATM cannot dispense cash.");
                return false;
            }
        }
    }

    boolean enhancedDispense(ATMAccount acc, int amount) {
        float balance = acc.getBalance();
        if(amount > balance) {
            System.out.println("Insufficient balance. You have P" + balance + " in your account.");
            return false;
        } else if(amount > calculateATMCash()) {
            System.out.println("Cannot withdraw. Remaining cash is insufficient.");
            return false;
        } else {
            // dummyAmount is used for manipulating amount without changing its value
            int dummyAmount = amount;
            dummyAmount = specialCase(dummyAmount);
//            System.out.println(dummyAmount);

            for(int i = bills.getBills().size() - 1; i >= 0; i--) {
                int currentBill = bills.getBill(i);
//                System.out.println("Bills.getNoOfBills.get(" + i + "): " + bills.getNoOfBills().get(i));
//                System.out.println("currentBill: " + currentBill);
//                System.out.println("dummyAmount: " + dummyAmount);
                // check if currentBill can be used to dispense amount and if the currentBill is not all used up
                if(dummyAmount >= currentBill && bills.getNoOfBills().get(i) > 0) {
                    int billToDispense = dummyAmount / currentBill;
                    // check if there are enough bills to dispense
                    if(billToDispense <= bills.getNoOfBills().get(i)) {
                        dummyAmount -= currentBill * billToDispense; // deduct dummy amount by bills * billToDispense
                        update(currentBill, billToDispense);
                    }
                }
//                else {
//                    System.out.println("Cannot withdraw. Remaining cash is insufficient.");
//                    return false;
//                }
            }

            // final check to make sure dispensed bills equals amount
            if(getTotalAmountOfDispensedBills() == amount) {
                return true;
            } else {
                System.out.println("Algo error.");
                return false;
            }
        }
    }

    int specialCase(int dummyAmount) {
        // check if there is 20 and 50 denomination
        if(getDenominationIndex(20) >= 0 && getDenominationIndex(50) >= 0) {
            // a special case for 20 and 50
            if(dummyAmount / 100 > 1) { // check if there is an irregular number
                if((getTotalAmountOfDispensedBills(50) + getTotalAmountOfDispensedBills(20)) >= dummyAmount % 100) {
                    if((dummyAmount - 50) % 20 == 0) {
                        dummyAmount -= 50;
                        update(50, 1);
                    }
                    if(dummyAmount % 20 == 0) {
                        return dummyAmount;
                    }
                }
            }
        }
        return -1;
    }

    void update(int bill, int numberOfUsedBills) {
        // update no of dispensed bills
        System.out.println("index: " + getDenominationIndex(bill));
        noOfDispenseBills.set(getDenominationIndex(bill), noOfDispenseBills.get(getDenominationIndex(bill)) + numberOfUsedBills);
        // update atm bills
        bills.deductBills(getDenominationIndex(bill), numberOfUsedBills);
    }

    int getDenominationIndex(int denomination) {
        return bills.getBills().indexOf(denomination);
    }

    int getTotalAmountOfDispensedBills() {
        int totalAmount = 0;
        for(int i = 0; i < noOfDispenseBills.size(); i++) {
            totalAmount += (noOfDispenseBills.get(i) * bills.getBill(i));
        } return totalAmount;
    }

    int getTotalAmountOfDispensedBills(int bill) {
        return bills.getNoOfBills().get(bills.getBills().indexOf(bill)) * bill;
    }

    void withdraw(ATMAccount acc, int amount) {
        if(enhancedDispense(acc, amount)) {
            System.out.println("You have withdrawn P" + amount);
            acc.updateBalance(amount);
        } else {
            reset();
        }
        acc.printBalance();
        printDispensedBills();
    }

    // reset values of saved dispensed and noOfRemaining bills
    void reset() {
        noOfDispenseBills = new ArrayList<>();
        initDispenseBills();
    }

    // initialize all dispensed bills to 0
    void initDispenseBills() {
        for(int i = 0; i < bills.getBills().size(); i++) {
            noOfDispenseBills.add(0);
        }
    }

    void printDispensedBills() {
        System.out.println("\n==============================");
        System.out.println("Dispensed Bills: ");
        for(int i = bills.getBills().size() - 1; i >= 0; i--) {
            int noOfDispensedBill = noOfDispenseBills.get(i);
            if(noOfDispensedBill > 0) {
                System.out.println("P" + bills.getBill(i) + "  " + noOfDispensedBill);
            }
        }
        System.out.println("==============================");
    }

    void sleep(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//====================================================================================//
//====================================================================================//

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ATMCopyBeforeDelete atm = new ATMCopyBeforeDelete();
        atm.testcase();
//====================================================================================//
// CREATING A NEW ACCOUNT
//====================================================================================//
//        float depositedAmount;
//        do {
//            System.out.println("You have created a bank account!");
//            atm.sleep(1);
//            System.out.print("Please enter the deposited amount: P");
//            depositedAmount = Float.parseFloat(scan.nextLine());
//        } while(depositedAmount <= 0);
//
//        ATMAccount acc = new ATMAccount(depositedAmount);

//====================================================================================//
// END CREATE NEW ACCOUNT
//====================================================================================//


//====================================================================================//
// ASK IF ATM HAS "INFINITE" NUMBER OF BILLS OR USER WILL ENTER REMAINING BILLS
//====================================================================================//
//        String choice = "";
//        do {
//            System.out.println("\nThe Infinity Stone can make all ATM bills infinite in number.");
//            atm.sleep(1);
//            System.out.print("Does the ATM have an Infinity Stone? [Y]/[N]");
//            choice = scan.nextLine();
//        } while(choice.compareTo("Y") == 0 && choice.compareTo("N") == 0);
//
//        if(choice.compareTo("y") == 0 || choice.compareTo("Y") == 1) { // enable "infinite" money
//            System.out.println();
//            atm.bills.setBillsToInfinity();
//            atm.bills.printATMBills();
//        } else {
//            atm.bills.setNoOfBills();
//        }
//====================================================================================//
// END
//====================================================================================//


//====================================================================================//
// WITHDRAW
//====================================================================================//
//        System.out.print("How much would you like to withdraw? P");
//        System.out.println();
//        int amount = scan.nextInt();
//        atm.withdraw(acc, amount);
//        atm.printATMBills();

//====================================================================================//
// END WITHDRAW
//====================================================================================//
    }
//====================================================================================//
// TESTCASES
//====================================================================================//
    /*
    This test case creates an account with a balance of P1000.
    The user will withdraw P10 up to P1000 with increments of 10, 20, 30, 40, 50, 60, 70, 80, 90, 100,
    every amount divisible by 100.
    Every time the user will withdraw from the ATM, the balance will return to 1000.
    */
    void testcase() {
        ATMCopyBeforeDelete atm = new ATMCopyBeforeDelete();
        ATMAccount acc = new ATMAccount(5000);

        for(int k = 0; k < 4; k++) {
            for (int i = 1, amount = 10, increment = 10, balance = 1000; i <= 110; i++, amount += increment) {
                System.out.println("amount: " + amount);
                System.out.println("balance: " + acc.getBalance());
                atm.withdraw(acc, amount);
                //            acc.updateBalance(amount);
                if (i % 10 == 0) {
                    increment += 10;
                    System.out.println("");
                    acc.addBalance(4000 * (i / 10));
                }
            }
        }
    }

//====================================================================================//
// END TESTCASES
//====================================================================================//
}