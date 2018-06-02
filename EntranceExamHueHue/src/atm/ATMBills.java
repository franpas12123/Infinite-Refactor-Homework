package atm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class ATMBills {
    private ArrayList<Integer> bills;
    private ArrayList<Integer> noOfBills;

    ATMBills() {
        bills = new ArrayList<>();
        noOfBills = new ArrayList<>();

//      INITIALIZE ATM BILLS
        addDefaultDenominations();
//        setBillsToInfinity();
//        setNoOfBills();
    }

    ArrayList<Integer> getBills() {
        return bills;
    }

    ArrayList<Integer> getNoOfBills() {
        return noOfBills;
    }

    int getBill(int index) {
        return bills.get(index);
    }

    int getSize() { return bills.size(); }

    void deductBills(int index, int usedBills) {
        noOfBills.set(index, noOfBills.get(index) - usedBills);
    }

    void addDefaultDenominations() {
        bills.add(20);
        bills.add(50);
        bills.add(100);
        bills.add(500);
        bills.add(1000);
        for(int i = 0; i < bills.size(); i++) {
            noOfBills.add(999);
        }
    }

    void addDenomination(int denomination) {
        bills.add(denomination);
        Collections.sort(bills);
        noOfBills.add(bills.indexOf(denomination), 0); // initialize noOfBills of the added denomination by 0
    }

    void setBillsToInfinity() {
        noOfBills.clear();
        for(int i = 0; i < bills.size(); i++) {
            noOfBills.add(999);
        }
    }

    void setNoOfBills(int index, int remainingBills) {
        noOfBills.set(index, remainingBills);
    }

    void setNoOfBills(ArrayList<Integer> billList) {
        noOfBills.clear();
        for(int bill : billList) {
            noOfBills.add(bill);
        }
    }

    void setNoOfBills() {
        Scanner scan = new Scanner(System.in);
        for(int i = 0; i < bills.size(); i++) {
            System.out.print("Set remaining bills for P" + bills.get(i) + ": ");
            int remainingBills = scan.nextInt();
            setNoOfBills(i, remainingBills);
        }
    }

    int getIndexOfBill(int denomination) {
        return bills.indexOf(denomination);
    }

    int getRemainingBill(int index) {
        return noOfBills.get(index);
    }

    // ==================================================================================== //
// DEBUG
    void printATMBills() {
        System.out.println("ATM Bills");
        for(int i = 0; i < bills.size(); i++) {
            System.out.println("Bill: P" + bills.get(i) + "   Remaining: " + noOfBills.get(i));
        }
    }
// ==================================================================================== //
}
