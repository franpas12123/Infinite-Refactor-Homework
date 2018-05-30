package atm;

import java.util.ArrayList;
import java.util.Scanner;

class ATMBills {
    private ArrayList<Integer> bills;
    private ArrayList<Integer> noOfBills;
    private int size;

    ATMBills() {
        bills = new ArrayList<>();
        noOfBills = new ArrayList<>();
        size = 0;

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

    int getSize() {
        return size;
    }

    int getBill(int index) {
        return bills.get(index);
    }

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
            noOfBills.add(0);
        }
        size = bills.size();
    }

    void setBillsToInfinity() {
        noOfBills.clear();
        for(int i = 0; i < bills.size(); i++) {
            noOfBills.add(9999);
        }
    }

    void setNoOfBills(int index, int remainingBills) {
        noOfBills.set(index, remainingBills);
    }

    void setNoOfBills() {
        Scanner scan = new Scanner(System.in);
        for(int i = 0; i < bills.size(); i++) {
            System.out.print("Set remaining bills for P" + bills.get(i) + ": ");
            int remainingBills = scan.nextInt();
            setNoOfBills(i, remainingBills);
            size++;
        }
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
