package atm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ATMBeforeNewAlgo {
    private ATMBills bills;
    private ArrayList<Integer> noOfDispenseBills;
    private Hashtable possibleAmount;

    ATMBeforeNewAlgo() {
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

//    void setNoOfBills(int a, int b, int c, int d, int e){
//        bills.setNoOfBills(a, b, c, d, e);
//    }

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
            System.out.println("dummy before special case: " + dummyAmount);
            dummyAmount = specialCase(dummyAmount);

            if(dummyAmount != 0) {
                for(int i = bills.getBills().size() - 1; i >= 0; i--) {
                    int currentBill = bills.getBill(i);
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
            }

            System.out.println("dummy after special case: " + dummyAmount);


            System.out.println("Total: " + getTotalAmountOfDispensedBills());
            // final check to make sure dispensed bills equals amount
            if(getTotalAmountOfDispensedBills() == amount) {
                return true;
            } else {
                System.out.println("Algo error.");
                return false;
            }
        }
    }

    int specialCase(int amount) {
        // check if there is 20 and 50 denomination
        if(getDenominationIndex(20) >= 0 && getDenominationIndex(50) >= 0) {
//            System.out.println("ijdhioqdoqhdoiasodcbasbqwd");
            int dummyAmount = amount;
//            amount -= dummyAmount;

            if(amount /100 > 1) {
                dummyAmount %= amount;
            }
            // a special case for 20 and 50
            if(dummyAmount < 100) { // check if there is an irregular number
                if(getTotalAmountOfDispensedBills(20) >= dummyAmount) {
                    if(dummyAmount % 20 == 0) {
                        dummyAmount = 0;
                        return amount - dummyAmount;
                    }
                }
                if((getTotalAmountOfDispensedBills(50) + getTotalAmountOfDispensedBills(20)) >= dummyAmount % 100) {
                    if(dummyAmount % 20 == 0) {
                        return dummyAmount;
                    } else if((dummyAmount - 50) % 20 == 0) {
                        dummyAmount -= 50;
                        update(50, 1);
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
//        ATM atm = new ATM();
//        atm.testcase();

        for(int i = 0; i < 50; i+=10) {
            System.out.println(i + " % 20 = " + (i % 20));
        }

        for(int i = 0, n = 50; i < 10; i++) {
            int total = i * 20 + 50;
            System.out.println("50 + 20 * " + (i) + " = " + total + "  % 20 = " + (total % 20));
        }
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
        ATMBeforeNewAlgo atm = new ATMBeforeNewAlgo();
        ATMAccount acc = new ATMAccount(5000);

//        for(int k = 0; k < 4; k++) {
//            atm.setNoOfBills(99, 99, 99, 99, 99);
            for (int i = 1, amount = 10, increment = 10; i <= 110; i++, amount += increment) {
                System.out.println("amount: " + amount);
                System.out.println("balance: " + acc.getBalance());
                atm.withdraw(acc, amount);
                atm.reset();
                //            acc.updateBalance(amount);
                if (i % 10 == 0) {
                    increment += 10;
                    System.out.println("");
                    acc.addBalance(4000 * (i / 10));
                }
            }
//        }
    }

//====================================================================================//
// END TESTCASES
//====================================================================================//
}